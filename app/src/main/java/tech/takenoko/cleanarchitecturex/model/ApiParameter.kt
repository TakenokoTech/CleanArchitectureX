package tech.takenoko.cleanarchitecturex.model

import com.squareup.moshi.JsonAdapter

sealed class ApiParameter {

    class GetParameter<T : Any>(
        val url: String,
        val parameters: Map<String, Any> = mapOf(),
        val header: Map<String, Any> = mapOf(),
        val adapter: JsonAdapter<T>? = null
    ) : ApiParameter()

    class PostParameter<T : Any>(
        val url: String,
        val parameters: Map<String, Any> = mapOf(),
        val header: Map<String, Any> = mapOf(),
        val body: Any? = null,
        val adapter: JsonAdapter<T>? = null
    ) : ApiParameter()
}