package wumf.com.sharedapps;

import android.app.Activity;
import android.os.Bundle;

import wumf.com.sharedapps.firebase.pojo.Profile;
import wumf.com.sharedapps.util.AppFinderUtils;
import wumf.com.sharedapps.util.UserFinderUtils;
import wumf.com.sharedapps.view.AppsRecycleView;
import wumf.com.sharedapps.view.CustomTopBar;

/**
 * Created by max on 02.01.17.
 */

public class PersonActivity extends Activity {

    public static final String KEY_USER_UID = "uid";

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_person);
        ((CustomTopBar) findViewById(R.id.top_bar)).bind(this).setText("Person");
        String uid = getIntent().getStringExtra(KEY_USER_UID);
        AppsRecycleView appsRecycleView = (AppsRecycleView) findViewById(R.id.apps_recycle_view);

        Profile user = UserFinderUtils.find(uid);

        appsRecycleView.updateSharedApps(AppFinderUtils.find(user));
    }



}
