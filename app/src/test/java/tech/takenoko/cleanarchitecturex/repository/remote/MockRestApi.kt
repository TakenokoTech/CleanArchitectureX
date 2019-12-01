package tech.takenoko.cleanarchitecturex.repository.remote

import kotlin.reflect.KClass
import tech.takenoko.cleanarchitecturex.di.AppRestApi
import tech.takenoko.cleanarchitecturex.entities.ApiParameter
import tech.takenoko.cleanarchitecturex.entities.ApiResult

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
        println("url: ${param.url}")
        println("parameters: ${param.parameters}")
        println("header: ${param.header}")
        println("body: ${param.body}")
        throw Exception()
    }

    companion object {
        var response: MutableMap<ApiParameter<*>, ApiResult<*>> = mutableMapOf()
    }
}
