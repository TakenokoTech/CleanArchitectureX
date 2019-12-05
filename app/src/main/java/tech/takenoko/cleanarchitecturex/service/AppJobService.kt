package tech.takenoko.cleanarchitecturex.service

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import tech.takenoko.cleanarchitecturex.entities.UsecaseResult
import tech.takenoko.cleanarchitecturex.entities.isFinished
import tech.takenoko.cleanarchitecturex.usecase.BackgroundUsecase
import tech.takenoko.cleanarchitecturex.utils.AppLog

class AppJobService : JobService(), LifecycleOwner {

    private val serviceScope = CoroutineScope(Dispatchers.Main + Job())
    private val dispatcher = ServiceLifecycleDispatcher(this)
    override fun getLifecycle(): Lifecycle = dispatcher.lifecycle

    private val backgroundUsecase: BackgroundUsecase by inject { parametersOf(serviceScope) }

    override fun onStartJob(params: JobParameters?): Boolean {
        AppLog.info(TAG, ">>> onStartJob")
        backgroundUsecase.source.observe(this@AppJobService, observer(params))
        backgroundUsecase.execute(BackgroundUsecase.BackgroundUsecaseParam())
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        AppLog.info(TAG, ">>> onStopJob")
        jobFinished(params, false)
        return false
    }

    fun observer(params: JobParameters?) = Observer<UsecaseResult<Boolean>> {
        if (it.isFinished()) {
            AppLog.info(TAG, "<<< onStartJob")
            jobFinished(params, false)
        }
    }

    companion object {
        private val TAG = AppJobService::class.java.simpleName
        private const val JOB_ID = 10000

        fun schedule(context: Context) {
            val scheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            val jobInfo = JobInfo.Builder(JOB_ID, ComponentName(context, AppJobService::class.java)).apply {
                setBackoffCriteria(5000, JobInfo.BACKOFF_POLICY_LINEAR)
                setPersisted(false)
                setPeriodic(1)
                setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE)
                setRequiresCharging(false)
            }.build()
            scheduler.schedule(jobInfo)
        }
    }
}
