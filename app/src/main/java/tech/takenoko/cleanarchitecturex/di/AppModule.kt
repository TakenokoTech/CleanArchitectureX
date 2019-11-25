package tech.takenoko.cleanarchitecturex.di

import android.app.Application
import androidx.lifecycle.ViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import tech.takenoko.cleanarchitecturex.repository.UserRepository
import tech.takenoko.cleanarchitecturex.repository.UserRepositoryImpl
import tech.takenoko.cleanarchitecturex.usecase.LoadUserUsecase
import tech.takenoko.cleanarchitecturex.viewmodel.TopViewModel

val appModule = module {
}

val viewmodelModules = module {
    factory { TopViewModel() }
}

val usecaseModules = module {
    factory { LoadUserUsecase(androidContext() as Application, it.get<ViewModel>() as ViewModel) }
}

val repositoryModules = module {
    factory { UserRepositoryImpl(androidContext() as Application) as UserRepository }
}