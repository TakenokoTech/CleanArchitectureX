package tech.takenoko.cleanarchitecturex.entities

/**
 * UseCaseの状態
 */
sealed class UsecaseResult<P> {
    class Pending<P> : UsecaseResult<P>()
    data class Resolved<P>(val value: P) : UsecaseResult<P>()
    data class Rejected<P>(val reason: Throwable) : UsecaseResult<P>()
}

fun <P> UsecaseResult<P>.isFinished() = when (this) {
    is UsecaseResult.Pending -> false
    is UsecaseResult.Resolved -> true
    is UsecaseResult.Rejected -> true
}
