package tech.takenoko.cleanarchitecturex.di

import androidx.annotation.WorkerThread
import com.github.kittinunf.fuel.coroutines.awaitResponseResult
import kotlin.reflect.KClass
import tech.takenoko.cleanarchitecturex.BuildConfig
import tech.takenoko.cleanarchitecturex.entities.ApiParameter
import tech.takenoko.cleanarchitecturex.entities.ApiResult
import tech.takenoko.cleanarchitecturex.extention.OBJECT_MAPPER
import tech.takenoko.cleanarchitecturex.extention.planeAdapter
import tech.takenoko.cleanarchitecturex.utils.AppLog

/**
 * Usage: https://github.com/kittinunf/fuel/tree/master/fuel-coroutines
 */
interface AppRestApi {
    suspend fun <T : Any> execute(param: ApiParameter<T>, clazz: KClass<T>): ApiResult<T>
}
suspend inline fun <reified T : Any> AppRestApi.fetch(param: ApiParameter<T>) = execute(param, T::class)

@Suppress("OVERRIDE_BY_INLINE")
class AppRestApiImpl : AppRestApi {

    @WorkerThread
    override suspend inline fun <T : Any> execute(param: ApiParameter<T>, clazz: KClass<T>): ApiResult<T> {
        AppLog.info(TAG, "${param.method} ====> $param.url")

        val bodyStr =
            if (param.body != null) OBJECT_MAPPER.adapter(Any::class.java).toJson(param.body) else ""

        val requestBuilder = param.call().header(param.header).body(bodyStr)

        val (request, response, result) = if (param.adapter != null) {
            val adapter = param.adapter ?: return ApiResult.Failed(Exception(), 0)
            requestBuilder.awaitResponseResult(adapter)
        } else {
            requestBuilder.awaitResponseResult(planeAdapter(clazz.java))
        }

        if (BuildConfig.DEBUG) {
            AppLog.debug(TAG, "request: $request")
            AppLog.debug(TAG, "response: $response")
        }

        val responseBody = result.component1()
        val error = result.component2()

        return if (responseBody != null) {
            AppLog.debug(TAG, "responseBody: $responseBody")
            ApiResult.Success(responseBody)
        } else {
            AppLog.debug(TAG, "error: ${error?.localizedMessage}")
            ApiResult.Failed(error ?: IllegalArgumentException(), response.statusCode)
        }
    }

    companion object {
        val TAG = AppRestApiImpl::class.java.simpleName
    }
}
