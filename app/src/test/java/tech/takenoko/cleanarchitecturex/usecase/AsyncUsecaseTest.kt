package tech.takenoko.cleanarchitecturex.usecase

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ServiceLifecycleDispatcher
import java.lang.Exception
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.*
import org.junit.rules.TestRule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.mockito.Mockito
import tech.takenoko.cleanarchitecturex.entities.UsecaseResult
import tech.takenoko.cleanarchitecturex.entities.isFinished
import tech.takenoko.cleanarchitecturex.entities.isLoading
import tech.takenoko.cleanarchitecturex.extension.*

@ExperimentalCoroutinesApi
class AsyncUsecaseTest : AutoCloseKoinTest(), LifecycleOwner {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private val mockObserver = mockObserver<UsecaseResult<String>>()

    @Before
    fun before() {
        startKoin { modules(mockModule) }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun callAsync_success() {
        val asyncUsecase = object : AsyncUsecase<String, String>(context, testScope) {
            override suspend fun callAsync(param: String): Deferred<String> = testScope.async(Dispatchers.IO) {
                Thread.sleep(100)
                return@async ""
            }
        }
        asyncUsecase.source.observeForever(mockObserver)
        asyncUsecase.execute("")
        checkedObserver(mockObserver) {
            Assert.assertEquals(it.toState(), PENDING)
            Assert.assertEquals(it.isFinished(), false)
            Assert.assertEquals(it.isLoading(), true)
        }
        Thread.sleep(200)
        checkedObserver(mockObserver) {
            Assert.assertEquals(it.toState(), RESOLVED)
            Assert.assertEquals(it.isFinished(), true)
            Assert.assertEquals(it.isLoading(), false)
        }
    }

    @Test
    fun callAsync_failed() {
        val asyncUsecase = object : AsyncUsecase<String, String>(context, testScope) {
            override suspend fun callAsync(param: String): Deferred<String> = testScope.async(Dispatchers.IO) {
                Thread.sleep(100)
                throw Exception()
            }
        }
        asyncUsecase.source.observeForever(mockObserver)
        asyncUsecase.execute("")
        checkedObserver(mockObserver) {
            Assert.assertEquals(it.toState(), PENDING)
            Assert.assertEquals(it.isFinished(), false)
            Assert.assertEquals(it.isLoading(), true)
        }
        Thread.sleep(200)
        checkedObserver(mockObserver) {
            Assert.assertEquals(it.toState(), REJECTED)
            Assert.assertEquals(it.isFinished(), true)
            Assert.assertEquals(it.isLoading(), false)
        }
    }

    private val mockModule: Module = module {}

    private val testScope = TestCoroutineScope()
    private val context: Context = Mockito.mock(Context::class.java)
    override fun getLifecycle(): Lifecycle = ServiceLifecycleDispatcher(this).lifecycle
}
