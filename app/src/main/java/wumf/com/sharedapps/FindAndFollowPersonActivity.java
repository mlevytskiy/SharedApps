package wumf.com.sharedapps;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import wumf.com.sharedapps.eventbus.SearchByNickOrNameOnClickEvent;
import wumf.com.sharedapps.eventbus.SearchByPhoneOnClickEvent;
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

    @Override
    public void onBackPressed() {
        boolean result = findAndFollowSearchBar.onBackPressed();
        if (!result) {
            super.onBackPressed();
        }
    }

    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(SearchByNickOrNameOnClickEvent event) {
        Toast.makeText(this, "search by nick:" + event.query, Toast.LENGTH_LONG).show();
    }

    @Subscribe
    public void onEvent(SearchByPhoneOnClickEvent event) {
        Toast.makeText(this, "search by phone:" + event.query, Toast.LENGTH_LONG).show();
    }

}
