package tech.takenoko.cleanarchitecturex.di

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import tech.takenoko.cleanarchitecturex.repository.UserRepository
import tech.takenoko.cleanarchitecturex.repository.UserRepositoryImpl
import tech.takenoko.cleanarchitecturex.repository.local.UserLocalDataSource
import tech.takenoko.cleanarchitecturex.repository.remote.UserRemoteDataSource
import tech.takenoko.cleanarchitecturex.usecase.BackgroundUsecase
import tech.takenoko.cleanarchitecturex.usecase.LoadUserUsecase
import tech.takenoko.cleanarchitecturex.viewmodel.TopViewModel

private val appModule = module {
}

private val viewmodelModules = module {
    factory { TopViewModel() }
}

private val usecaseModules = module {
    factory { LoadUserUsecase(androidContext() as Application, it.get<CoroutineScope>() as CoroutineScope) }
    factory { BackgroundUsecase(androidContext() as Application, it.get<CoroutineScope>() as CoroutineScope) }
}

private val repositoryModules = module {
    factory { UserRepositoryImpl() as UserRepository }
}

private val localModules = module {
    single { AppDatabase.getDatabase(androidContext() as Application) }
    single { UserLocalDataSource() }
}

private val remoteModules = module {
    single { AppRestApiImpl() as AppRestApi }
    single { UserRemoteDataSource() }
}

val diModules = listOf(
    appModule,
    viewmodelModules,
    usecaseModules,
    repositoryModules,
    localModules,
    remoteModules
)
