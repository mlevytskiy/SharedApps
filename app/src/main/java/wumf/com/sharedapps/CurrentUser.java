package wumf.com.sharedapps;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by max on 30.12.16.
 */

public class CurrentUser {

    private static FirebaseUser currentUser;

    public static FirebaseUser get() {
        return currentUser;
    }

    public static void set(FirebaseUser user) {
        currentUser = user;
    }

    public static String getUID() {
        if (currentUser == null) {
            return "currentUser == null";
        } else {
            return currentUser.getUid();
        }
    }

}
