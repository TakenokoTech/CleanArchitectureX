package tech.takenoko.cleanarchitecturex

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import tech.takenoko.cleanarchitecturex.di.*

class App: Application() {

    override fun onCreate(){
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(diModules)
        }
    }
}