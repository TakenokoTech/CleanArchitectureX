package tech.takenoko.cleanarchitecturex.di

import kotlin.reflect.KClass
import tech.takenoko.cleanarchitecturex.entities.ApiParameter
import tech.takenoko.cleanarchitecturex.entities.ApiResult

@Suppress("UNCHECKED_CAST")
class MockRestApi : AppRestApi {

    override suspend fun <T : Any> execute(param: ApiParameter<T>, clazz: KClass<T>): ApiResult<T> {
        response.forEach { (p, result) ->
            if (
                param.url == p.url &&
                param.parameters == p.parameters &&
                param.header == p.header &&
                param.body == p.body
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
