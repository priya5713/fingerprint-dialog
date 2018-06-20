package com.livinglife.keepinkotlin.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.support.annotation.RequiresApi

class ChannelBuilder(base: Context) : ContextWrapper(base) {

    private var mManager: NotificationManager? = null

    private val manager: NotificationManager
        get() {
            if (mManager == null) {
                mManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            }
            return mManager as NotificationManager
        }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createChannels(id: String, channel_name: String) {

        val androidChannel = NotificationChannel(id,
                channel_name, NotificationManager.IMPORTANCE_DEFAULT)
        androidChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        manager.createNotificationChannel(androidChannel)
    }
}
