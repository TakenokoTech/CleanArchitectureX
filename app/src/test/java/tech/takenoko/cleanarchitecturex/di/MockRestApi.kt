package tech.takenoko.cleanarchitecturex.di

import kotlin.reflect.KClass
import tech.takenoko.cleanarchitecturex.entities.ApiParameter
import tech.takenoko.cleanarchitecturex.entities.ApiResult
import tech.takenoko.cleanarchitecturex.extension.OBJECT_MAPPER

@Suppress("UNCHECKED_CAST")
class MockRestApi : AppRestApi {

    override suspend fun <T : Any> execute(param: ApiParameter<T>, clazz: KClass<T>): ApiResult<T> {
        response.forEach { (p, result) ->
            val paramBody = OBJECT_MAPPER.adapter(Any::class.java).toJson(param.body)
            val pBody = OBJECT_MAPPER.adapter(Any::class.java).toJson(p.body)
            println("$paramBody ?= $pBody ${paramBody == pBody}")
            if (
                param.url == p.url &&
                param.parameters == p.parameters &&
                param.header == p.header &&
                paramBody == pBody
            )
                return result as ApiResult<T>
        }
        println("responsePattern: $response")
        println("url: ${param.url}")
        println("parameters: ${param.parameters}")
        println("header: ${param.header}")
        println("body: ${param.body}")
        throw Exception("No test pattern.")
    }

    companion object {
        var response: MutableMap<ApiParameter<*>, ApiResult<*>> = mutableMapOf()
    }
}
