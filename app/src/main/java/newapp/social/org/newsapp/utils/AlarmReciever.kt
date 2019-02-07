package newapp.social.org.newsapp.utils

import android.app.Notification
import android.app.Notification.PRIORITY_MIN
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startForegroundService
import android.os.Build
import android.support.v4.app.JobIntentService
import android.support.v4.app.NotificationCompat
import newapp.social.org.newsapp.R


class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val service = Intent(context, NotificationService::class.java)
        service.putExtra("reason", intent.getStringExtra("reason"))
        service.putExtra("timestamp", intent.getLongExtra("timestamp", 0))

        JobIntentService.enqueueWork(context,NotificationService::class.java,1,Intent())
    }



}