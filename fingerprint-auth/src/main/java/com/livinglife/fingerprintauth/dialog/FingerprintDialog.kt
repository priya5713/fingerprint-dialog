package com.livinglife.fingerprintauth.dialog

import android.content.Context
import android.hardware.fingerprint.FingerprintManager
import android.os.Handler
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.AppCompatButton
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.TextView

import com.livinglife.fingerprintauth.R
import com.livinglife.fingerprintauth.callback.FailAuthCounterCallback
import com.livinglife.fingerprintauth.callback.FingerprintCallback
import com.livinglife.fingerprintauth.callback.FingerprintDialogCallback
import com.livinglife.fingerprintauth.callback.FingerprintDialogSecureCallback
import com.livinglife.fingerprintauth.callback.FingerprintSecureCallback
import com.livinglife.fingerprintauth.utils.FingerprintToken
import com.livinglife.fingerprintauth.view.Fingerprint


class FingerprintDialog private constructor(context: Context) : AnimatedDialog<FingerprintDialog>(context) {
    private var fingerprint: Fingerprint? = null
    private var dialogTitle: TextView? = null
    private var dialogMessage: TextView? = null
    private var dialogStatus: TextView? = null
    private var cancelButton: AppCompatButton? = null
    private var usePasswordButton: AppCompatButton? = null

    private var fingerprintDialogCallback: FingerprintDialogCallback? = null
    private var fingerprintDialogSecureCallback: FingerprintDialogSecureCallback? = null

    private var statusScanningColor: Int = 0
    private var statusSuccessColor: Int = 0
    private var statusErrorColor: Int = 0
    private var onUsePassword: View.OnClickListener? = null
    private var handler: Handler? = null

    private var delayAfterError: Int = 0
    private var delayAfterSuccess: Int = 0

    private val returnToScanning = Runnable { setStatus(R.string.fingerprint_state_scanning, statusScanningColor) }

    private val fingerprintCallback = object : FingerprintCallback {
        override fun onAuthenticationSucceeded() {
            handler?.removeCallbacks(returnToScanning)
            setStatus(R.string.fingerprint_state_success, statusSuccessColor)
            handler?.postDelayed({
                dialog.cancel()
                if (fingerprintDialogCallback != null) {
                    fingerprintDialogCallback?.onAuthenticationSucceeded()
                }
            }, delayAfterSuccess.toLong())
        }

        override fun onAuthenticationFailed() {
            setStatus(R.string.fingerprint_state_failure, statusErrorColor)
            handler?.postDelayed(returnToScanning, delayAfterError.toLong())
        }

        override fun onAuthenticationError(errorCode: Int, error: String) {
            setStatus(error, statusErrorColor)
            handler?.postDelayed(returnToScanning, delayAfterError.toLong())
        }
    }

    private val fingerprintSecureCallback = object : FingerprintSecureCallback {
        override fun onAuthenticationSucceeded() {
            handler?.removeCallbacks(returnToScanning)
            setStatus(R.string.fingerprint_state_success, statusSuccessColor)
            handler?.postDelayed({
                dialog.cancel()
                if (fingerprintDialogSecureCallback != null) {
                    fingerprintDialogSecureCallback?.onAuthenticationSucceeded()
                }
            }, delayAfterSuccess.toLong())
        }

        override fun onAuthenticationFailed() {
            setStatus(R.string.fingerprint_state_failure, statusErrorColor)
            handler?.postDelayed(returnToScanning, delayAfterError.toLong())
        }

        override fun onNewFingerprintEnrolled(token: FingerprintToken) {
            dialog.cancel()
            if (fingerprintDialogSecureCallback != null) {
                fingerprintDialogSecureCallback?.onNewFingerprintEnrolled(token)
            }
        }

        override fun onAuthenticationError(errorCode: Int, error: String) {
            setStatus(error, statusErrorColor)
            handler?.postDelayed(returnToScanning, delayAfterError.toLong())
        }
    }

    init {
        init()
    }

    private fun init() {
        this.handler = Handler()
        this.onUsePassword = null
        this.delayAfterError = Fingerprint.DEFAULT_DELAY_AFTER_ERROR
        this.delayAfterSuccess = Fingerprint.DEFAULT_DELAY_AFTER_ERROR

        this.statusScanningColor = R.color.status_scanning
        this.statusSuccessColor = R.color.status_success
        this.statusErrorColor = R.color.status_error

        this.dialogView = layoutInflater.inflate(R.layout.fingerprint_dialog, null)
        this.fingerprint = dialogView.findViewById(R.id.fingerprint_dialog_fp)
        this.dialogTitle = dialogView.findViewById(R.id.fingerprint_dialog_title)
        this.dialogMessage = dialogView.findViewById(R.id.fingerprint_dialog_message)
        this.dialogStatus = dialogView.findViewById(R.id.fingerprint_dialog_status)
        this.cancelButton = dialogView.findViewById(R.id.fingerprint_dialog_cancel)
        this.usePasswordButton = dialogView.findViewById(R.id.fingerprint_dialog_use_password)
    }

    /**
     * Set an authentication callback.
     * @param fingerprintDialogCallback The callback
     * @return FingerprintDialog object
     */
    fun callback(fingerprintDialogCallback: FingerprintDialogCallback): FingerprintDialog {
        this.fingerprintDialogCallback = fingerprintDialogCallback
        this.fingerprint?.callback(fingerprintCallback)
        return this
    }

    /**
     * Set a callback for secured authentication.
     * @param fingerprintDialogSecureCallback The callback
     * @param KEY_NAME An arbitrary string used to create a cipher pair in the Android KeyStore
     * @return FingerprintDialog object
     */
    fun callback(fingerprintDialogSecureCallback: FingerprintDialogSecureCallback, KEY_NAME: String): FingerprintDialog {
        this.fingerprintDialogSecureCallback = fingerprintDialogSecureCallback
        this.fingerprint?.callback(fingerprintSecureCallback, KEY_NAME)
        return this
    }

    /**
     * Perform a secured authentication using that particular CryptoObject.
     * @param cryptoObject CryptoObject to use
     * @return FingerprintDialog object
     */
    fun cryptoObject(cryptoObject: FingerprintManager.CryptoObject): FingerprintDialog {
        this.fingerprint?.cryptoObject(cryptoObject)
        return this
    }

    /**
     * Set color of the fingerprint scanning status.
     * @param fingerprintScanningColor resource color
     * @return FingerprintDialog object
     */
    fun fingerprintScanningColor(fingerprintScanningColor: Int): FingerprintDialog {
        this.fingerprint?.fingerprintScanningColor(fingerprintScanningColor)
        return this
    }

    /**
     * Set color of the fingerprint success status.
     * @param fingerprintSuccessColor resource color
     * @return FingerprintDialog object
     */
    fun fingerprintSuccessColor(fingerprintSuccessColor: Int): FingerprintDialog {
        this.fingerprint?.fingerprintSuccessColor(fingerprintSuccessColor)
        return this
    }

    /**
     * Set color of the fingerprint error status.
     * @param fingerprintErrorColor resource color
     * @return FingerprintDialog object
     */
    fun fingerprintErrorColor(fingerprintErrorColor: Int): FingerprintDialog {
        this.fingerprint?.fingerprintErrorColor(fingerprintErrorColor)
        return this
    }

    /**
     * Set color of the circle scanning status.
     * @param circleScanningColor resource color
     * @return FingerprintDialog object
     */
    fun circleScanningColor(circleScanningColor: Int): FingerprintDialog {
        this.fingerprint?.circleScanningColor(circleScanningColor)
        return this
    }

    /**
     * Set color of the circle success status.
     * @param circleSuccessColor resource color
     * @return FingerprintDialog object
     */
    fun circleSuccessColor(circleSuccessColor: Int): FingerprintDialog {
        this.fingerprint?.circleSuccessColor(circleSuccessColor)
        return this
    }

    /**
     * Set color of the circle error status.
     * @param circleErrorColor resource color
     * @return FingerprintDialog object
     */
    fun circleErrorColor(circleErrorColor: Int): FingerprintDialog {
        this.fingerprint?.circleErrorColor(circleErrorColor)
        return this
    }

    /**
     * Set color of the text scanning status.
     * @param statusScanningColor resource color
     * @return FingerprintDialog object
     */
    fun statusScanningColor(statusScanningColor: Int): FingerprintDialog {
        this.statusScanningColor = statusScanningColor
        return this
    }

    /**
     * Set color of the text success status.
     * @param statusSuccessColor resource color
     * @return FingerprintDialog object
     */
    fun statusSuccessColor(statusSuccessColor: Int): FingerprintDialog {
        this.statusSuccessColor = statusSuccessColor
        return this
    }

    /**
     * Set color of the text error status.
     * @param statusErrorColor resource color
     * @return FingerprintDialog object
     */
    fun statusErrorColor(statusErrorColor: Int): FingerprintDialog {
        this.statusErrorColor = statusErrorColor
        return this
    }

    /**
     * Set delay before triggering callback after a failed attempt to authenticate.
     * @param delayAfterError delay in milliseconds
     * @return FingerprintDialog object
     */
    fun delayAfterError(delayAfterError: Int): FingerprintDialog {
        this.delayAfterError = delayAfterError
        this.fingerprint?.delayAfterError(delayAfterError)
        return this
    }

    /**
     * Set delay before triggering callback after a successful authentication.
     * @param delayAfterSuccess delay in milliseconds
     * @return FingerprintDialog object
     */
    fun delayAfterSuccess(delayAfterSuccess: Int): FingerprintDialog {
        this.delayAfterSuccess = delayAfterSuccess
        return this
    }

    /**
     * Set a fail limit. Android blocks automatically when 5 attempts failed.
     * @param limit number of tries
     * @param counterCallback callback to be triggered when limit is reached
     * @return FingerprintDialog object
     */
    fun tryLimit(limit: Int, counterCallback: FailAuthCounterCallback): FingerprintDialog {
        this.fingerprint?.tryLimit(limit, counterCallback)
        return this
    }

    /**
     * Display a "use password" button on the dialog.
     * @param onUsePassword OnClickListener triggered when button is clicked
     * @return FingerprintDialog object
     */
    fun usePasswordButton(onUsePassword: View.OnClickListener): FingerprintDialog {
        this.onUsePassword = onUsePassword
        return this
    }

    /**
     * Show the dialog.
     */
    fun show() {
        if (title == null || message == null) {
            throw RuntimeException("Title or message cannot be null.")
        }

        showDialog()
    }

    private fun showDialog() {
        dialogTitle?.text = title
        dialogMessage?.text = message
        cancelButton?.setText(R.string.fingerprint_cancel)
        usePasswordButton?.setText(R.string.fingerprint_use_password)
        setStatus(R.string.fingerprint_state_scanning, statusScanningColor)

        builder.setView(dialogView)
        dialog = builder.create()

        cancelButton?.setOnClickListener {
            fingerprint?.cancel()
            if (fingerprintDialogSecureCallback != null) {
                fingerprintDialogSecureCallback?.onAuthenticationCancel()
            } else {
                fingerprintDialogCallback?.onAuthenticationCancel()
            }
            dialog.cancel()
        }

        if (onUsePassword == null) {
            usePasswordButton?.visibility = View.GONE
        } else {
            usePasswordButton?.setOnClickListener { view ->
                fingerprint?.cancel()
                dialog.cancel()
                onUsePassword?.onClick(view)
            }
        }

        if (dialog.window != null) {
            if (enterAnimation != DialogAnimation.Enter.APPEAR || exitAnimation != DialogAnimation.Exit.DISAPPEAR) {
                val style = DialogAnimation.getStyle(enterAnimation, exitAnimation)
                if (style != -1) {
                    dialog.window?.attributes?.windowAnimations = style
                } else {
                    Log.w(TAG, "The animation selected is not available. Default animation will be used.")
                }
            }

            if (!dimBackground) {
                dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            }
        }
        dialog.setCanceledOnTouchOutside(cancelOnTouchOutside)
        dialog.setCancelable(cancelOnPressBack)
        dialog.show()

        authenticate()
    }

    private fun authenticate() {
        fingerprint?.authenticate()
    }

    private fun setStatus(textId: Int, textColorId: Int) {
        setStatus(context.resources.getString(textId), textColorId)
    }

    private fun setStatus(text: String, textColorId: Int) {
        dialogStatus?.setTextColor(ResourcesCompat.getColor(context.resources, textColorId, context.theme))
        dialogStatus?.text = text
    }

    companion object {

        private val TAG = "FingerprintDialog"

        /**
         * Check if a fingerprint scanner is available and if at least one finger is enrolled in the phone.
         * @param context A context
         * @return True is authentication is available, False otherwise
         */
        fun isAvailable(context: Context): Boolean {
            val manager = context.getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager
            return manager.isHardwareDetected && manager.hasEnrolledFingerprints()
        }

        /**
         * Create a FingerprintDialog instance.
         * @param context Activity Context
         * @return FingerprintDialog instance
         */
        fun initialize(context: Context): FingerprintDialog {
            return FingerprintDialog(context)
        }
    }
}