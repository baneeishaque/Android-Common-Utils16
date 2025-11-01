package ndk.utils_android16;

import android.content.Context;

import ndk.utils_android1.UpdateUtils;

public class UpdateUtilsWrapperBase {

    private static String applicationName;

    UpdateUtilsWrapperBase(String applicationName) {
        UpdateUtilsWrapperBase.applicationName = applicationName;
    }

    public static String[] getFlavouredServerVersion(String flavour, String fullVersionCheckUrl, Context currentApplicationContext, boolean isGuiAvailable) {
        return UpdateUtils.getFlavouredServerVersion(flavour, fullVersionCheckUrl, applicationName, currentApplicationContext, isGuiAvailable);
    }

    public static String[] getServerVersion(String fullVersionCheckUrl, Context currentApplicationContext, boolean isGuiAvailable) {
        return UpdateUtils.getServerVersion(fullVersionCheckUrl, applicationName, currentApplicationContext, isGuiAvailable);
    }
}
