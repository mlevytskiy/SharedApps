package wumf.com.sharedapps.util;

import android.text.TextUtils;

import wumf.com.sharedapps.CurrentUser;
import wumf.com.sharedapps.MainApplication;

/**
 * Created by max on 08.01.17.
 */

public class PushUtil {

    public static boolean alreadySent(String uid) {
        if (TextUtils.equals(uid, CurrentUser.getUID())) {
            return true;
        }
        if (MainApplication.instance.alreadySentUids.contains(uid)) {
            return true;
        }
        return false;
    }

    public static void add(String uid) {
        MainApplication.instance.alreadySentUids.add(uid);
    }

}
