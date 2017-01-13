package wumf.com.sharedapps.util;

import android.text.TextUtils;

import wumf.com.sharedapps.CurrentUser;

/**
 * Created by max on 08.01.17.
 */

public class PushUtil {

    public static boolean alreadySent(String uid) {
        if (TextUtils.equals(uid, CurrentUser.getUID())) {
            return true;
        }
        return false;
    }

}
