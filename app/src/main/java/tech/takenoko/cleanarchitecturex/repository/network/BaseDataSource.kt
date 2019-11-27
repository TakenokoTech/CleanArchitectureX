package tech.takenoko.cleanarchitecturex.repository.network

import android.content.Context
import androidx.annotation.WorkerThread
import com.github.kittinunf.fuel.coroutines.awaitResponseResult
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.moshi.moshiDeserializerOf
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import tech.takenoko.cleanarchitecturex.utils.AppLog

/**
 * Usage: https://github.com/kittinunf/fuel/tree/master/fuel-coroutines
 */
abstract class BaseDataSource(private val context: Context) {

    @WorkerThread
    protected suspend inline fun <reified T : Any> awaitGet(url: String, parameters: Map<String, Any> = mapOf(), header: Map<String, Any> = mapOf()): T {
        AppLog.info(TAG, "GET ====> $url")

        val (request, response, result) = url
            .httpGet(parameters.map { it.key to it.value })
            .header(header)
            .awaitResponseResult(moshiDeserializerOf(T::class.java))

        AppLog.debug(TAG, "request: $request")
        AppLog.debug(TAG, "response: $response")

        return result.component1() ?: throw result.component2() ?: throw Error()
    }

    @WorkerThread
    protected suspend inline fun <reified T : Any> awaitPost(url: String, parameters: Map<String, Any> = mapOf(), header: Map<String, Any> = mapOf(), body: Any? = null): T {
        AppLog.info(TAG, "GET ====> $url")

        val bodyStr = if(body != null) moshi.adapter(Any::class.java).toJson(body) else ""

        val (request, response, result) = url
            .httpPost(parameters.map { it.key to it.value })
            .header(header)
            .body(bodyStr)
            .awaitResponseResult(moshiDeserializerOf(T::class.java))

        AppLog.debug(TAG, "request: $request")
        AppLog.debug(TAG, "response: $response")

        return result.component1() ?: throw result.component2() ?: throw Error()
    }

    companion object {
        val TAG = BaseDataSource::class.java.simpleName
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }
}