package wumf.com.sharedapps;

import com.google.firebase.auth.FirebaseUser;

import org.greenrobot.eventbus.EventBus;

import wumf.com.sharedapps.eventbus.CurrentUserChangedEvent;
import wumf.com.sharedapps.firebase.FavouriteAppsFirebase;
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

        }

        currentUser = user;

        EventBus.getDefault().post(new CurrentUserChangedEvent(currentUser));
    }

    public static String getUID() {
        if (currentUser == null) {
            return null;
        } else {
            return currentUser.getUid();
        }
    }

}
