package wumf.com.sharedapps;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import wumf.com.sharedapps.adapter.RemovedPeopleAdapter;
import wumf.com.sharedapps.eventbus.RemovedFollowedUsersChangeEvent;
import wumf.com.sharedapps.firebase.GarbageFirebase;
import wumf.com.sharedapps.view.CustomTopBar;

/**
 * Created by max on 01.01.17.
 */

public class GarbageActivity extends Activity {

    private RemovedPeopleAdapter adapter;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_garbage);
        CustomTopBar topBar = ((CustomTopBar) findViewById(R.id.top_bar)).
                setText("Garbage").bind(this);

        ListView listView = (ListView) findViewById(R.id.list_view);
        View header = View.inflate(this, R.layout.header_restore_all_people, null);
        ImageButton replay = (ImageButton) header.findViewById(R.id.replay_image_button);
        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GarbageFirebase.restoreAllRemovedPeople(CurrentUser.getUID());
                setResult(RESULT_OK);
            }
        });
        listView.addHeaderView(header);

        adapter = new RemovedPeopleAdapter(((MainApplication) getApplication()).removedUsers);

        listView.setAdapter(adapter);
        setResult(RESULT_CANCELED);
    }

    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void onClickReplay(View view) {
        String uid = (String) view.getTag();
        GarbageFirebase.restore(CurrentUser.getUID(), uid);
        setResult(RESULT_OK);
    }

    @Subscribe
    public void onEvent(RemovedFollowedUsersChangeEvent event) {
        adapter.update(((MainApplication) getApplication()).removedUsers);
    }

}
