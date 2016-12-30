package wumf.com.sharedapps.services;

/**
 * Created by max on 27.12.16.
 */

import com.google.firebase.iid.FirebaseInstanceIdService;

import wumf.com.sharedapps.CurrentUser;
import wumf.com.sharedapps.firebase.UsersFirebase;

/**
 * Created by filipp on 5/23/2016.
 */
public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        UsersFirebase.refreshPushId(CurrentUser.getUID());
    }

}
