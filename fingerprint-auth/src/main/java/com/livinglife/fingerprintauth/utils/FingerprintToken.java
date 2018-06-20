package com.livinglife.fingerprintauth.utils;



public class FingerprintToken {
    private CipherHelper cipherHelper;

    public FingerprintToken(CipherHelper cipherHelper) {
        this.cipherHelper = cipherHelper;
    }

    public void validate(){
        if(cipherHelper !=null) {
            cipherHelper.generateNewKey();
        }
    }
}
