package tech.takenoko.cleanarchitecturex.entities

import org.junit.Assert
import org.junit.Test

class HttpStatusCodeTest {

    @Test
    fun call_success() {
        Assert.assertEquals(HttpStatusCode.DEFAULT.code, 0)
        Assert.assertEquals(HttpStatusCode.OK.code, 200)
        Assert.assertEquals(HttpStatusCode.BAD_REQUEST.code, 400)
        Assert.assertEquals(HttpStatusCode.INTERNAL_SERVER_ERROR.code, 500)

        HttpStatusCode.values().forEach {
            Assert.assertTrue(it.code in 0..510)
        }
    }
}
