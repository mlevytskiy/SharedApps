package wumf.com.sharedapps.util;

import android.text.TextUtils;

import java.util.List;

import wumf.com.sharedapps.firebase.observable.ObservablePeopleFirebase;
import wumf.com.sharedapps.firebase.pojo.Profile;

/**
 * Created by max on 03.01.17.
 */

public class UserFinderUtils {

    public static Profile find(String uid) {
        List<Profile> users = ObservablePeopleFirebase.getPeople();

        for (Profile user : users) {
            if ( TextUtils.equals(uid, user.getUid()) ) {
                return user;
            }
        }

        return null;
    }

}
