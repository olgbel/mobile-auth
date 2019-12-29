package com.example.mobile_auth.utils

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.mobile_auth.NotificationHelper
import com.example.mobile_auth.NotificationHelper.getLastVisitTime
import com.example.mobile_auth.NotificationHelper.isFirstTime

class UserNotHereWorker(val context: Context, workerParameters: WorkerParameters) : Worker(context, workerParameters) {

    override fun doWork(): Result {
        if (!isFirstTime(context) && (getLastVisitTime(context) + SHOW_NOTIFICATION_AFTER_UNVISITED_MS < System.currentTimeMillis()))
            NotificationHelper.returnToAppNotification(context)

        return Result.success()
    }
}