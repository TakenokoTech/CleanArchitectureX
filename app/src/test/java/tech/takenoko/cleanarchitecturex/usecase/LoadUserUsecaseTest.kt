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
import tech.takenoko.cleanarchitecturex.extention.checkedObserver
import tech.takenoko.cleanarchitecturex.extention.mockObserver
import tech.takenoko.cleanarchitecturex.repository.UserRepository
import tech.takenoko.cleanarchitecturex.repository.local.UserLocalDataSource
import tech.takenoko.cleanarchitecturex.utils.AppLog

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

    @Test
    fun callAsync_success1() {
        val loadUserUsecase by inject<LoadUserUsecase>()
        loadUserUsecase.source.observeForever(mockObserver)
        loadUserUsecase.execute(Unit)
        checkedObserver(mockObserver) {
            Assert.assertTrue(it is UsecaseResult.Pending)
        }
        Thread.sleep(1500)
        checkedObserver(mockObserver) {
            val result = it as? UsecaseResult.Resolved
            Assert.assertTrue(it is UsecaseResult.Resolved)
            Assert.assertEquals(result?.value, listOf("testName"))
        }
    }

    private val mockModule: Module = module {
        factory { LoadUserUsecase(context, testScope) }
        factory { MockUserRepository() as UserRepository }
    }

    inner class MockUserRepository : UserRepository {
        override suspend fun getAllUser(): List<UserLocalDataSource.User> = listOf(
            UserLocalDataSource.User("testUid", "testName")
        )
        override suspend fun addUser(name: String) {
            AppLog.debug("MockUserRepository", name)
        }
    }

    private val testScope = TestCoroutineScope()
    private val context: Context = mock(Context::class.java)
    override fun getLifecycle(): Lifecycle = ServiceLifecycleDispatcher(this).lifecycle
}
