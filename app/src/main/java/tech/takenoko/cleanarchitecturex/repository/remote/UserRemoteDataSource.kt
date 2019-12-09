package tech.takenoko.cleanarchitecturex.repository.remote

import androidx.annotation.WorkerThread
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.koin.core.KoinComponent
import org.koin.core.inject
import tech.takenoko.cleanarchitecturex.di.AppRestApi
import tech.takenoko.cleanarchitecturex.di.fetch
import tech.takenoko.cleanarchitecturex.entities.ApiResult
import tech.takenoko.cleanarchitecturex.entities.Get
import tech.takenoko.cleanarchitecturex.entities.Post
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
            is ApiResult.Success -> when (it.value.status) {
                else -> it.value
            }
            is ApiResult.Failed -> when (it.statusCode) {
                else -> throw it.cause
            }
        }
    }

    @JsonClass(generateAdapter = true)
    data class UserEntity(
        @Json(name = "user_name") val userName: String,
        @Json(name = "display_name") val displayName: String
    )

    @JsonClass(generateAdapter = true)
    data class ResultEntity(
        @Json(name = "status") val status: String
    )

    companion object {
        const val getUserUrl = "https://xxxx/getUser"
        const val addUserUrl = "https://xxxx/addUser"
    }
}
