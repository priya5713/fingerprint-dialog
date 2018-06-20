package com.livinglife.fingerprintauth.callback;


import com.livinglife.fingerprintauth.utils.FingerprintToken;

public interface FingerprintSecureCallback {
    void onAuthenticationSucceeded();
    void onAuthenticationFailed();
    void onNewFingerprintEnrolled(FingerprintToken token);
    void onAuthenticationError(int errorCode, String error);
}
