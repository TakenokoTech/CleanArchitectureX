package tech.takenoko.cleanarchitecturex.repository.remote

import android.content.Context
import androidx.annotation.WorkerThread
import tech.takenoko.cleanarchitecturex.entities.ApiResult
import tech.takenoko.cleanarchitecturex.entities.Get
import tech.takenoko.cleanarchitecturex.entities.HttpStatusCode
import tech.takenoko.cleanarchitecturex.entities.Post
import tech.takenoko.cleanarchitecturex.entities.response.ResultEntity
import tech.takenoko.cleanarchitecturex.entities.response.UserEntity
import tech.takenoko.cleanarchitecturex.extention.listAdapter

class UserRemoteDataSource(context: Context) : BaseDataSource(context) {

    private val getUserUrl = "https://us-central1-takenokotechapi.cloudfunctions.net/getUser"
    private val addUserUrl = "https://us-central1-takenokotechapi.cloudfunctions.net/addUser"
    private val failedUrl = "https://us-central1-takenokotechapi.cloudfunctions.net/failed"

    @WorkerThread
    suspend fun getUser(): List<UserEntity> {
        val param =
            Get<List<UserEntity>>(
                url = getUserUrl,
                adapter = listAdapter()
            )
        fetch(param).let {
            return when (it) {
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
        fetch(param).let {
            return when (it) {
                is ApiResult.Success -> it.value
                is ApiResult.Failed -> throw it.cause
            }
        }
    }

    @WorkerThread
    suspend fun postFailed(): ResultEntity {
        val param =
            Post<ResultEntity>(url = addUserUrl)
        fetch(param).let {
            return when (it) {
                is ApiResult.Success -> it.value
                is ApiResult.Failed -> when (it.statusCode) {
                    HttpStatusCode.INTERNAL_SERVER_ERROR.code -> throw it.cause
                    else -> throw it.cause
                }
            }
        }
    }
}
