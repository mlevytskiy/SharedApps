package wumf.com.sharedapps.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

import org.greenrobot.eventbus.EventBus;

import wumf.com.sharedapps.R;
import wumf.com.sharedapps.eventbus.NewPhoneNumberFromViber;

/**
 * Created by max on 17.09.16.
 */

public class PhoneFromViberDialog {

    public PhoneFromViberDialog(final Activity activity, final String phoneNumber) {
        MaterialDialog d = new MaterialDialog.Builder(activity)
                .content("Your phone number")
                .contentColor(Color.BLACK)
                .theme(Theme.LIGHT)
                .negativeColor(Color.BLACK)
                .positiveText("OK (" + phoneNumber + ")")
                .positiveColorRes(R.color.viber)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        EventBus.getDefault().post(new NewPhoneNumberFromViber(phoneNumber));
                        activity.finish();
                    }
                })
                .negativeText("CANCEL")
                .show();

        d.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                activity.finish();
            }
        });
    }

}
