package tech.takenoko.cleanarchitecturex.utils

object AppLog {
    private const val LOG_FORMAT: String = "<UnitTest>%s %s, %s"

    private fun Boolean.toInt() = if (this) 1 else 0
    private fun thread(): String = "[${Thread.currentThread().name}]"

    fun debug(tag: String?, msg: String) {
        println(String.format(LOG_FORMAT, thread(), tag, msg))
    }

    fun info(tag: String?, msg: String) {
        println(String.format(LOG_FORMAT, thread(), tag, msg))
    }

    fun error(tag: String?, msg: String) {
        println(String.format(LOG_FORMAT, thread(), tag, msg))
    }

    fun warn(tag: String?, t: Throwable) {
        println(String.format(LOG_FORMAT, thread(), tag, t.localizedMessage))
    }
}
