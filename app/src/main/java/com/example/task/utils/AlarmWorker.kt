package com.example.task.utils

import android.content.Context
import android.content.Intent
import androidx.work.Worker
import androidx.work.WorkerParameters

class AlarmWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        val intent = Intent(applicationContext, AlarmReceiver::class.java)
        applicationContext.sendBroadcast(intent)
        return Result.success()
    }
}