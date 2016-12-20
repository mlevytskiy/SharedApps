package wumf.com.sharedapps;

import android.app.Activity;
import android.os.Bundle;

import wumf.com.sharedapps.view.CustomTopBar;

/**
 * Created by max on 20.12.16.
 */

public class FindAndFollowPersonActivity extends Activity {

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_find_and_follow);
        ((CustomTopBar) findViewById(R.id.top_bar)).setText("Find and follow").bind(this);
    }
}
