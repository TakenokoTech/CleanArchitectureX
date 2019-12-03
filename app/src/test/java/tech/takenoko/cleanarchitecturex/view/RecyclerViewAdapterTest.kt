package tech.takenoko.cleanarchitecturex.view

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import com.nhaarman.mockitokotlin2.mock
import kotlinx.android.synthetic.main.fragment_top.*
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import org.mockito.Mockito.`when`
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import tech.takenoko.cleanarchitecturex.viewmodel.TopViewModel

@Config(sdk = [Build.VERSION_CODES.P])
@RunWith(RobolectricTestRunner::class)
class RecyclerViewAdapterTest {

    @Before
    fun before() {
        stopKoin()
        startKoin {
            androidContext(ApplicationProvider.getApplicationContext())
            modules(mockModule)
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun onCreate_success() {
        val controller = Robolectric.buildActivity(TestActivity::class.java).setup()
        controller.pause()
        val context = controller.get() as Context
        val bindingHolder = RecyclerViewAdapter.BindingHolder(mock {
            `when`(this.mock.root).thenReturn(mock { })
            `when`(this.mock.text).thenReturn("test")
        })
        controller.get().testView = RecyclerView(context).apply {
            adapter = RecyclerViewAdapter()
            (adapter as RecyclerViewAdapter).setItem(listOf("test"))
            (adapter as RecyclerViewAdapter).onCreateViewHolder(MockViewGroup(context), 0)
            (adapter as RecyclerViewAdapter).onBindViewHolder(bindingHolder, 0)
            (adapter as RecyclerViewAdapter).notifyDataSetChanged()
        }
        controller.stop()
        Assert.assertEquals(bindingHolder.binding.text, "test")
    }

    private val mockModule: Module = module {
        factory { mock<TopViewModel> {} }
    }

    class MockViewGroup(context: Context) : ViewGroup(context) {
        override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {}
    }

    class TestActivity : Activity() {
        lateinit var testView: View
    }
}
