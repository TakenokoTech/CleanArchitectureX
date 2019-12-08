package tech.takenoko.cleanarchitecturex.usecase

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ServiceLifecycleDispatcher
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.*
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
import tech.takenoko.cleanarchitecturex.repository.MockUserRepository
import tech.takenoko.cleanarchitecturex.repository.UserRepository
import tech.takenoko.cleanarchitecturex.repository.local.UserLocalDataSource

@ExperimentalCoroutinesApi
class LoadUserUsecaseTest : AutoCloseKoinTest(), LifecycleOwner {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private val mockObserver = mockObserver<UsecaseResult<List<String>>>()

    @Before
    fun before() {
        startKoin { modules(mockModule) }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test(timeout = 2000)
    fun callAsync_success_not_null() {
        MockUserRepository.allUser = listOf(UserLocalDataSource.User("testName", "testDisplay"))
        val loadUserUsecase by inject<LoadUserUsecase>()
        loadUserUsecase.source.observeForever(mockObserver)
        loadUserUsecase.execute(Unit)
        checkedObserver(mockObserver) {
            Assert.assertEquals(it.toState(), PENDING)
        }
        Thread.sleep(1500)
        checkedObserver(mockObserver) {
            val result = it as? UsecaseResult.Resolved
            Assert.assertEquals(it.toState(), RESOLVED)
            Assert.assertEquals(result?.value, listOf("0: testDisplay"))
        }
    }

    private val mockModule: Module = module {
        factory { LoadUserUsecase(context, testScope) }
        factory { MockUserRepository() as UserRepository }
    }

    private val testScope = TestCoroutineScope()
    private val context: Context = mock(Context::class.java)
    override fun getLifecycle(): Lifecycle = ServiceLifecycleDispatcher(this).lifecycle
}
