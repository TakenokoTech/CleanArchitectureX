package tech.takenoko.cleanarchitecturex.extention

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import org.junit.Assert
import org.junit.Test

class SharedPreferencesTest {

    private val preferences: SharedPreferences = MockSharedPreferences()
    enum class NumberEnum(v: String) { ONE("1"), TWO("2") }

    var booleanPref: Boolean by preferences.boolean()
    var floatPref: Float by preferences.float()
    var intPref: Int by preferences.int()
    var longPref: Long by preferences.long()
    var stringPref: String by preferences.string()
    var jsonPref: Sample by preferences.json(OBJECT_MAPPER.adapter(Sample::class.java), lazy { Sample("") })

    @Test
    fun preferencesBoolean_success() {
        Assert.assertEquals(booleanPref, false)
        booleanPref = true
        Assert.assertEquals(booleanPref, true)
    }

    @Test
    fun preferencesFloat_success() {
        Assert.assertEquals(floatPref, 0.0F)
        floatPref = 1.0F
        Assert.assertEquals(floatPref, 1.0F)
    }

    @Test
    fun preferencesInt_success() {
        Assert.assertEquals(intPref, 0)
        intPref = 1
        Assert.assertEquals(intPref, 1)
    }

    @Test
    fun preferencesLong_success() {
        Assert.assertEquals(longPref, 0)
        longPref = 1
        Assert.assertEquals(longPref, 1)
    }

    @Test
    fun preferencesString_success() {
        Assert.assertEquals(stringPref, "")
        stringPref = "test"
        Assert.assertEquals(stringPref, "test")
    }

    @Test
    fun preferencesJson_success() {
        Assert.assertEquals(jsonPref, Sample(""))
        jsonPref = Sample("test")
        Assert.assertEquals(jsonPref, Sample("test"))
    }

    data class Sample(val text: String)
}

class MockSharedPreferences : SharedPreferences {
    var valBoolean = false
    var valInt = 0
    var valFloat = 0F
    var valLong = 0L
    var valString: String? = null

    override fun contains(key: String): Boolean = false
    override fun edit(): Editor = MockEditor()
    override fun getAll(): Map<String, *> = mapOf<String, String>()
    override fun getBoolean(key: String, defValue: Boolean): Boolean = valBoolean
    override fun getFloat(key: String, defValue: Float): Float = valFloat
    override fun getInt(key: String, defValue: Int): Int = valInt
    override fun getLong(key: String, defValue: Long): Long = valLong
    override fun getString(key: String, defValue: String?): String? = valString

    override fun getStringSet(arg0: String, arg1: Set<String>?): Set<String>? {
        throw UnsupportedOperationException()
    }

    override fun registerOnSharedPreferenceChangeListener(listener: OnSharedPreferenceChangeListener) {
        throw UnsupportedOperationException()
    }

    override fun unregisterOnSharedPreferenceChangeListener(listener: OnSharedPreferenceChangeListener) {
        throw UnsupportedOperationException()
    }

    inner class MockEditor : Editor {
        override fun apply() {}
        override fun clear(): Editor = this
        override fun commit(): Boolean = true
        override fun putBoolean(key: String, value: Boolean): Editor {
            this@MockSharedPreferences.valBoolean = value
            return this
        }
        override fun putFloat(key: String, value: Float): Editor {
            this@MockSharedPreferences.valFloat = value
            return this
        }
        override fun putInt(key: String, value: Int): Editor {
            this@MockSharedPreferences.valInt = value
            return this
        }
        override fun putLong(key: String, value: Long): Editor {
            this@MockSharedPreferences.valLong = value
            return this
        }
        override fun putString(key: String, value: String?): Editor {
            this@MockSharedPreferences.valString = value
            return this
        }
        override fun putStringSet(arg0: String, arg1: Set<String>?): Editor {
            throw UnsupportedOperationException()
        }
        override fun remove(key: String): Editor {
            throw UnsupportedOperationException()
        }
    }
}
