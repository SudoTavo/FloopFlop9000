package com.floopflop;

public class AndroidNativePlatform extends Platform {

    private AndroidNativeService androidNativeService;
    
    @Override
    public NativeService getNativeService() {
        if (androidNativeService == null) {
            androidNativeService = new AndroidNativeService();
        }
        return androidNativeService;
    }
    
}