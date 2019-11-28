package tech.takenoko.cleanarchitecturex.repository.network

import android.content.Context
import androidx.annotation.WorkerThread
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.coroutines.awaitResponseResult
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.moshi.moshiDeserializerOf
import com.github.kittinunf.result.Result
import tech.takenoko.cleanarchitecturex.extention.OBJECT_MAPPER
import tech.takenoko.cleanarchitecturex.extention.planeAdapter
import tech.takenoko.cleanarchitecturex.model.ApiParameter.*
import tech.takenoko.cleanarchitecturex.model.ApiResult
import tech.takenoko.cleanarchitecturex.utils.AppLog

/**
 * Usage: https://github.com/kittinunf/fuel/tree/master/fuel-coroutines
 */
abstract class BaseDataSource(private val context: Context) {

    @WorkerThread
    protected suspend inline fun <reified T : Any> awaitGet( param: GetParameter<T>): ApiResult<T> {
        AppLog.info(TAG, "GET ====> $param.url")
        val (request, response, result) = param.url
            .httpGet(param.parameters.map { it.key to it.value })
            .header(param.header)
            .awaitResponseResult(moshiDeserializerOf(param.adapter?: planeAdapter()))
        return createApiResult(request, response, result)
    }

    @WorkerThread
    protected suspend inline fun <reified T : Any> awaitPost( param: PostParameter<T>): ApiResult<T> {
        AppLog.info(TAG, "POST ====> $param.url")
        val bodyStr = if (param.body != null) OBJECT_MAPPER.adapter(Any::class.java).toJson(param.body) else ""
        val (request, response, result) = param.url
            .httpPost(param.parameters.map { it.key to it.value })
            .header(param.header)
            .body(bodyStr)
            .awaitResponseResult(moshiDeserializerOf(param.adapter ?: planeAdapter()))
        return createApiResult(request, response, result)
    }

    protected inline fun <reified T : Any> createApiResult(request: Request, response: Response, result: Result<T, FuelError>): ApiResult<T> {
        // AppLog.debug(TAG, "request: $request")
        // AppLog.debug(TAG, "response: $response")
        val responseBody = result.component1()
        val error = result.component2()
        return if(responseBody != null) {
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