package com.livinglife.keepinkotlin

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.livinglife.keepinkotlin.databinding.ActivityMainBinding
import com.livinglife.keepinkotlin.fingerprint.FingerPrintActivity
import com.livinglife.keepinkotlin.notification.NotificationActivity

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
        mBinding.btnNotification.setOnClickListener { startActivity(Intent(this@MainActivity, NotificationActivity::class.java)) }
        mBinding.btnFingerprint.setOnClickListener { startActivity(Intent(this@MainActivity, FingerPrintActivity::class.java)) }

    }
}
