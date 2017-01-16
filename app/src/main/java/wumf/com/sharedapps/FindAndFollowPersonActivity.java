package wumf.com.sharedapps;

import android.app.Activity;
import android.os.Bundle;

import wumf.com.sharedapps.view.CustomTopBar;
import wumf.com.sharedapps.view.FindAndFollowSearchBar;

/**
 * Created by max on 20.12.16.
 */

public class FindAndFollowPersonActivity extends Activity {

    private FindAndFollowSearchBar findAndFollowSearchBar;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_find_and_follow);
        ((CustomTopBar) findViewById(R.id.top_bar)).setText("Find and follow").bind(this);
        findAndFollowSearchBar = (FindAndFollowSearchBar) findViewById(R.id.find_and_follow_search_bar);
    }

    public void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        boolean result = findAndFollowSearchBar.onBackPressed();
        if (!result) {
            super.onBackPressed();
        }
    }

}
