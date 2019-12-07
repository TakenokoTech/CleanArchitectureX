package tech.takenoko.cleanarchitecturex.di

import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.test.MockHttpTestCase
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito.`when`
import tech.takenoko.cleanarchitecturex.entities.ApiParameter
import tech.takenoko.cleanarchitecturex.entities.ApiResult
import tech.takenoko.cleanarchitecturex.extension.planeAdapter

@ExperimentalCoroutinesApi
class AppRestApiTest : MockHttpTestCase() {

    @Test
    fun success() = runBlocking {
        val restApi = mock<AppRestApiImpl>()

        val request = mock<Request> { }
        val param = MockGet(request)
        `when`(request.header(mapOf())).thenReturn(request)
        `when`(request.body("")).thenReturn(request)

        val result = restApi.execute(param, String::class)
        Assert.assertEquals(result.toState(), "Failed")

        return@runBlocking
    }

    class MockGet(private val request: Request) : ApiParameter.GetParameter<String>("http", adapter = adapter) {
        override val call: () -> Request = { request }
    }

    companion object {
        val failedTestException = object : FailedTestException() {}
        private val adapter = planeAdapter(String::class.java)
    }

    open class FailedTestException : Exception()
    private fun <T> ApiResult<T>.toState(): String = this::class.java.simpleName
}
