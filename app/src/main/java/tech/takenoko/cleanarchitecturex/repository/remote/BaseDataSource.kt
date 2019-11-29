package tech.takenoko.cleanarchitecturex.repository.remote

import android.content.Context
import androidx.annotation.WorkerThread
import com.github.kittinunf.fuel.coroutines.awaitResponseResult
import com.github.kittinunf.fuel.moshi.moshiDeserializerOf
import tech.takenoko.cleanarchitecturex.BuildConfig
import tech.takenoko.cleanarchitecturex.extention.OBJECT_MAPPER
import tech.takenoko.cleanarchitecturex.extention.planeAdapter
import tech.takenoko.cleanarchitecturex.model.ApiParameter
import tech.takenoko.cleanarchitecturex.model.ApiResult
import tech.takenoko.cleanarchitecturex.utils.AppLog

/**
 * Usage: https://github.com/kittinunf/fuel/tree/master/fuel-coroutines
 */
abstract class BaseDataSource(private val context: Context) {

    @WorkerThread
    protected suspend inline fun <reified T : Any> fetch(param: ApiParameter<T>): ApiResult<T> {
        AppLog.info(TAG, "${param.method} ====> $param.url")

        val bodyStr =
            if (param.body != null) OBJECT_MAPPER.adapter(Any::class.java).toJson(param.body) else ""

        val (request, response, result) = param.call()
            .header(param.header)
            .body(bodyStr)
            .awaitResponseResult(moshiDeserializerOf(param.adapter ?: planeAdapter()))

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
        val TAG = BaseDataSource::class.java.simpleName
    }
}
