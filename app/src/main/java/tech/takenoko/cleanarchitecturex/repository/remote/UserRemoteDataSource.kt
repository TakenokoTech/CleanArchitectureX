package tech.takenoko.cleanarchitecturex.repository.remote

import android.content.Context
import androidx.annotation.WorkerThread
import tech.takenoko.cleanarchitecturex.extention.listAdapter
import tech.takenoko.cleanarchitecturex.model.ApiParameter.*
import tech.takenoko.cleanarchitecturex.model.ApiResult
import tech.takenoko.cleanarchitecturex.model.HttpStatusCode
import tech.takenoko.cleanarchitecturex.model.response.ResultEntity
import tech.takenoko.cleanarchitecturex.model.response.UserEntity

class UserRemoteDataSource(context: Context): BaseDataSource(context) {

    private val getUserUrl = "https://us-central1-takenokotechapi.cloudfunctions.net/getUser"
    private val addUserUrl = "https://us-central1-takenokotechapi.cloudfunctions.net/addUser"
    private val failedUrl = "https://us-central1-takenokotechapi.cloudfunctions.net/failed"

    @WorkerThread
    suspend fun getUser(): List<UserEntity> {
        val param = GetParameter<List<UserEntity>>(url = getUserUrl, adapter = listAdapter())
        awaitGet(param).let {
            return when(it) {
                is ApiResult.Success -> it.value
                is ApiResult.Failed -> throw it.cause
            }
        }

    }

    @WorkerThread
    suspend fun postUser(): ResultEntity {
        val param = PostParameter<ResultEntity>(url = addUserUrl, body = UserEntity("user1"))
        awaitPost(param).let {
            return when (it) {
                is ApiResult.Success -> it.value
                is ApiResult.Failed -> throw it.cause
            }
        }
    }

    @WorkerThread
    suspend fun postFailed(): ResultEntity {
        val param = PostParameter<ResultEntity>(url = addUserUrl)
        awaitPost(param).let {
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
