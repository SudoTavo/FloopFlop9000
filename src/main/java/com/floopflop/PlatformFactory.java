package com.floopflop;

import com.gluonhq.charm.down.common.JavaFXPlatform;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlatformFactory {

    public static Platform getPlatform() {
        try {
            return (Platform) Class.forName(getPlatformClassName()).newInstance();
        } catch (Throwable ex) {
            Logger.getLogger(PlatformFactory.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    private static String getPlatformClassName() {
        switch (JavaFXPlatform.getCurrent()) {
            case ANDROID: return "com.floopflop.AndroidNativePlatform";
            case IOS: return "com.floopflop.IosNativePlatform";
            default : return "com.floopflop.DesktopNativePlatform";
        }
    }
}
