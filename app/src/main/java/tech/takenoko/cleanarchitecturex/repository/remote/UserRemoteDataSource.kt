package tech.takenoko.cleanarchitecturex.repository.remote

import androidx.annotation.WorkerThread
import org.koin.core.KoinComponent
import org.koin.core.inject
import tech.takenoko.cleanarchitecturex.di.AppRestApi
import tech.takenoko.cleanarchitecturex.di.fetch
import tech.takenoko.cleanarchitecturex.entities.ApiResult
import tech.takenoko.cleanarchitecturex.entities.Get
import tech.takenoko.cleanarchitecturex.entities.Post
import tech.takenoko.cleanarchitecturex.entities.response.ResultEntity
import tech.takenoko.cleanarchitecturex.entities.response.UserEntity
import tech.takenoko.cleanarchitecturex.extension.listAdapter

class UserRemoteDataSource : KoinComponent {

    private val restApi: AppRestApi by inject()

    @WorkerThread
    suspend fun getUser(): List<UserEntity> {
        val param = Get<List<UserEntity>>(
            url = getUserUrl,
            adapter = listAdapter()
        )
        return when (val it = restApi.fetch(param)) {
            is ApiResult.Success -> it.value
            is ApiResult.Failed -> when (it.statusCode) {
                else -> throw it.cause
            }
        }
    }

    @WorkerThread
    suspend fun postUser(user: UserEntity): ResultEntity {
        val param = Post<ResultEntity>(
            url = addUserUrl,
            body = user
        )
        return when (val it = restApi.fetch(param)) {
            is ApiResult.Success -> it.value
            is ApiResult.Failed -> when (it.statusCode) {
                else -> throw it.cause
            }
        }
    }

    companion object {
        const val getUserUrl = "https://xxxx/getUser"
        const val addUserUrl = "https://xxxx/addUser"
    }
}
