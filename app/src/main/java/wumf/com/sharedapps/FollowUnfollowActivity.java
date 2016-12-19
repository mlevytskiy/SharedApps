package wumf.com.sharedapps;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import wumf.com.sharedapps.adapter.FollowUnfollowPeopleAdapter;
import wumf.com.sharedapps.view.CustomTopBar;

/**
 * Created by max on 21.11.16.
 */

public class FollowUnfollowActivity extends Activity {

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_follow_unfollow);
        ((CustomTopBar) findViewById(R.id.top_bar)).setText("Follow/unfollow people").bind(this);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(new FollowUnfollowPeopleAdapter());
        listView.addHeaderView( View.inflate(this, R.layout.header_follow_all_my_phone_contacts, null) );
    }

}
