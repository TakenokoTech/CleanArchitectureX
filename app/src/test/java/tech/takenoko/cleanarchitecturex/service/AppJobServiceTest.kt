package tech.takenoko.cleanarchitecturex.service

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import tech.takenoko.cleanarchitecturex.entities.UsecaseResult
import tech.takenoko.cleanarchitecturex.usecase.BackgroundUsecase
import tech.takenoko.cleanarchitecturex.viewmodel.TopViewModel

@Config(sdk = [Build.VERSION_CODES.P])
@RunWith(RobolectricTestRunner::class)
class AppJobServiceTest : AutoCloseKoinTest() {

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
        val appJobService = AppJobService()
        appJobService.onStartJob(mock {})
        appJobService.observer(mock {}).onChanged(UsecaseResult.Pending())
        appJobService.observer(mock {}).onChanged(UsecaseResult.Resolved(true))
        appJobService.onStopJob(mock {})
    }

    private val mockModule: Module = module {
        factory { TopViewModel() }
        factory { MockBackgroundUsecase(ApplicationProvider.getApplicationContext(), mock { }) as BackgroundUsecase }
    }

    private inner class MockBackgroundUsecase(context: Context, scope: CoroutineScope) : BackgroundUsecase(context, scope) {
        override fun execute(param: Unit) {
            result.postValue(UsecaseResult.Resolved(true))
        }
    }

    class TestActivity : Activity()
}
