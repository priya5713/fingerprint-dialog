package com.livinglife.fingerprintauth.callback;


import com.livinglife.fingerprintauth.utils.FingerprintToken;


public interface FingerprintDialogSecureCallback {
    void onAuthenticationSucceeded();
    void onAuthenticationCancel();
    void onNewFingerprintEnrolled(FingerprintToken token);
}
