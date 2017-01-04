package wumf.com.sharedapps;

import com.google.firebase.auth.FirebaseUser;

import wumf.com.sharedapps.firebase.FavouriteAppsFirebase;
import wumf.com.sharedapps.firebase.GarbageFirebase;
import wumf.com.sharedapps.firebase.TagsFirebase;
import wumf.com.sharedapps.firebase.UsersFirebase;

/**
 * Created by max on 30.12.16.
 */

public class CurrentUser {

    private static FirebaseUser currentUser;

    public static FirebaseUser get() {
        return currentUser;
    }

    public static void set(FirebaseUser user) {
        if (currentUser == null && user != null) {

            String uid = user.getUid();
            TagsFirebase.listenMyTags(uid);
            UsersFirebase.listenPhoneNumber(uid);
            FavouriteAppsFirebase.listenFoldersAndApps(uid);
            GarbageFirebase.listenAndNotify(uid);

        }

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
