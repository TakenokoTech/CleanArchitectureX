package tech.takenoko.cleanarchitecturex.extension

import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert
import org.junit.Test

class MoshiTest {

    @Test
    fun planeAdapter_success() {
        val map = planeAdapter(Map::class.java).deserialize("{\"param1\": \"value\"}")
        Assert.assertEquals(map?.get("param1"), "value")

        val a = planeAdapter(A::class.java).deserialize("{\"param1\": \"value\"}")
        Assert.assertEquals(a?.param1, "value")
    }

    @Test
    fun planeAdapter_pattern_success() {
        val str = "{\"param1\": \"value\"}"

        val a2 = planeAdapter<A>().deserialize(inputStream = mock { })
        Assert.assertEquals(a2?.param1, null)

        val a3 = planeAdapter<A>().deserialize(reader = mock { })
        Assert.assertEquals(a3?.param1, null)

        val a4 = planeAdapter<A>().deserialize(bytes = str.toByteArray())
        Assert.assertEquals(a4?.param1, null)
    }

    @Test
    fun listAdapter_success() {
        val list = listAdapter<String>().deserialize("[\"value1\", \"value2\"]")
        Assert.assertEquals(list?.get(0), "value1")
        Assert.assertEquals(list?.get(1), "value2")

        val a = listAdapter<A>().deserialize("[{\"param1\": \"value1\"}, {\"param1\": \"value2\"}]")
        Assert.assertEquals(a?.get(0)?.param1, "value1")
        Assert.assertEquals(a?.get(1)?.param1, "value2")
    }

    @Test
    fun listAdapter_pattern_success() {
        val str = "[\"value1\", \"value2\"]"

        val a2 = listAdapter(A::class.java).deserialize(inputStream = mock { })
        Assert.assertEquals(a2?.get(0)?.param1, null)

        val a3 = listAdapter(A::class.java).deserialize(reader = mock { })
        Assert.assertEquals(a3?.get(0)?.param1, null)

        val a4 = listAdapter(A::class.java).deserialize(bytes = str.toByteArray())
        Assert.assertEquals(a4?.get(0)?.param1, null)
    }

    @Test
    fun mapAdapter_success() {
        val mapString = mapAdapter<String, String>().deserialize("{\"key1\": \"value1\", \"key2\": \"value2\"}")
        Assert.assertEquals(mapString?.get("key1"), "value1")
        Assert.assertEquals(mapString?.get("key2"), "value2")

        val mapA = mapAdapter<String, A>().deserialize("{\"key1\": {\"param1\": \"value1\"}, \"key2\": {\"param1\": \"value2\"}}")
        Assert.assertEquals(mapA?.get("key1")?.param1, "value1")
        Assert.assertEquals(mapA?.get("key2")?.param1, "value2")
    }

    data class A(val param1: String)
}
