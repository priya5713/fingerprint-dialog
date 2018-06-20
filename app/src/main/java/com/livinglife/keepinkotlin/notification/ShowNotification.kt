package com.livinglife.keepinkotlin.notification

import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.livinglife.keepinkotlin.R
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

fun Context.showNotification(
        channelId: String,
        title: String
): Int {
    val notificationId = Math.random().toInt()
    val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setChannelId(channelId)
            .setAutoCancel(true)

    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    notificationManager.notify(notificationId, notificationBuilder.build())
    return notificationId
}

fun Context.showExpandedNotification(
        channelId: String,
        title: String,
        content: String
): Int {
    val notificationId = Math.random().toInt()
    val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setChannelId(channelId)
            .setContentText(content)
            .setStyle(NotificationCompat.BigTextStyle().bigText(content))
            .setAutoCancel(true)

    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    notificationManager.notify(notificationId, notificationBuilder.build())
    return notificationId
}


fun Context.showNotificationImageUrl(

        channelId: String,
        title: String,
        content: String,
        url: String

): Int {

    val notificationId = Math.random().toInt()
    val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(content)
            .setAutoCancel(true)

    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    class ImageDownloadAsyncTask : AsyncTask<String, Void, Bitmap>() {
        override fun doInBackground(vararg params: String): Bitmap? {
            try {
                val url2 = URL(params[0])
                val connection = url2.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input = connection.inputStream
                return BitmapFactory.decodeStream(input)
            } catch (e: Exception) {
                Log.d(ContentValues.TAG, e.message)
            }
            return null
        }

        override fun onPostExecute(result: Bitmap) {
            notificationBuilder.setLargeIcon(result)
            notificationBuilder.setStyle(NotificationCompat.BigPictureStyle().bigPicture(result))
            notificationManager.notify(notificationId, notificationBuilder.build())

        }
    }
    ImageDownloadAsyncTask().execute(url)
    return notificationId
}


fun Context.showNotificationImageResource(

        channelId: String,
        title: String,
        content: String,
        img: Int
): Int {

    val notificationId = Math.random().toInt()
    val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setLargeIcon(BitmapFactory.decodeResource(resources, img))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(content)
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(resources, img)))
            .setAutoCancel(true)

    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.notify(notificationId, notificationBuilder.build())
    return notificationId
}

fun Context.showNotificationImageDrawable(

        channelId: String,
        title: String,
        content: String,
        img: Bitmap
): Int {

    val notificationId = Math.random().toInt()
    val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setLargeIcon(img)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(content)
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(img))
            .setAutoCancel(true)

    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.notify(notificationId, notificationBuilder.build())
    return notificationId
}

fun Context.showNotificationIndextype(
        channelId: String,
        title: String,
        content: String,
        strings: List<String>,
        contentTitle: String,
        summaryText: String

): Int {
    var addString = StringBuilder()
    for (i in strings) {
        addString.append(i)
    }

    val notificationId = Math.random().toInt()
    val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setChannelId(channelId)
            .setContentText(content)
            .setAutoCancel(true)
    var notificationCompact = NotificationCompat.InboxStyle().setBigContentTitle(contentTitle).setSummaryText(summaryText)

    for (i in strings)
        notificationCompact.addLine(i)

    notificationBuilder.setStyle(notificationCompact)

    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    notificationManager.notify(notificationId, notificationBuilder.build())
    return notificationId
}