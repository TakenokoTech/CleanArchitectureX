package tech.takenoko.cleanarchitecturex.extention

import androidx.annotation.MainThread
import androidx.lifecycle.Observer
import org.koin.test.AutoCloseKoinTest
import org.mockito.ArgumentCaptor
import org.mockito.Mockito

@MainThread
inline fun <reified T> AutoCloseKoinTest.mockObserver(): Observer<T> = Mockito.mock((Observer<T> {})::class.java, Mockito.RETURNS_DEEP_STUBS)

@MainThread
inline fun <reified T> AutoCloseKoinTest.checkedObserver(mockObserver: Observer<T>, callback: (T) -> Unit) = run {
    ArgumentCaptor.forClass(T::class.java).run {
        val mode = Mockito.atLeast(1)
        Mockito.verify(mockObserver, mode).onChanged(capture())
        callback(value)
    }
}
