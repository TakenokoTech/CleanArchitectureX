package tech.takenoko.cleanarchitecturex.usecase

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ServiceLifecycleDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.*
import org.junit.rules.TestRule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.inject
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.mockito.Mockito.*
import tech.takenoko.cleanarchitecturex.entities.UsecaseResult
import tech.takenoko.cleanarchitecturex.extension.*

@ExperimentalCoroutinesApi
class BackgroundUsecaseTest : AutoCloseKoinTest(), LifecycleOwner {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private val mockObserver = mockObserver<UsecaseResult<Boolean>>()

    @Before
    fun before() {
        startKoin { modules(mockModule) }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun callAsync_success1() {
        val backgroundUsecase by inject<BackgroundUsecase>()
        backgroundUsecase.source.observeForever(mockObserver)
        backgroundUsecase.execute(BackgroundUsecase.BackgroundUsecaseParam(count = 1, sleepTime = 10))
        checkedObserver(mockObserver) {
            Assert.assertEquals(it.toState(), PENDING)
        }
        Thread.sleep(1000)
        checkedObserver(mockObserver) {
            Assert.assertEquals(it.toState(), RESOLVED)
        }
    }

    private val mockModule: Module = module {
        factory { BackgroundUsecase(context, testScope) }
    }

    private val testScope = TestCoroutineScope()
    private val context: Context = mock(Context::class.java)
    override fun getLifecycle(): Lifecycle = ServiceLifecycleDispatcher(this).lifecycle
}
