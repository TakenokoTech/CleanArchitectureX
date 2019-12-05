package tech.takenoko.cleanarchitecturex.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ServiceLifecycleDispatcher
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.*
import org.junit.rules.TestRule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.inject
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.mockito.Mockito
import tech.takenoko.cleanarchitecturex.entities.UsecaseResult
import tech.takenoko.cleanarchitecturex.extention.checkedObserver
import tech.takenoko.cleanarchitecturex.extention.mockObserver
import tech.takenoko.cleanarchitecturex.usecase.LoadUserUsecase

@ExperimentalCoroutinesApi
class TopViewModelTest : AutoCloseKoinTest(), LifecycleOwner {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private val mockObserverString = mockObserver<String>()
    private val mockObserverListString = mockObserver<List<String>>()

    @Before
    fun before() {
        startKoin { modules(mockModule) }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun pending() {
        val topViewModel by inject<TopViewModel>()
        topViewModel.text1.observeForever(mockObserverString)
        topViewModel.list1.observeForever(mockObserverListString)

        // Before
        Assert.assertEquals(topViewModel.text1.value, null)
        Assert.assertEquals(topViewModel.list1.value, null)

        // Execute
        loadUserUsecaseValue = UsecaseResult.Pending()
        topViewModel.load()

        // After
        checkedObserver(mockObserverString) { Assert.assertEquals(it, "loading...") }
    }

    @Test
    fun success() {
        val topViewModel by inject<TopViewModel>()
        topViewModel.text1.observeForever(mockObserverString)
        topViewModel.list1.observeForever(mockObserverListString)

        // Before
        Assert.assertEquals(topViewModel.text1.value, null)
        Assert.assertEquals(topViewModel.list1.value, null)

        // Execute
        loadUserUsecaseValue = UsecaseResult.Resolved(listOf("test"))
        topViewModel.load()

        // After
        checkedObserver(mockObserverString) { Assert.assertEquals(it, "success!") }
        checkedObserver(mockObserverListString) { Assert.assertEquals(it, listOf("test")) }
    }

    @Test
    fun failed() {
        val topViewModel by inject<TopViewModel>()
        topViewModel.text1.observeForever(mockObserverString)
        topViewModel.list1.observeForever(mockObserverListString)

        // Before
        Assert.assertEquals(topViewModel.text1.value, null)
        Assert.assertEquals(topViewModel.list1.value, null)

        // Execute
        loadUserUsecaseValue = UsecaseResult.Rejected(Throwable("failed"))
        topViewModel.load()

        // After
        checkedObserver(mockObserverString) { Assert.assertEquals(it, "failed") }
    }

    private val mockModule: Module = module {
        factory { TopViewModel() }
        factory { MockLoadUserUsecase(context, mock { }) as LoadUserUsecase }
    }

    lateinit var loadUserUsecaseValue: UsecaseResult<List<String>>
    private inner class MockLoadUserUsecase(context: Context, scope: CoroutineScope) : LoadUserUsecase(context, scope) {
        override fun execute(param: Unit) {
            result.postValue(loadUserUsecaseValue)
        }
    }

    private val context: Context = Mockito.mock(Context::class.java)
    override fun getLifecycle(): Lifecycle = ServiceLifecycleDispatcher(this).lifecycle
}
