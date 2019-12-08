package tech.takenoko.cleanarchitecturex.view

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.CoroutineScope
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import tech.takenoko.cleanarchitecturex.MainActivity
import tech.takenoko.cleanarchitecturex.R
import tech.takenoko.cleanarchitecturex.usecase.LoadUserUsecase
import tech.takenoko.cleanarchitecturex.usecase.RegisterUserUsecase
import tech.takenoko.cleanarchitecturex.viewmodel.TopViewModel

@Config(sdk = [Build.VERSION_CODES.P])
@RunWith(RobolectricTestRunner::class)
class TopFragmentTest {

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
        val controller = Robolectric.buildActivity(MainActivity::class.java).setup()
        controller.pause()
        Navigation.findNavController(controller.get(), R.id.nav_host_fragment).navigateUp()
        controller.stop()
    }

    private val mockModule: Module = module {
        factory { TopViewModel() }
        factory { MockLoadUserUsecase(ApplicationProvider.getApplicationContext(), mock { }) as LoadUserUsecase }
        factory { MockRegisterUserUsecase(ApplicationProvider.getApplicationContext(), mock { }) as RegisterUserUsecase }
    }

    private inner class MockLoadUserUsecase(context: Context, scope: CoroutineScope) : LoadUserUsecase(context, scope) {
        override fun execute(param: Unit) {}
    }

    private inner class MockRegisterUserUsecase(context: Context, scope: CoroutineScope) : RegisterUserUsecase(context, scope) {
        override fun execute(param: Param) {}
    }

    class TestActivity : Activity()
}
