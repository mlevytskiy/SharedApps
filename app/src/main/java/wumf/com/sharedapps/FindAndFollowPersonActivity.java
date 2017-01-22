package wumf.com.sharedapps;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import wumf.com.sharedapps.eventbus.SearchByNickOrNameOnClickEvent;
import wumf.com.sharedapps.eventbus.SearchByPhoneOnClickEvent;
import wumf.com.sharedapps.eventbus.SearchQueryFirebaseResultEvent;
import wumf.com.sharedapps.firebase.SearchQueryFirebase;
import wumf.com.sharedapps.view.CustomTopBar;
import wumf.com.sharedapps.view.FindAndFollowQueryProgress;
import wumf.com.sharedapps.view.FindAndFollowSearchBar;

/**
 * Created by max on 20.12.16.
 */

public class FindAndFollowPersonActivity extends Activity {

    private FindAndFollowSearchBar findAndFollowSearchBar;
    private ListView listView;
    private FindAndFollowQueryProgress emptyView;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_find_and_follow);
        ((CustomTopBar) findViewById(R.id.top_bar)).setText("Find and follow").bind(this);
        findAndFollowSearchBar = (FindAndFollowSearchBar) findViewById(R.id.find_and_follow_search_bar);
        listView = (ListView) findViewById(R.id.list_view);
        emptyView = (FindAndFollowQueryProgress) findViewById(R.id.find_and_follow_query_progress);
        listView.setEmptyView(emptyView);
        emptyView.setState(FindAndFollowQueryProgress.State.DISABLED);
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
        //show progress
        emptyView.setTopBarHeight(findAndFollowSearchBar.getHeight());
        findAndFollowSearchBar.showProgress();
        emptyView.setState(FindAndFollowQueryProgress.State.IN_PROGRESS,event.query);
        SearchQueryFirebase.searchByName(event.query);
    }

    @Subscribe
    public void onEvent(SearchByPhoneOnClickEvent event) {
        //show progress
        emptyView.setTopBarHeight(findAndFollowSearchBar.getHeight());
        findAndFollowSearchBar.showProgress();
        emptyView.setState(FindAndFollowQueryProgress.State.IN_PROGRESS,event.query);
        SearchQueryFirebase.searchByPhone(event.query);
    }

    @Subscribe
    public void onEvent(SearchQueryFirebaseResultEvent event) {
        findAndFollowSearchBar.hideProgress();
        if (event.hasError) {
            emptyView.setState(FindAndFollowQueryProgress.State.ERROR, "Some error!");
        } else {
            emptyView.setState(FindAndFollowQueryProgress.State.EMPTY_RESULT, "We find nobody");
            //show list
        }
    }

}
