package wumf.com.sharedapps;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import wumf.com.sharedapps.adapter.FindFollowPeopleAdapter;
import wumf.com.sharedapps.eventbus.SearchByNickOrNameOnClickEvent;
import wumf.com.sharedapps.eventbus.SearchByPhoneOnClickEvent;
import wumf.com.sharedapps.eventbus.SearchQueryFirebaseResultEvent;
import wumf.com.sharedapps.firebase.FollowUnfollowPeopleFirebase;
import wumf.com.sharedapps.firebase.SearchQueryFirebase;
import wumf.com.sharedapps.firebase.TransactionResultListener;
import wumf.com.sharedapps.firebase.observable.ObservablePeopleFirebase;
import wumf.com.sharedapps.firebase.pojo.Profile;
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
    private FindFollowPeopleAdapter adapter;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_find_and_follow);
        ((CustomTopBar) findViewById(R.id.top_bar)).setText("Find and follow").bind(this);
        findAndFollowSearchBar = (FindAndFollowSearchBar) findViewById(R.id.find_and_follow_search_bar);
        listView = (ListView) findViewById(R.id.list_view);
        emptyView = (FindAndFollowQueryProgress) findViewById(R.id.find_and_follow_query_progress);
        listView.setEmptyView(emptyView);
        emptyView.setState(FindAndFollowQueryProgress.State.DISABLED);
        adapter = new FindFollowPeopleAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                String uid = adapter.getItem(i).getUid();
                if ( TextUtils.equals(CurrentUser.getUID(), uid) ) {
                    Toast.makeText(view.getContext(), "You can'y follow yourself", Toast.LENGTH_LONG).show();
                    return;
                }
                if ( hasUid(ObservablePeopleFirebase.getPeople(), uid) ) {
                    Toast.makeText(view.getContext(), "You've already follow the person", Toast.LENGTH_LONG).show();
                    return;
                }
                //  Toast.makeText(view.getContext(), "You've already follow the person", Toast.LENGTH_LONG).show();
                FollowUnfollowPeopleFirebase.followPerson(uid, new TransactionResultListener() {
                    @Override
                    public void onSuccess() {
                        ObservablePeopleFirebase.add(adapter.getItem(i));
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onError() {
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                });
            }
        });
    }

    private boolean hasUid(List<Profile> people, String uid) {
        for (Profile person : people) {
            if (TextUtils.equals(person.getUid(), uid)) {
                return true;
            }
        }
        return false;
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
        adapter.clear();
        emptyView.setTopBarHeight(findAndFollowSearchBar.getHeight());
        findAndFollowSearchBar.showProgress();
        emptyView.setState(FindAndFollowQueryProgress.State.IN_PROGRESS,event.query);
        SearchQueryFirebase.searchByName(event.query);
    }

    @Subscribe
    public void onEvent(SearchByPhoneOnClickEvent event) {
        //show progress
        adapter.clear();
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
            adapter.update(event.users);
        }
    }

}
