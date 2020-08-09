package com.example.githubuserapp.reminder

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.githubuserapp.R
import com.example.githubuserapp.view.activity.MainActivity
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DailyReminderReceiver : BroadcastReceiver() {

    companion object {
        const val MESSAGE = "MESSAGE"
        const val notifID = 1

        fun setReminder(context: Context, time: String, message: String) {
            val timeFormat = "HH:mm"
            if (isDateInvalid(time, timeFormat)) return

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, DailyReminderReceiver::class.java)
            intent.putExtra(MESSAGE, message)

            val timeArray: List<String> = time.split(":")

            val calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = timeArray[0].toInt()
            calendar[Calendar.MINUTE] = timeArray[1].toInt()
            calendar[Calendar.SECOND] = 0
            val pendingIntent = PendingIntent.getBroadcast(context, notifID, intent, 0)

            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
        }

        fun cancelReminder(context: Context) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, DailyReminderReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, notifID, intent, 0)
            pendingIntent.cancel()

            alarmManager.cancel(pendingIntent)
        }
        private fun isDateInvalid(date: String, format: String) : Boolean {
            return try {
                val dateFormat : DateFormat = SimpleDateFormat(format, Locale.getDefault())
                dateFormat.isLenient = false
                dateFormat.parse(date)
                false
            } catch (e: ParseException) {
                true
            }
        }
    }
    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        val message = context.resources.getString(R.string.notification_message)
        showNotification(context, message)
    }

    private fun showNotification(context: Context, message: String) {
        val channel_id = "channel_githubuserapp"
        val channel_name = "Github User App Channel"

        val notifIntent = Intent(context, MainActivity::class.java)
        notifIntent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent = PendingIntent.getActivity(context, 0, notifIntent, 0)
        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notifSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(context, channel_id)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle(context.resources.getString(R.string.app_name))
            .setContentText(message)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(notifSound)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelNotification = NotificationChannel(channel_id, channel_name, NotificationManager.IMPORTANCE_HIGH)
            channelNotification.enableVibration(true)
            channelNotification.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            notificationBuilder.setChannelId(channel_id)
            notificationManagerCompat.createNotificationChannel(channelNotification)
        }

        val notification = notificationBuilder.build()
        notificationManagerCompat.notify(notifID, notification)
    }
}
