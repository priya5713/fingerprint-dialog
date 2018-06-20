package com.livinglife.keepinkotlin.fingerprint

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import com.livinglife.fingerprintauth.callback.FingerprintDialogCallback
import com.livinglife.fingerprintauth.dialog.DialogAnimation
import com.livinglife.fingerprintauth.dialog.FingerprintDialog
import com.livinglife.keepinkotlin.R
import com.livinglife.keepinkotlin.databinding.ActivityFingerPrintBinding

class FingerPrintActivity : AppCompatActivity(), View.OnClickListener, FingerprintDialogCallback {

    private lateinit var mBinding: ActivityFingerPrintBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this@FingerPrintActivity, R.layout.activity_finger_print)
        mBinding.button.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        if (FingerprintDialog.isAvailable(this))
            FingerprintDialog.initialize(this)
                    .title("Fingerprint Dialog")
                    .message("Auth")
                    .cancelOnTouchOutside(false)
                    .cancelOnPressBack(false)
                    .enterAnimation(DialogAnimation.Enter.APPEAR)
                    .exitAnimation(DialogAnimation.Exit.BOTTOM)
                    .dimBackground(false)
                    .callback(this)
                    .show()
        else {
            Toast.makeText(this@FingerPrintActivity, "FingerPrint not found", Toast.LENGTH_LONG).show()
        }


    }

    override fun onAuthenticationSucceeded() {
        Toast.makeText(this@FingerPrintActivity, "Successfully authenticated", Toast.LENGTH_SHORT).show()
    }

    override fun onAuthenticationCancel() {
    }
}
