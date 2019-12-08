package tech.takenoko.cleanarchitecturex.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.rules.TestRule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.inject
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import tech.takenoko.cleanarchitecturex.di.*
import tech.takenoko.cleanarchitecturex.di.AppRestApiTest.Companion.failedTestException
import tech.takenoko.cleanarchitecturex.entities.ApiResult
import tech.takenoko.cleanarchitecturex.entities.Get
import tech.takenoko.cleanarchitecturex.entities.HttpStatusCode
import tech.takenoko.cleanarchitecturex.entities.Post
import tech.takenoko.cleanarchitecturex.entities.room.UserDao
import tech.takenoko.cleanarchitecturex.extension.checkedObserver
import tech.takenoko.cleanarchitecturex.extension.mockObserver
import tech.takenoko.cleanarchitecturex.repository.local.UserLocalDataSource
import tech.takenoko.cleanarchitecturex.repository.remote.UserRemoteDataSource
import tech.takenoko.cleanarchitecturex.repository.remote.UserRemoteDataSource.Companion.addUserUrl
import tech.takenoko.cleanarchitecturex.repository.remote.UserRemoteDataSource.Companion.getUserUrl

@ExperimentalCoroutinesApi
class UserRepositoryTest : AutoCloseKoinTest() {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun before() {
        startKoin { modules(mockModule) }
        MockRestApi.response = mutableMapOf()
        MockDatabase.userDao = userDao
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun getAllUser_success() = runBlocking {
        // mock api
        val getUserUrlParam = Get<List<UserRemoteDataSource.UserEntity>>(url = getUserUrl)
        val getUserUrlResponse = ApiResult.Success(listOf(
            UserRemoteDataSource.UserEntity(
                "testUser",
                "testDisplay"
            )
        ))
        MockRestApi.response[getUserUrlParam] = getUserUrlResponse
        // mock db
        getAll = listOf(UserLocalDataSource.User("testName", "testName"))
        // verification
        val userRepository by inject<UserRepository>()
        val result = userRepository.getAllUser()
        Assert.assertEquals(result, getAll)
    }

    @Test
    fun getAllUser_failed() = runBlocking {
        // mock api
        val getUserParam = Get<List<UserRemoteDataSource.UserEntity>>(url = getUserUrl)
        val getUserResponse = ApiResult.Failed<List<UserRemoteDataSource.UserEntity>>(failedTestException, HttpStatusCode.INTERNAL_SERVER_ERROR.code)
        MockRestApi.response[getUserParam] = getUserResponse
        // mock db
        getAll = listOf(UserLocalDataSource.User("testName", "testName"))
        // verification
        val userRepository by inject<UserRepository>()
        val result = runCatching { userRepository.getAllUser() }.exceptionOrNull()
        Assert.assertEquals(result, failedTestException)
    }

    @Test
    fun addUser_success() = runBlocking {
        // mock api
        val getUserUrlParam = Post<List<UserRemoteDataSource.UserEntity>>(url = addUserUrl, body = UserRemoteDataSource.UserEntity(
            "testUser",
            "testDisplay"
        ))
        val getUserUrlResponse = ApiResult.Success(UserRemoteDataSource.ResultEntity("true"))
        MockRestApi.response[getUserUrlParam] = getUserUrlResponse
        // verification
        val userRepository by inject<UserRepository>()
        userRepository.addUser(UserLocalDataSource.User("testUser", "testDisplay"))
    }

    @Test
    fun addUser_failed() = runBlocking {
        // mock api
        val getUserUrlParam = Post<List<UserRemoteDataSource.UserEntity>>(url = addUserUrl, body = UserRemoteDataSource.UserEntity(
            "testUser",
            "testDisplay"
        ))
        val getUserUrlResponse = ApiResult.Failed<List<UserRemoteDataSource.UserEntity>>(failedTestException, HttpStatusCode.INTERNAL_SERVER_ERROR.code)
        MockRestApi.response[getUserUrlParam] = getUserUrlResponse
        // verification
        val userRepository by inject<UserRepository>()
        val result = runCatching { userRepository.addUser(UserLocalDataSource.User("testUser", "testDisplay")) }.exceptionOrNull()
        Assert.assertEquals(result, failedTestException)
    }

    @Test
    fun getAllToLive_success() {
        val mockObserver = mockObserver<List<UserLocalDataSource.User>>()
        val userRepository by inject<UserRepository>()
        userRepository.getAllToLive().observeForever(mockObserver)
        val testUser = UserLocalDataSource.User("testUser", "testDisplay")
        // Before
        getAllToLive.postValue(listOf(testUser))
        checkedObserver(mockObserver) {
            Assert.assertEquals(it, listOf(testUser))
        }
        // After
        getAllToLive.postValue(listOf(testUser, testUser))
        checkedObserver(mockObserver) {
            Assert.assertEquals(it, listOf(testUser, testUser))
        }
    }

    private val mockModule: Module = module {
        factory { UserRepositoryImpl() as UserRepository }
        factory { UserLocalDataSource() }
        factory { UserRemoteDataSource() }
        factory { MockRestApi() as AppRestApi }
        factory { MockDatabase() as AppDatabase }
    }

    var getAll: List<UserLocalDataSource.User>? = null
    val getAllToLive = MediatorLiveData<List<UserLocalDataSource.User>>()

    private val userDao = object : UserDao {
        override suspend fun insertAll(vararg users: UserLocalDataSource.User) {}
        override suspend fun getAll(): List<UserLocalDataSource.User> = getAll!!
        override suspend fun deleteAll() {}
        override fun getAllToLive(): LiveData<List<UserLocalDataSource.User>> = getAllToLive
    }
}
