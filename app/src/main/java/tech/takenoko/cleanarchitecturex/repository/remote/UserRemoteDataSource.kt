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
import tech.takenoko.cleanarchitecturex.extention.listAdapter

class UserRemoteDataSource : KoinComponent {

    private val restApi: AppRestApi by inject()

    @WorkerThread
    suspend fun getUser(): List<UserEntity> {
        val param = Get<List<UserEntity>>(
            url = getUserUrl,
            adapter = listAdapter()
        )
        return restApi.fetch(param).let {
            when (it) {
                is ApiResult.Success -> it.value
                is ApiResult.Failed -> throw it.cause
            }
        }
    }

    @WorkerThread
    suspend fun postUser(): ResultEntity {
        val param = Post<ResultEntity>(
            url = addUserUrl,
            body = UserEntity("user1")
        )
        return restApi.fetch(param).let {
            when (it) {
                is ApiResult.Success -> it.value
                is ApiResult.Failed -> throw it.cause
            }
        }
    }

    @WorkerThread
    suspend fun postFailed(): ResultEntity {
        val param = Post<ResultEntity>(
            url = failedUrl
        )
        return restApi.fetch(param).let {
            when (it) {
                is ApiResult.Success -> it.value
                is ApiResult.Failed -> when (it.statusCode) {
                    else -> throw it.cause
                }
            }
        }
    }

    companion object {
        const val getUserUrl = "https://us-central1-takenokotechapi.cloudfunctions.net/getUser"
        const val addUserUrl = "https://us-central1-takenokotechapi.cloudfunctions.net/addUser"
        const val failedUrl = "https://us-central1-takenokotechapi.cloudfunctions.net/failed"
    }
}
