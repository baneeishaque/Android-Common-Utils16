package ndk.utils_android16;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

public class AlertDialogUtils {

    YesButtonActions yesButtonActions;
    NoButtonActions noButtonActions;

    public AlertDialogUtils(YesButtonActions yesButtonActions, NoButtonActions noButtonActions) {

        this.yesButtonActions = yesButtonActions;
        this.noButtonActions = noButtonActions;
    }

    public void titledSingleButtonDialogue(Context activityContext, String title, String message, String positiveButtonText, boolean cancelable) {

        getDialogue(activityContext, title, message, positiveButtonText, "", cancelable).show();
    }

    public void titledOkDialogue(Context activityContext, String title, String message, boolean cancelable) {

        titledSingleButtonDialogue(activityContext, title, message, "OK", cancelable);
    }

    public void okDialogue(Context activityContext, String message, boolean cancelable) {

        titledOkDialogue(activityContext, "", message, cancelable);
    }

    public void titledYesNoDialogue(Context activityContext, String title, String message, boolean cancelable) {

        getDialogue(activityContext, title, message, "Yes", "No", cancelable).show();
    }

    public void yesNoDialogue(Context activityContext, String message, boolean cancelable) {

        titledYesNoDialogue(activityContext, "", message, cancelable);
    }

    private AlertDialog getDialogue(Context activityContext, String title, String message, String positiveButtonText, String negativeButtonText, boolean cancelable) {

        AlertDialog.Builder alertDialogue = new AlertDialog.Builder(activityContext);
        alertDialogue
                .setMessage(message)
                .setPositiveButton(positiveButtonText, (dialog, which) -> yesButtonActions.configureYesButtonActions(dialog, which))
                .setCancelable(cancelable);

        if (!negativeButtonText.isEmpty()) {
            alertDialogue.setNegativeButton(negativeButtonText, (dialog, which) -> noButtonActions.configureNoButtonActions(dialog, which));
        }

        if (!title.isEmpty()) {
            alertDialogue.setTitle(title);
        }

        return alertDialogue.create();
    }

    public interface YesButtonActions {
        void configureYesButtonActions(DialogInterface dialog, int which);
    }

    public interface NoButtonActions {
        void configureNoButtonActions(DialogInterface dialog, int which);
    }
}
