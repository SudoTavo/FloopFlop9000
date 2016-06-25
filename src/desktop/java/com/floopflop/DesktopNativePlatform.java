package com.floopflop;

import com.floopflop.Platform;
import com.floopflop.NativeService;

public class DesktopNativePlatform extends Platform {

    private DesktopNativeService androidNativeService;

    @Override
    public NativeService getNativeService() {
        if (androidNativeService == null) {
            androidNativeService = new DesktopNativeService();
        }
        return androidNativeService;
    }

}