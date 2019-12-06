package tech.takenoko.cleanarchitecturex.entities

import com.github.kittinunf.fuel.core.Deserializable
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost

typealias Get<T> = ApiParameter.GetParameter<T>
typealias Post<T> = ApiParameter.PostParameter<T>

sealed class ApiParameter<T : Any> {
    abstract val url: String
    abstract val parameters: Map<String, Any>
    abstract val header: Map<String, Any>
    abstract val body: Any?
    abstract val adapter: Deserializable<T>?
    abstract val method: String
    abstract val call: () -> Request

    open class GetParameter<T : Any>(
        override val url: String,
        override val parameters: Map<String, Any> = mapOf(),
        override val header: Map<String, Any> = mapOf(),
        override val adapter: Deserializable<T>? = null
    ) : ApiParameter<T>() {
        override val method = "GET"
        override val body: Any? = null
        override val call = { url.httpGet(parameters.map { it.key to it.value }) }
    }

    open class PostParameter<T : Any>(
        override val url: String,
        override val parameters: Map<String, Any> = mapOf(),
        override val header: Map<String, Any> = mapOf(),
        override val body: Any? = null,
        override val adapter: Deserializable<T>? = null
    ) : ApiParameter<T>() {
        override val method = "POST"
        override val call = { url.httpPost(parameters.map { it.key to it.value }) }
    }
}
