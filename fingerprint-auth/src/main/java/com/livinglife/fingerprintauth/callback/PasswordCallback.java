package com.livinglife.fingerprintauth.callback;


public interface PasswordCallback {
    void onPasswordSucceeded();
    boolean onPasswordCheck(String password);
    void onPasswordCancel();
}