package tech.takenoko.cleanarchitecturex

import android.content.Context
import android.os.Build
import android.view.View
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
import org.robolectric.Robolectric.buildActivity
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import tech.takenoko.cleanarchitecturex.usecase.LoadUserUsecase
import tech.takenoko.cleanarchitecturex.viewmodel.TopViewModel

@Config(sdk = [Build.VERSION_CODES.P])
@RunWith(RobolectricTestRunner::class)
class MainActivityTest {

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
    fun mainActivity_onCreate_success() {
        val controller = buildActivity(MainActivity::class.java).setup()
        controller.pause().stop()

        val fragment = controller.get().findViewById<View>(R.id.nav_host_fragment)
        Assert.assertEquals(fragment.id, R.id.nav_host_fragment)
    }

    private val mockModule: Module = module {
        factory { TopViewModel() }
        factory { MockLoadUserUsecase(ApplicationProvider.getApplicationContext(), mock { }) as LoadUserUsecase }
    }

    inner class MockLoadUserUsecase(context: Context, scope: CoroutineScope) : LoadUserUsecase(context, scope) {
        override fun execute(param: Unit) {}
    }
}
