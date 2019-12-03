package tech.takenoko.cleanarchitecturex.view

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.CoroutineScope
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
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import tech.takenoko.cleanarchitecturex.usecase.LoadUserUsecase
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

        val recyclerViewAdapter = RecyclerViewAdapter()
        recyclerViewAdapter.setItem(listOf("test"))
        Assert.assertEquals(recyclerViewAdapter.itemCount, 1)

        val bindingHolder = RecyclerViewAdapter.BindingHolder(mock {
            `when`(this.mock.root).thenReturn(mock { })
            `when`(this.mock.text).thenReturn("test")
        })
        recyclerViewAdapter.onBindViewHolder(bindingHolder, 0)
        Assert.assertEquals(bindingHolder.binding.text, "test")
    }

    private val mockModule: Module = module {
        factory { mock<TopViewModel> {} }
        factory { MockLoadUserUsecase(ApplicationProvider.getApplicationContext(), mock { }) as LoadUserUsecase }
    }

    private inner class MockLoadUserUsecase(context: Context, scope: CoroutineScope) : LoadUserUsecase(context, scope) {
        override fun execute(param: Unit) {}
    }

    inner class TestActivity : Activity()
}
