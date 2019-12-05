package tech.takenoko.cleanarchitecturex.extension

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

val OBJECT_MAPPER: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

/**
 * moshi adapter
 */
inline fun <reified T : Any> planeAdapter() = planeAdapter(T::class.java)
fun <T : Any> planeAdapter(clazz: Class<T>) = object : ResponseDeserializable<T> {
    override fun deserialize(content: String): T? = OBJECT_MAPPER
    .adapter(clazz)
    .fromJson(content)
}

/**
 * moshi list adapter
 */
inline fun <reified T : Any> listAdapter() = listAdapter(T::class.java)
fun <T : Any> listAdapter(clazz: Class<T>) = object : ResponseDeserializable<List<T>> {
    override fun deserialize(content: String): List<T>? {
        val type = Types.newParameterizedType(List::class.java, clazz)
        val listAdapter = OBJECT_MAPPER.adapter<List<T>>(type)
        return listAdapter.fromJson(content)
    }
}
