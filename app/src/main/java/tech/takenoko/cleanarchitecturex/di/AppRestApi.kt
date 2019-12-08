package tech.takenoko.cleanarchitecturex.di

import androidx.annotation.WorkerThread
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.coroutines.awaitResponseResult
import com.github.kittinunf.result.Result
import java.lang.Exception
import kotlin.reflect.KClass
import tech.takenoko.cleanarchitecturex.BuildConfig
import tech.takenoko.cleanarchitecturex.entities.ApiParameter
import tech.takenoko.cleanarchitecturex.entities.ApiResult
import tech.takenoko.cleanarchitecturex.extension.OBJECT_MAPPER
import tech.takenoko.cleanarchitecturex.extension.planeAdapter
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
        val adapter = param.adapter ?: planeAdapter(clazz.java)

        val (request, response, result) = runCatching {
            requestBuilder.awaitResponseResult(adapter)
        }.getOrElse {
            Triple(requestBuilder, Response.error(), Result.error(Exception(it)))
        }

        if (BuildConfig.DEBUG) {
            AppLog.debug(TAG, "request: $request")
            AppLog.debug(TAG, "response: $response")
        }

        return result.fold({
            AppLog.debug(TAG, "responseBody: $it")
            ApiResult.Success(it)
        }, {
            AppLog.debug(TAG, "error: ${it.localizedMessage}")
            ApiResult.Failed(it, response.statusCode)
        })
    }

    companion object {
        val TAG = AppRestApiImpl::class.java.simpleName
    }
}
