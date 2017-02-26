package wumf.com.sharedapps.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

import wumf.com.sharedapps.R;

/**
 * Created by max on 17.09.16.
 */

public class ExitDialog {

    public ExitDialog(final Activity activity) {
        MaterialDialog d = new MaterialDialog.Builder(activity)
                .content("Do you want exit?")
                .contentColor(Color.BLACK)
                .theme(Theme.LIGHT)
                .negativeColor(Color.BLACK)
                .positiveText("EXIT")
                .positiveColorRes(R.color.exit)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.cancel();
                        activity.finish();
                    }
                })
                .negativeText("CANCEL")
                .show();

        d.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                dialogInterface.cancel();
            }
        });
    }

}
