package newapp.social.org.newsapp.utils

import android.app.*
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.JobIntentService
import android.support.v4.app.NotificationCompat
import newapp.social.org.newsapp.Activities.MainActivity
import newapp.social.org.newsapp.R
import java.util.*



class NotificationService : JobIntentService() {

    override fun onHandleWork(intent: Intent) {
        createChannel()


        var timestamp: Long = 0
        if (intent.extras != null) {
            timestamp = intent.extras!!.getLong("timestamp")
        }

        val title = "Trending news"
        var message = "Click to get trending news."

        val appDb = AppDB.getAppDatabase(application)
        val list = appDb.newsDao().getAll()

        if (!list.isNullOrEmpty()) {
            list.get(0).title?.let {
                message = it
            }
        }



        val context = this.applicationContext
        var notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notifyIntent = Intent(this, MainActivity::class.java)
        notifyIntent.putExtra("title", title)
        notifyIntent.putExtra("message", message)
        notifyIntent.putExtra("notification", true)

        notifyIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        val pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val res = this.resources
        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotification = Notification.Builder(this, CHANNEL_ID)
                    // Set the intent that will fire when the user taps the notification
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_location)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setStyle(Notification.BigTextStyle()
                        .bigText(message))
                    .setContentText(message).build()
        } else {
            mNotification = Notification.Builder(this)
                    // Set the intent that will fire when the user taps the notification
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_location)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setContentTitle(title)
                    .setStyle(Notification.BigTextStyle()
                        .bigText(message))
                    .setSound(uri)
                    .setContentText(message).build()

        }



        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // mNotificationId is a unique int for each notification that you must define
        notificationManager.notify(mNotificationId, mNotification)
    }


    private lateinit var mNotification: Notification
    private val mNotificationId: Int = 1000

    private fun createChannel() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val context = this.applicationContext
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
            notificationChannel.enableVibration(true)
            notificationChannel.setShowBadge(true)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.parseColor("#e8334a")
            notificationChannel.description = "aaya"
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationManager.createNotificationChannel(notificationChannel)
        }

    }

    companion object {

        const val CHANNEL_ID = "newapp.social.org.newsapp.CHANNEL_ID"
        const val CHANNEL_NAME = "Sample Notification"
    }

}