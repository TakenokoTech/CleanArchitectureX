package tech.takenoko.cleanarchitecturex.extension

import tech.takenoko.cleanarchitecturex.entities.UsecaseResult

val PENDING: String = UsecaseResult.Pending::class.java.simpleName
val RESOLVED: String = UsecaseResult.Resolved::class.java.simpleName
val REJECTED: String = UsecaseResult.Rejected::class.java.simpleName
fun <T> UsecaseResult<T>.toState(): String = this::class.java.simpleName
