package wumf.com.sharedapps.mockactivity;

import android.app.Activity;
import android.os.Bundle;

import wumf.com.sharedapps.firebase.UsersFirebase;

/**
 * Created by max on 19.12.16.
 */

public class TestFirebaseQueryMockActivity extends Activity {

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        UsersFirebase.getUsers();
    }
}
