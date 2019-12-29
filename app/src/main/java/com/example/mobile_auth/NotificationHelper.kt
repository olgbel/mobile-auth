package com.example.mobile_auth

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.*
import com.example.mobile_auth.utils.API_SHARED_FILE
import com.example.mobile_auth.utils.LAST_TIME_VISIT_SHARED_KEY

object NotificationHelper {
    private val UPLOAD_CHANEL_ID = "upload_chanel_id"
    private var channelCreated = false
    private var lastNotificationId: Int? = null

    private val COMEBACK_FIRST_TIME_NOTIFY_ID = 0
    private val RETURN_NOTIFY_ID = 1
    private val COMEBACK_CHANNEL_ID = "comeback_chanel_id"

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
                "Понравилось ли вам у нас?",
                "Дорогой пользователь, возвращайтесь к нам скорее",
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
            .setSmallIcon(R.mipmap.ic_launcher_round).setContentTitle("Новый контент!")
            .setContentText("Проверь, что нового появилось, пока тебя не было!")
            .setStyle(NotificationCompat.BigTextStyle())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        builder.priority = NotificationManager.IMPORTANCE_HIGH
        NotificationManagerCompat.from(context).notify(RETURN_NOTIFY_ID, builder.build())
    }

//    fun mediaUploaded(type: AttachmentType, context: Context) {
//        createNotificationChannelIfNotCreated(context)
//        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            createBuilder(
//                context,
//                "Media uploaded",
//                "your ${type.name.toLowerCase()} successfully uploaded.",
//                NotificationManager.IMPORTANCE_HIGH
//            )
//        } else {
//            createBuilder(
//                context,
//                "Media uploaded",
//                "your ${type.name.toLowerCase()} successfully uploaded."
//            )
//        }
//        showNotification(context, builder)
//    }

    private fun showNotification(
        context: Context,
        builder: NotificationCompat.Builder
    ) {
        with(NotificationManagerCompat.from(context)) {
            val notificationId = Random().nextInt(100000)
            lastNotificationId = notificationId
            notify(notificationId, builder.build())
        }
    }

    //    @TargetApi(24)
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
    ): NotificationCompat.Builder {
        val builder = NotificationCompat.Builder(context, UPLOAD_CHANEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        return builder
    }

    private fun createNotificationChannelIfNotCreated(context: Context) {
        if (!channelCreated) {
            createNotificationChannel(context)
            channelCreated = true
        }
    }

    private fun createNotificationChannel(context: Context) {
        val name = "come back"
        val descriptionText = "Notifies when user close app after 1st visit & do not open it long time"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(UPLOAD_CHANEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}