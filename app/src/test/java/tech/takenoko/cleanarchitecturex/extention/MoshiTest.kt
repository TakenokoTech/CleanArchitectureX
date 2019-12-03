package tech.takenoko.cleanarchitecturex.extention

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
    fun listAdapter_success() {
        val list = listAdapter<String>().deserialize("[\"value1\", \"value2\"]")
        Assert.assertEquals(list?.get(0), "value1")
        Assert.assertEquals(list?.get(1), "value2")
    }

    data class A(val param1: String)
}
