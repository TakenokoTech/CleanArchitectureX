package tech.takenoko.cleanarchitecturex.di

import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.test.MockHttpTestCase
import com.nhaarman.mockitokotlin2.spy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito.`when`
import tech.takenoko.cleanarchitecturex.entities.ApiParameter
import tech.takenoko.cleanarchitecturex.extention.planeAdapter

@ExperimentalCoroutinesApi
class AppRestApiTest : MockHttpTestCase() {

    @Test
    fun success() = runBlocking {
        val restApi = AppRestApiImpl()

        val request = spy<Request> { }
        val param = MockGet(request)

        `when`(request.header(mapOf())).thenReturn(request)
        `when`(request.body("")).thenReturn(request)

        kotlin.runCatching {
            restApi.execute(param, String::class)
        }

        return@runBlocking
    }

    class MockGet(private val request: Request) : ApiParameter.GetParameter<String>("", adapter = planeAdapter(String::class.java)) {
        override val call: () -> Request = { request }
    }
}
