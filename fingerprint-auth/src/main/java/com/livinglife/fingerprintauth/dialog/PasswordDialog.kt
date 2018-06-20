package com.livinglife.fingerprintauth.dialog

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.text.InputType
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView

import com.livinglife.fingerprintauth.R
import com.livinglife.fingerprintauth.callback.FailAuthCounterCallback
import com.livinglife.fingerprintauth.callback.PasswordCallback
import com.livinglife.fingerprintauth.utils.FingerprintToken


class PasswordDialog private constructor(context: Context, private val token: FingerprintToken?) : AnimatedDialog<PasswordDialog>(context) {
    private var callback: PasswordCallback? = null
    private var counterCallback: FailAuthCounterCallback? = null

    private var passwordType: Int = 0
    private var limit: Int = 0
    private var tryCounter: Int = 0

    init {
        this.passwordType = PASSWORD_TYPE_TEXT
        this.callback = null
        this.counterCallback = null
        this.tryCounter = 0
    }

    /**
     * Set callback triggered when Password is entered.
     *
     * @param callback The callback
     * @return PasswordDialog object
     */
    fun callback(callback: PasswordCallback): PasswordDialog {
        this.callback = callback
        return this
    }

    /**
     * Set a fail limit.
     *
     * @param limit           Number of tries
     * @param counterCallback Callback triggered when limit is reached
     * @return PasswordDialog object
     */
    fun tryLimit(limit: Int, counterCallback: FailAuthCounterCallback): PasswordDialog {
        this.limit = limit
        this.counterCallback = counterCallback
        return this
    }

    /**
     * Set the password type (text or numbers)
     *
     * @param passwordType PASSWORD_TYPE_TEXT or PASSWORD_TYPE_NUMBER
     * @return PasswordDialog object
     */
    fun passwordType(passwordType: Int): PasswordDialog {
        this.passwordType = passwordType
        return this
    }

    /**
     * Show the password dialog
     */
    fun show() {
        if (title == null || message == null) {
            throw RuntimeException("Title or message cannot be null.")
        }

        dialogView = layoutInflater.inflate(R.layout.password_dialog, null)
        (dialogView.findViewById<View>(R.id.password_dialog_title) as TextView).text = title
        (dialogView.findViewById<View>(R.id.password_dialog_message) as TextView).text = message
        val input = dialogView.findViewById<EditText>(R.id.password_dialog_input)

        input.inputType = passwordType

        dialog = builder.setView(dialogView)
                .setPositiveButton(R.string.password_confirm) { dialogInterface, i ->
                    // have to override this listener, otherwise it will close the dialog
                }
                .setNegativeButton(R.string.password_cancel) { dialogInterface, i ->
                    if (callback != null) {
                        callback!!.onPasswordCancel()
                    }
                }
                .create()

        if (dialog.window != null) {
            if (enterAnimation !== DialogAnimation.Enter.APPEAR || exitAnimation !== DialogAnimation.Exit.DISAPPEAR) {
                val style = DialogAnimation.getStyle(enterAnimation, exitAnimation)
                if (style != -1) {
                    dialog.window!!.attributes.windowAnimations = style
                } else {
                    Log.w(TAG, "The animation selected is not available. Default animation will be used.")
                }
            }

            if (!dimBackground) {
                dialog.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            }
        } else {
            Log.w(TAG, "Could not get window from dialog")
        }
        dialog.setCanceledOnTouchOutside(cancelOnTouchOutside)
        dialog.setCancelable(cancelOnPressBack)
        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            if (callback != null) {
                val password = input.text.toString()
                if (callback!!.onPasswordCheck(password)) {
                    dialog.dismiss()
                    token?.validate()
                    tryCounter = 0
                    callback!!.onPasswordSucceeded()
                } else {
                    input.setText("")
                    input.error = context.resources.getString(R.string.password_incorrect)
                    tryCounter++
                    if (counterCallback != null && tryCounter == limit) {
                        counterCallback!!.onTryLimitReached()
                    }
                }
            }
        }
    }

    companion object {

        val PASSWORD_TYPE_TEXT = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        val PASSWORD_TYPE_NUMBER = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD

        private val TAG = "PasswordDialog"

        /**
         * Create a PasswordDialog instance.
         *
         * @param context Activity Context
         * @param token   Token got with FingerprintDialogSecureCallback
         * @return PasswordDialog instance
         */
        fun initialize(context: Context, token: FingerprintToken): PasswordDialog {
            return PasswordDialog(context, token)
        }

        /**
         * Create a PasswordDialog instance.
         *
         * @param context Activity Context
         * @return PasswordDialog instance
         */
        fun initialize(context: Context): PasswordDialog {
            return PasswordDialog(context, null)
        }
    }
}
