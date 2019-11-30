package tech.takenoko.cleanarchitecturex.extention

import android.content.SharedPreferences
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Boolean Read Write Delegate
 */
fun SharedPreferences.boolean(
    defaultValue: Boolean = false,
    key: String? = null
): ReadWriteProperty<Any, Boolean> =
    delegate(defaultValue, key, SharedPreferences::getBoolean, SharedPreferences.Editor::putBoolean)

/**
 * Float Read Write Delegate
 */
fun SharedPreferences.float(
    defaultValue: Float = 0f,
    key: String? = null
): ReadWriteProperty<Any, Float> =
    delegate(defaultValue, key, SharedPreferences::getFloat, SharedPreferences.Editor::putFloat)

/**
 * Int Read Write Delegate
 */
fun SharedPreferences.int(
    defaultValue: Int = 0,
    key: String? = null
): ReadWriteProperty<Any, Int> =
    delegate(defaultValue, key, SharedPreferences::getInt, SharedPreferences.Editor::putInt)

/**
 * Long Read Write Delegate
 */
fun SharedPreferences.long(
    defaultValue: Long = 0,
    key: String? = null
): ReadWriteProperty<Any, Long> =
    delegate(defaultValue, key, SharedPreferences::getLong, SharedPreferences.Editor::putLong)

/**
 * String Read Write Delegate
 */
fun SharedPreferences.string(
    defaultValue: String = "",
    key: String? = null
): ReadWriteProperty<Any, String> =
    delegate(defaultValue, key, SharedPreferences::getString, SharedPreferences.Editor::putString)

/**
 * Enum Read Write Delegate
 */
fun <T : Enum<T>> SharedPreferences.enum(
    valueOf: (String) -> T,
    defaultValue: Lazy<T>,
    key: String? = null
) =
    object : ReadWriteProperty<Any, T> {
        override fun getValue(thisRef: Any, property: KProperty<*>): T {
            return getString(key ?: property.name, null)?.let { valueOf(it) } ?: defaultValue.value
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
            edit().putString(key ?: property.name, value.name).apply()
        }
    }

/**
 * Moshi Json String Read Write Delegate
 */
inline fun <reified T : Any> SharedPreferences.json(
    moshi: Moshi,
    defaultValue: Lazy<T>,
    key: String? = null
): ReadWriteProperty<Any, T> =
    json(moshi.adapter(T::class.java), defaultValue, key)

/**
 * Moshi Json String Read Write Delegate
 */
fun <T : Any> SharedPreferences.json(
    adapter: JsonAdapter<T>,
    defaultValue: Lazy<T>,
    key: String? = null
) =
    object : ReadWriteProperty<Any, T> {
        override fun getValue(thisRef: Any, property: KProperty<*>): T {
            return getString(key ?: property.name, null)?.let(adapter::fromJson)
                ?: defaultValue.value
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
            edit().putString(key ?: property.name, adapter.toJson(value)).apply()
        }
    }

private typealias Getter<T> = SharedPreferences.(key: String, defaultValue: T) -> T?
private typealias Setter<T> = SharedPreferences.Editor.(key: String, value: T) -> SharedPreferences.Editor

private inline fun <T : Any> SharedPreferences.delegate(
    defaultValue: T,
    key: String?,
    crossinline getter: Getter<T>,
    crossinline setter: Setter<T>
) = object : ReadWriteProperty<Any, T> {
    override fun getValue(thisRef: Any, property: KProperty<*>) =
        getter(key ?: property.name, defaultValue) ?: defaultValue

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) =
        edit().setter(key ?: property.name, value).apply()
}
