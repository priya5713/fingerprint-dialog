package com.livinglife.fingerprintauth.callback;



public interface FingerprintCallback {
    void onAuthenticationSucceeded();
    void onAuthenticationFailed();
    void onAuthenticationError(int errorCode, String error);
}
