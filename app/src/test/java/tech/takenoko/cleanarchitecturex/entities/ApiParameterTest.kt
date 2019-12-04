package tech.takenoko.cleanarchitecturex.entities

import com.github.kittinunf.fuel.core.Deserializable
import com.github.kittinunf.fuel.core.Request
import com.nhaarman.mockitokotlin2.spy
import org.junit.Assert
import org.junit.Test

class ApiParameterTest {

    val mockAdapter = spy<Deserializable<String>>()

    @Test
    fun checkPram() {
        val getRequest = spy<Request> { }
        val mockGet = MockGet(getRequest)
        Assert.assertEquals(mockGet.method, "GET")
        Assert.assertEquals(mockGet.url, "")
        Assert.assertEquals(mockGet.parameters, mapOf("key" to "value"))
        Assert.assertEquals(mockGet.header, mapOf("key" to "value"))
        Assert.assertEquals(mockGet.body, null)
        Assert.assertEquals(mockGet.adapter, mockAdapter)
        Assert.assertEquals(mockGet.call(), getRequest)

        val postRequest = spy<Request> { }
        val mockPost = MockPost(postRequest)
        mockPost.call()
        Assert.assertEquals(mockPost.method, "POST")
        Assert.assertEquals(mockPost.url, "")
        Assert.assertEquals(mockPost.parameters, mapOf("key" to "value"))
        Assert.assertEquals(mockPost.header, mapOf("key" to "value"))
        Assert.assertEquals(mockPost.body, mapOf<String, String>())
        Assert.assertEquals(mockPost.adapter, mockAdapter)
        Assert.assertEquals(mockPost.call(), postRequest)
    }

    inner class MockGet(private val request: Request) : Get<String>(
        "",
        mapOf("key" to "value"),
        mapOf("key" to "value"),
        adapter = mockAdapter
    ) {
        override val call: () -> Request = { request }
    }

    inner class MockPost(private val request: Request) : Post<String>(
        "",
        mapOf("key" to "value"),
        mapOf("key" to "value"),
        mapOf<String, String>(),
        adapter = mockAdapter
    ) {
        override val call: () -> Request = { request }
    }
}
