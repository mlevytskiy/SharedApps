package wumf.com.sharedapps;

/**
 * Created by max on 27.12.16.
 */

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import wumf.com.sharedapps.util.TagsBuilder;

/**
 * Created by filipp on 5/23/2016.
 */
public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = new TagsBuilder().add("FirebaseInstanceIDService").add("push").build();

    @Override
    public void onTokenRefresh() {

        String token = FirebaseInstanceId.getInstance().getToken();

        registerToken(token);
    }

    private void registerToken(String token) {
        Log.i(TAG, "token=" + token);
    }
}
