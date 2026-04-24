package com.music42.swiftyprotein.ui.proteinview

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.music42.swiftyprotein.R

class MediaProjectionForegroundService : Service() {
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        ensureChannel()
        try {
            val notification = buildNotification()
            if (Build.VERSION.SDK_INT >= 29) {
                startForeground(
                    NOTIFICATION_ID,
                    notification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION
                )
            } else {
                startForeground(NOTIFICATION_ID, notification)
            }
        } catch (t: Throwable) {
            stopSelf()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    private fun ensureChannel() {
        if (Build.VERSION.SDK_INT < 26) return
        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val existing = nm.getNotificationChannel(CHANNEL_ID)
        if (existing != null) return
        nm.createNotificationChannel(
            NotificationChannel(
                CHANNEL_ID,
                "Screen capture",
                NotificationManager.IMPORTANCE_LOW
            )
        )
    }

    private fun buildNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Recording")
            .setContentText("SwiftyProtein is recording the screen")
            .setOngoing(true)
            .build()
    }

    companion object {
        private const val CHANNEL_ID = "media_projection"
        private const val NOTIFICATION_ID = 4242
    }
}
