package tech.takenoko.cleanarchitecturex.usecase

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
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
import tech.takenoko.cleanarchitecturex.repository.UserRepository
import tech.takenoko.cleanarchitecturex.repository.local.UserLocalDataSource
import tech.takenoko.cleanarchitecturex.utils.AppLog

@ExperimentalCoroutinesApi
class RegisterUserUsecaseTest : AutoCloseKoinTest(), LifecycleOwner {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private val mockObserver = mockObserver<UsecaseResult<Unit>>()

    @Before
    fun before() {
        startKoin { modules(mockModule) }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test(timeout = 2000)
    fun callAsync_success() {
        val loadUserUsecase by inject<RegisterUserUsecase>()
        loadUserUsecase.source.observeForever(mockObserver)
        loadUserUsecase.execute(Unit)
        checkedObserver(mockObserver) {
            Assert.assertEquals(it.toState(), PENDING)
        }
        Thread.sleep(1500)
        checkedObserver(mockObserver) {
            val result = it as? UsecaseResult.Resolved
            Assert.assertEquals(it.toState(), RESOLVED)
            Assert.assertEquals(result?.value, Unit)
        }
    }

    private val mockModule: Module = module {
        factory { RegisterUserUsecase(context, testScope) }
        factory { MockUserRepository() as UserRepository }
    }

    var allUser = listOf<UserLocalDataSource.User>()
    inner class MockUserRepository : UserRepository {
        override suspend fun getAllUser(): List<UserLocalDataSource.User> = allUser
        override suspend fun addUser(name: String) {
            AppLog.debug("MockUserRepository", name)
        }
        override fun getAllToLive(): LiveData<List<UserLocalDataSource.User>> = mock {}
    }

    private val testScope = TestCoroutineScope()
    private val context: Context = mock(Context::class.java)
    override fun getLifecycle(): Lifecycle = ServiceLifecycleDispatcher(this).lifecycle
}
