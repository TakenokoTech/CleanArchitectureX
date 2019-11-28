package tech.takenoko.cleanarchitecturex.model

import com.github.kittinunf.fuel.core.FuelError

/**
 * APIの状態
 */
sealed class ApiResult<P> {
    data class Success<P>(val value: P): ApiResult<P>()
    data class Failed<P>(val cause: Exception, val statusCode: Int): ApiResult<P>()
}
