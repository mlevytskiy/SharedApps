package wumf.com.sharedapps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import wumf.com.sharedapps.eventbus.UsersByPhoneNumbersFromFirebaseEvent;
import wumf.com.sharedapps.firebase.pojo.Profile;
import wumf.com.sharedapps.util.AutofollowTextBuilder;
import wumf.com.sharedapps.view.CustomTopBar;

/**
 * Created by max on 21.11.16.
 */

public class FollowUnfollowActivity extends Activity {

    private boolean isUsersListEmpty = false;
    private List<Profile> users;
    private ListView listView;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_follow_unfollow);
        listView = (ListView) findViewById(R.id.list_view);
        final CustomTopBar customTopBar = ((CustomTopBar) findViewById(R.id.top_bar)).setText("Follow/unfollow people").bind(this)
                .addNewImage(R.drawable.ic_garbage, false, null);

        showUsers(((MainApplication) getApplication()).users);
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
        Toast.makeText(this, "onClickGarbage", Toast.LENGTH_LONG).show();
    }

    private void showUsers(List<Profile> _users) {
        users = _users;
        isUsersListEmpty = (users == Collections.EMPTY_LIST) || users.isEmpty();

        if (isUsersListEmpty) {
            listView.setAdapter(new FollowUnfollowPeopleEmptyAdapter());
        } else {
            listView.setAdapter(new FollowUnfollowFullPeopleAdapter(users));
        }

        createListViewHeader(listView, isUsersListEmpty);
    }

    private void createListViewHeader(ListView listView, boolean isUsersListEmpty) {
        View header = View.inflate(this, R.layout.header_follow_all_my_phone_contacts, null);
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
                startActivity(new Intent(FollowUnfollowActivity.this, FindAndFollowPersonActivity.class));
            }
        });
    }

    @Subscribe
    public void onEvent(UsersByPhoneNumbersFromFirebaseEvent event) {
        showUsers(event.profiles);
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
