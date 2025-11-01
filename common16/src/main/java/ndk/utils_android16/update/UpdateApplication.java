package ndk.utils_android16.update;

import androidx.appcompat.app.AppCompatActivity;

import ndk.utils_android14.ApplicationVCSUtils;
import ndk.utils_android16.AlertDialogUtils;

public class UpdateApplication {

    public static void updateApplication(final String applicationName, final AppCompatActivity appCompatActivity, final float versionName, final String updateUrl) {

        new AlertDialogUtils(
                (dialog, which) -> ApplicationVCSUtils.downloadAndInstallApk(applicationName, versionName, updateUrl, appCompatActivity),
                null
        ).titledSingleButtonDialogue(appCompatActivity, "Warning!", "New version is available, please update...", "Update", false);
    }
}
