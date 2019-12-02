package tech.takenoko.cleanarchitecturex.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.*
import org.junit.rules.TestRule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.inject
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.mockito.Mockito
import tech.takenoko.cleanarchitecturex.di.AppDatabase
import tech.takenoko.cleanarchitecturex.di.AppRestApi
import tech.takenoko.cleanarchitecturex.entities.ApiResult
import tech.takenoko.cleanarchitecturex.entities.Get
import tech.takenoko.cleanarchitecturex.entities.Post
import tech.takenoko.cleanarchitecturex.entities.response.ResultEntity
import tech.takenoko.cleanarchitecturex.entities.response.UserEntity
import tech.takenoko.cleanarchitecturex.entities.room.UserDao
import tech.takenoko.cleanarchitecturex.repository.local.MockDatabase
import tech.takenoko.cleanarchitecturex.repository.local.UserLocalDataSource
import tech.takenoko.cleanarchitecturex.repository.remote.MockRestApi
import tech.takenoko.cleanarchitecturex.repository.remote.UserRemoteDataSource
import tech.takenoko.cleanarchitecturex.repository.remote.UserRemoteDataSource.Companion.addUserUrl
import tech.takenoko.cleanarchitecturex.repository.remote.UserRemoteDataSource.Companion.getUserUrl

@ExperimentalCoroutinesApi
class UserRepositoryTest : AutoCloseKoinTest(), LifecycleOwner {
    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    // private val baseDataSource = mock(BaseDataSource::class.java)

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
    fun getAllUser_success1() = runBlocking {
        // mock api
        val getUserUrlParam = Get<List<UserEntity>>(url = getUserUrl)
        val getUserUrlResponse = ApiResult.Success<List<UserEntity>>(listOf())
        MockRestApi.response[getUserUrlParam] = getUserUrlResponse
        // mock db
        getAll = listOf(UserLocalDataSource.User("testName", "testName"))
        // verification
        val userRepository by inject<UserRepository>()
        val result = userRepository.getAllUser()
        Assert.assertEquals(result, getAll)
    }

    @Test
    fun addUser_success1() = runBlocking {
        // mock api
        val getUserUrlParam = Post<List<UserEntity>>(url = addUserUrl, body = UserEntity("user1"))
        val getUserUrlResponse = ApiResult.Success(ResultEntity(""))
        MockRestApi.response[getUserUrlParam] = getUserUrlResponse
        // verification
        val userRepository by inject<UserRepository>()
        userRepository.addUser("testName")
    }

    private val mockModule: Module = module {
        factory { UserRepositoryImpl() as UserRepository }
        factory { UserLocalDataSource() }
        factory { UserRemoteDataSource() }
        factory { MockRestApi() as AppRestApi }
        factory { MockDatabase() as AppDatabase }
    }

    var getAll = listOf<UserLocalDataSource.User>()
    var findByName = UserLocalDataSource.User("", "")

    private val userDao = object : UserDao {
        override suspend fun insertAll(vararg users: UserLocalDataSource.User) {}
        override suspend fun delete(user: UserLocalDataSource.User) {}
        override suspend fun getAll(): List<UserLocalDataSource.User> = getAll
        override suspend fun findByName(name: String): UserLocalDataSource.User = findByName
        override suspend fun deleteAll() {}
        override fun getAllToLive(): LiveData<List<UserLocalDataSource.User>> = MediatorLiveData()
    }

    private val testScope = TestCoroutineScope()
    private val context: Context = Mockito.mock(Context::class.java)
    override fun getLifecycle(): Lifecycle = ServiceLifecycleDispatcher(this).lifecycle
}
