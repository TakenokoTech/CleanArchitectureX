package tech.takenoko.cleanarchitecturex.usecase

import android.content.Context
import androidx.annotation.WorkerThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import tech.takenoko.cleanarchitecturex.utils.AppLog

open class BackgroundUsecase(context: Context, private val scope: CoroutineScope) : AsyncUsecase<Unit, Boolean>(context, scope) {

    @WorkerThread
    override suspend fun callAsync(param: Unit): Deferred<Boolean> = scope.async(Dispatchers.IO) {
        AppLog.info(TAG, "callAsync")

        repeat((0..1000).count()) {
            AppLog.info(TAG, "job...(${timeFormat(it * 10000L)})")
            Thread.sleep(10000)
        }

        return@async true
    }

    private fun timeFormat(ms: Long): String {
        val hour = (ms / 1000) / 3600
        val min = ((ms / 1000) % 3600) / 60
        val sec = (ms / 1000) % 60
        return "%02d:%02d:%02d".format(hour, min, sec)
    }

    companion object {
        private val TAG = BackgroundUsecase::class.java.simpleName
    }
}
