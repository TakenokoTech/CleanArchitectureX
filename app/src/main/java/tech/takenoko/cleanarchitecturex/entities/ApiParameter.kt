package tech.takenoko.cleanarchitecturex.entities

import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.squareup.moshi.JsonAdapter

typealias Get<T> = ApiParameter.GetParameter<T>
typealias Post<T> = ApiParameter.PostParameter<T>

sealed class ApiParameter<T : Any>(
    open val url: String,
    open val parameters: Map<String, Any>,
    open val header: Map<String, Any>,
    open val body: Any?,
    open val adapter: JsonAdapter<T>?
) {
    open val method: String = ""
    abstract fun call(): Request

    class GetParameter<T : Any>(
        override val url: String,
        override val parameters: Map<String, Any> = mapOf(),
        override val header: Map<String, Any> = mapOf(),
        override val adapter: JsonAdapter<T>? = null
    ) : ApiParameter<T>(url, parameters, header, null, adapter) {
        override var method = "GET"
        override fun call() = url.httpGet(parameters.map { it.key to it.value })
    }

    class PostParameter<T : Any>(
        override val url: String,
        override val parameters: Map<String, Any> = mapOf(),
        override val header: Map<String, Any> = mapOf(),
        override val body: Any? = null,
        override val adapter: JsonAdapter<T>? = null
    ) : ApiParameter<T>(url, parameters, header, body, adapter) {
        override var method = "POST"
        override fun call() = url.httpPost(parameters.map { it.key to it.value })
    }
}
