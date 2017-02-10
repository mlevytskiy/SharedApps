package wumf.com.sharedapps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Collections;
import java.util.List;

import wumf.com.sharedapps.adapter.FollowUnfollowFullPeopleAdapter;
import wumf.com.sharedapps.adapter.FollowUnfollowPeopleEmptyAdapter;
import wumf.com.sharedapps.eventbus.observable.ObservableChangeProfileEvent;
import wumf.com.sharedapps.eventbus.observable.ObservableGarbageEvent;
import wumf.com.sharedapps.eventbus.observable.ObservablePeopleEvent;
import wumf.com.sharedapps.eventbus.observable.ObservableRemoveProfileEvent;
import wumf.com.sharedapps.firebase.GarbageFirebase;
import wumf.com.sharedapps.firebase.observable.ObservablePeopleFirebase;
import wumf.com.sharedapps.firebase.pojo.Profile;
import wumf.com.sharedapps.memory.Key;
import wumf.com.sharedapps.memory.MemoryCommunicator;
import wumf.com.sharedapps.util.AutofollowTextBuilder;
import wumf.com.sharedapps.view.CustomTopBar;

/**
 * Created by max on 21.11.16.
 */

public class FollowUnfollowActivity extends Activity {

    private static final int REQUEST_CODE_GARBAGE = 888;
    private static final int REQUEST_CODE_FOLLOW_PERSON = 889;
    private boolean isUsersListEmpty = false;
    private List<Profile> users;
    private ListView listView;
    private View header;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_follow_unfollow);
        listView = (ListView) findViewById(R.id.list_view);
        final CustomTopBar customTopBar = ((CustomTopBar) findViewById(R.id.top_bar)).setText("Follow/unfollow people").bind(this);

        if (MemoryCommunicator.getInstance().loadBoolean(Key.isNeedGarbageIcon)) {
            customTopBar.addNewImage(R.drawable.ic_garbage, false, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivityForResult(new Intent(FollowUnfollowActivity.this, GarbageActivity.class), REQUEST_CODE_GARBAGE);
                }
            });
        }

        showUsers(ObservablePeopleFirebase.getPeople());
    }

    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void onClickGarbage(View view) {
        String uid = (String) view.getTag();
        GarbageFirebase.moveUserToGarbage(CurrentUser.getUID(), uid);
    }

    @Subscribe
    public void onEvent(ObservableGarbageEvent event) {
        showUsers(event.people);
    }

    @Subscribe
    public void onEvent(ObservablePeopleEvent event) {
        showUsers(event.people);
    }

    @Subscribe
    public void onEvent(ObservableChangeProfileEvent event) {
        showUsers(ObservablePeopleFirebase.getPeople());
    }

    @Subscribe
    public void onEvent(ObservableRemoveProfileEvent event) {
        showUsers(ObservablePeopleFirebase.getPeople());
    }

    private void showUsers(List<Profile> _users) {
        users = _users;
        isUsersListEmpty = (users == Collections.EMPTY_LIST) || users.isEmpty();
        listView.removeHeaderView(header);
        createListViewHeader(listView, isUsersListEmpty);
        if (isUsersListEmpty) {
            listView.setAdapter(new FollowUnfollowPeopleEmptyAdapter());
            listView.setOnItemClickListener(null);
        } else {
            listView.setAdapter(new FollowUnfollowFullPeopleAdapter(users));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String uid = users.get(i - listView.getHeaderViewsCount()).getUid();
                    startActivity(new Intent(FollowUnfollowActivity.this, NotMePersonActivity.class).putExtra(NotMePersonActivity.KEY_USER_UID, uid));
                }
            });
        }
    }

    private void createListViewHeader(ListView listView, boolean isUsersListEmpty) {
        header = View.inflate(this, R.layout.header_follow_all_my_phone_contacts, null);
        header.findViewById(R.id.line).setVisibility(isUsersListEmpty ? View.VISIBLE : View.GONE);
        TextView autofollow = (TextView) header.findViewById(R.id.autofollow_typeface_text_view);
        autofollowCheckBoxClick(autofollow, (CheckBox) header.findViewById(R.id.autofollow_check_box));
        String autofollowText = new AutofollowTextBuilder().setTags(( (MainApplication) getApplication() ).myTags).build();
        autofollow.setText(autofollowText);
        listView.addHeaderView(header);
        ImageButton findImageButton = (ImageButton) header.findViewById(R.id.find_image_button);
        findImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(FollowUnfollowActivity.this, FindAndFollowPersonActivity.class), REQUEST_CODE_FOLLOW_PERSON);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_GARBAGE || requestCode == REQUEST_CODE_FOLLOW_PERSON) {
            if (resultCode == RESULT_OK) {
                showUsers(ObservablePeopleFirebase.getPeople());
            } else {
                //do nothing
            }
        }
    }

    private void autofollowCheckBoxClick(TextView textView, CheckBox checkBox) {
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if ( !checked ) {
                    compoundButton.setChecked(true);
                    showMessageThisOptionDontWorkYet();
                }
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessageThisOptionDontWorkYet();
            }
        });
    }

    private void showMessageThisOptionDontWorkYet() {
        Toast.makeText(this, "This option don't work yet", Toast.LENGTH_LONG).show();
    }

}
