package tech.takenoko.cleanarchitecturex.model

/**
 * UseCaseの状態
 */
sealed class UsecaseResult<P> {
    class Pending<P> : UsecaseResult<P>()
    data class Resolved<P>(val value: P): UsecaseResult<P>()
    data class Rejected<P>(val reason: Throwable): UsecaseResult<P>()
}
