package wumf.com.sharedapps.util;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import wumf.com.appsprovider.App;
import wumf.com.sharedapps.MainApplication;

/**
 * Created by max on 17.09.16.
 */

public class ViberUtils {

    private static final String VIBER_PACKAGE = "com.viber.voip";
    private static String phoneNumber;

    public static boolean hasViber() {
        for (App app : MainApplication.instance.allApps) {
            if (TextUtils.equals(VIBER_PACKAGE, app.appPackage)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasPhoneFromViber(Context context) {

        AccountManager am = AccountManager.get(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        Account[] accounts = am.getAccounts();

        for (Account ac : accounts) {
            String acname = ac.name;
            String actype = ac.type;

            if ( TextUtils.equals(VIBER_PACKAGE, actype) ) {
                phoneNumber = acname;
                return true;
            }
        }

        return false;
    }

    public static String getPhoneNumber() {
        return phoneNumber;
    }

}
