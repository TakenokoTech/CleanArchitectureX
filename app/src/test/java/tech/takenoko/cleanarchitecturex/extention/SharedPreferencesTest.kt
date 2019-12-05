package tech.takenoko.cleanarchitecturex.extention

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class SharedPreferencesTest {

    private val preferences: SharedPreferences = MockSharedPreferences()

    private var booleanPref: Boolean by preferences.boolean()
    private var floatPref: Float by preferences.float()
    private var intPref: Int by preferences.int()
    private var longPref: Long by preferences.long()
    private var stringPref: String by preferences.string()
    private var jsonPref: Sample by preferences.json(
        OBJECT_MAPPER.adapter(Sample::class.java),
        lazy { Sample("") })

    @Before
    fun before() {
        valBoolean = null
        valInt = null
        valFloat = null
        valLong = null
        valString = null
    }

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

    private var booleanPrefDef: Boolean by preferences.boolean(true, "booleanPrefDef")
    private var floatPrefDef: Float by preferences.float(2.0F, "floatPrefDef")
    private var intPrefDef: Int by preferences.int(2, "intPrefDef")
    private var longPrefDef: Long by preferences.long(2, "longPrefDef")
    private var stringPrefDef: String by preferences.string("default", "stringPrefDef")
    private var jsonPrefDef: Sample by preferences.json(
        OBJECT_MAPPER.adapter(Sample::class.java),
        lazy { Sample("") },
        "jsonPrefDef"
    )

    @Test
    fun preferencesBooleanDef_success() {
        Assert.assertEquals(booleanPrefDef, true)
        booleanPrefDef = false
        Assert.assertEquals(booleanPrefDef, false)
    }

    @Test
    fun preferencesFloatDef_success() {
        Assert.assertEquals(floatPrefDef, 2.0F)
        floatPrefDef = 1.0F
        Assert.assertEquals(floatPrefDef, 1.0F)
    }

    @Test
    fun preferencesIntDef_success() {
        Assert.assertEquals(intPrefDef, 2)
        intPrefDef = 1
        Assert.assertEquals(intPrefDef, 1)
    }

    @Test
    fun preferencesLongDef_success() {
        Assert.assertEquals(longPrefDef, 2)
        longPrefDef = 1
        Assert.assertEquals(longPrefDef, 1)
    }

    @Test
    fun preferencesStringDef_success() {
        Assert.assertEquals(stringPrefDef, "default")
        stringPrefDef = "test"
        Assert.assertEquals(stringPrefDef, "test")
    }

    @Test
    fun preferencesJsonDef_success() {
        Assert.assertEquals(jsonPrefDef, Sample(""))
        jsonPrefDef = Sample("test")
        Assert.assertEquals(jsonPrefDef, Sample("test"))
    }

    @Test
    fun delegate() {
        val read1 = preferences.delegate<String>("def", "key", { _, _ -> "val" }, { _, _ -> MockSharedPreferences().edit() })
        val v1 = read1.getValue("", mock { })
        Assert.assertEquals(v1, "val")
        read1.setValue("", mock {}, "value")

//        val read2 = preferences.delegate("def", null, { _, _ -> null }, { _, _ -> MockSharedPreferences().edit() })
//        val v2 = read2.getValue("", mock { })
//        Assert.assertEquals(v2, "def")
//        read2.setValue("", mock {}, "value")
    }

    data class Sample(val text: String)

    var valBoolean: Boolean? = null
    var valInt: Int? = null
    var valFloat: Float? = null
    var valLong: Long? = null
    var valString: String? = null

    inner class MockSharedPreferences : SharedPreferences {
        override fun contains(key: String): Boolean = false
        override fun edit(): Editor = MockEditor()
        override fun getAll(): Map<String, *> = mapOf<String, String>()
        override fun getBoolean(key: String, defValue: Boolean): Boolean = if (valBoolean != null) valBoolean!! else defValue
        override fun getFloat(key: String, defValue: Float): Float = valFloat ?: defValue
        override fun getInt(key: String, defValue: Int): Int = valInt ?: defValue
        override fun getLong(key: String, defValue: Long): Long = valLong ?: defValue
        override fun getString(key: String, defValue: String?): String? = valString
        override fun getStringSet(arg0: String, arg1: Set<String>?): Set<String>? = throw UnsupportedOperationException()
        override fun registerOnSharedPreferenceChangeListener(listener: OnSharedPreferenceChangeListener) = throw UnsupportedOperationException()
        override fun unregisterOnSharedPreferenceChangeListener(listener: OnSharedPreferenceChangeListener) = throw UnsupportedOperationException()
        inner class MockEditor : Editor {
            override fun apply() {}
            override fun clear(): Editor = this
            override fun commit(): Boolean = true
            override fun putBoolean(key: String, value: Boolean): Editor = this.also { this@SharedPreferencesTest.valBoolean = value }
            override fun putFloat(key: String, value: Float): Editor = this.also { this@SharedPreferencesTest.valFloat = value }
            override fun putInt(key: String, value: Int) = this.also { this@SharedPreferencesTest.valInt = value }
            override fun putLong(key: String, value: Long): Editor = this.also { this@SharedPreferencesTest.valLong = value }
            override fun putString(key: String, value: String?): Editor = this.also { this@SharedPreferencesTest.valString = value }
            override fun putStringSet(arg0: String, arg1: Set<String>?): Editor = throw UnsupportedOperationException()
            override fun remove(key: String): Editor = throw UnsupportedOperationException()
        }
    }
}
