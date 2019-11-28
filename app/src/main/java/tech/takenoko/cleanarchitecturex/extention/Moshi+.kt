package tech.takenoko.cleanarchitecturex.extention

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

val OBJECT_MAPPER : Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

/**
 * moshi list adapter
 */
inline fun <reified T> listAdapter(): JsonAdapter<List<T>> {
    val type = Types.newParameterizedType(List::class.java, T::class.java)
    return OBJECT_MAPPER.adapter(type)
}

/**
 * moshi adapter
 */
inline fun <reified T> planeAdapter(): JsonAdapter<T> {
    return OBJECT_MAPPER.adapter(T::class.java)
}
