package tech.takenoko.cleanarchitecturex

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import tech.takenoko.cleanarchitecturex.di.diModules
import tech.takenoko.cleanarchitecturex.service.AppJobService

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        // Dependency Injection
        startKoin {
            androidContext(this@App)
            modules(diModules)
        }

        // JobService
        AppJobService.schedule(this)
    }
}
