package com.example.mobile_auth

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.mobile_auth.utils.*
import java.util.*

object NotificationHelper {
    private var channelCreated = false
    private var lastNotificationId: Int? = null

    fun isFirstTime(context: Context) =
        context.getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE).getLong(
            LAST_TIME_VISIT_SHARED_KEY, 0
        ) == 0L

    fun getLastVisitTime(context: Context): Long =
        context.getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE).getLong(
            LAST_TIME_VISIT_SHARED_KEY, 0L
        )

    fun setLastVisitTime(context: Context, currentTimeMillis: Long) =
        context.getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE)
            .edit()
            .putLong(LAST_TIME_VISIT_SHARED_KEY, currentTimeMillis)
            .commit()

    fun setNotFirstTime(context: Context) =
        context.getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(API_SHARED_FILE, false)

    fun comeBackNotification(context: Context) {
        createNotificationChannelIfNotCreated(context)

        val builder =
            createBuilder(
                context,
                R.string.come_back_notification_title.toString(),
                R.string.come_back_notification_content.toString(),
                NotificationManager.IMPORTANCE_HIGH
            )
        showNotification(context, builder)
    }

    fun returnToAppNotification(context: Context) {
        val intent = Intent(context, FeedActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            COMEBACK_FIRST_TIME_NOTIFY_ID, intent,
            COMEBACK_FIRST_TIME_NOTIFY_ID
        )

        createNotificationChannelIfNotCreated(context)
        val builder = NotificationCompat.Builder(context, COMEBACK_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round).setContentTitle(R.string.new_content_title.toString())
            .setContentText(R.string.notification_content_text.toString())
            .setStyle(NotificationCompat.BigTextStyle())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        builder.priority = NotificationManager.IMPORTANCE_HIGH
        NotificationManagerCompat.from(context).notify(RETURN_NOTIFY_ID, builder.build())
    }

    private fun showNotification(
        context: Context,
        builder: NotificationCompat.Builder
    ) {
        with(NotificationManagerCompat.from(context)) {
            val notificationId = Random().nextInt(RANDOM_NOTIFICATION)
            lastNotificationId = notificationId
            notify(notificationId, builder.build())
        }
    }

    private fun createBuilder(
        context: Context,
        title: String,
        content: String,
        priority: Int
    ): NotificationCompat.Builder {
        val builder = createBuilder(context, title, content)
        builder.priority = priority
        return builder
    }

    private fun createBuilder(
        context: Context,
        title: String,
        content: String
    ): NotificationCompat.Builder =
        NotificationCompat.Builder(context, UPLOAD_CHANEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    private fun createNotificationChannelIfNotCreated(context: Context) {
        if (!channelCreated) {
            createNotificationChannel(context)
            channelCreated = true
        }
    }

    private fun createNotificationChannel(context: Context) {
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(UPLOAD_CHANEL_ID, R.string.notification_channel_name.toString(), importance).apply {
            description = R.string.notification_channel_description.toString()
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}