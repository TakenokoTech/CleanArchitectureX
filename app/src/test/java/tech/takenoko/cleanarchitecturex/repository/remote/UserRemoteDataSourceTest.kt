package tech.takenoko.cleanarchitecturex.repository.remote

import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.inject
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import tech.takenoko.cleanarchitecturex.di.AppRestApi
import tech.takenoko.cleanarchitecturex.entities.ApiResult
import tech.takenoko.cleanarchitecturex.entities.Get
import tech.takenoko.cleanarchitecturex.entities.HttpStatusCode
import tech.takenoko.cleanarchitecturex.entities.Post
import tech.takenoko.cleanarchitecturex.entities.response.UserEntity

class UserRemoteDataSourceTest : AutoCloseKoinTest() {

    private val errorResponse = Exception("error response.")

    @Before
    fun before() {
        startKoin { modules(mockModule) }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun getUser_failed() = runBlocking {
        // mock api
        val getUserParam = Get<List<UserEntity>>(url = UserRemoteDataSource.getUserUrl)
        val getUserResponse = ApiResult.Failed<List<UserEntity>>(errorResponse, HttpStatusCode.INTERNAL_SERVER_ERROR.code)
        MockRestApi.response[getUserParam] = getUserResponse
        // verification
        val userRemoteDataSource by inject<UserRemoteDataSource>()
        val result = kotlin.runCatching { userRemoteDataSource.getUser() }
        Assert.assertEquals(result.exceptionOrNull()?.localizedMessage, errorResponse.localizedMessage)
    }

    @Test
    fun postUser_failed() = runBlocking {
        // mock api
        val postUserParam = Post<List<UserEntity>>(url = UserRemoteDataSource.addUserUrl, body = UserEntity("user1"))
        val postUserResponse = ApiResult.Failed<List<UserEntity>>(errorResponse, HttpStatusCode.INTERNAL_SERVER_ERROR.code)
        MockRestApi.response[postUserParam] = postUserResponse
        // verification
        val userRemoteDataSource by inject<UserRemoteDataSource>()
        val result = kotlin.runCatching { userRemoteDataSource.postUser() }
        Assert.assertEquals(result.exceptionOrNull()?.localizedMessage, errorResponse.localizedMessage)
    }

    private val mockModule: Module = module {
        factory { MockRestApi() as AppRestApi }
        factory { UserRemoteDataSource() }
    }
}
