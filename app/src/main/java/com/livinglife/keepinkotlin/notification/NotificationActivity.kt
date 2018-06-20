package com.livinglife.keepinkotlin.notification

import android.annotation.TargetApi
import android.databinding.DataBindingUtil
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.livinglife.keepinkotlin.R
import com.livinglife.keepinkotlin.databinding.ActivityNotificationBinding

class NotificationActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityNotificationBinding
    @TargetApi(Build.VERSION_CODES.O)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this@NotificationActivity, R.layout.activity_notification)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ChannelBuilder(this).createChannels("11", "Default")
        }
        mBinding.btnNotification.setOnClickListener {
            showNotification("11", "Simple Notification")
        }
        mBinding.btnNotification2.setOnClickListener {
            showExpandedNotification("11", "Expanded Notification", "gfadgfjgadjfjkadgfjkadgfjkadgfadjgfjkagfkagfasjfgjkagfjkagfjkasgfjgajgfjagfjgasjkfgjaskgfjasgfjgajkfgjagfjagfjgafgjagfjagfajgfhagfgahgfhagfhagfjagfjagjfgsdjhfgjsdfjsdgfjsdgfsdgfjsgfjsdgfjgsdfgsdjfgsjhgfjhsgfsfsgf")
        }
        mBinding.btnNotification3.setOnClickListener {
            showNotificationImageResource("11", "Image Notification", "Hello image", R.drawable.ic_launcher_background)
        }
        mBinding.btnNotification4.setOnClickListener {
            showNotificationIndextype("11", "Input Notification", "hhhhhh", listOf("one", "two", "threee"), "cdcshch", "+3image")
        }
        mBinding.btnNotification5.setOnClickListener {
            showNotificationImageUrl("11", "URL Notification", "content", "https://www.gstatic.com/webp/gallery3/1.png")
        }
        mBinding.btnNotification6.setOnClickListener {
            showNotificationImageDrawable("11", "URL Notification", "content", BitmapFactory.decodeResource(resources, R.drawable.ic_launcher_background))
        }
    }
}
