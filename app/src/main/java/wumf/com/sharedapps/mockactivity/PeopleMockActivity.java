package wumf.com.sharedapps.mockactivity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import interesting.com.contactsprovider.ContactProvider;
import interesting.com.contactsprovider.FinishInitListener;
import wumf.com.sharedapps.MainApplication;
import wumf.com.sharedapps.R;
import wumf.com.sharedapps.eventbus.observable.ObservableChangeProfileEvent;
import wumf.com.sharedapps.eventbus.observable.ObservablePeopleEvent;
import wumf.com.sharedapps.eventbus.observable.ObservableRemoveProfileEvent;
import wumf.com.sharedapps.firebase.observable.ObservablePeopleFirebase;
import wumf.com.sharedapps.firebase.pojo.Profile;

/**
 * Created by max on 11.01.17.
 */

public class PeopleMockActivity extends Activity {

    private TextView tv;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.mock_activity_people);
        tv = (TextView) findViewById(R.id.text_view);
    }

    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        show(ObservablePeopleFirebase.getPeople());
        if (!TextUtils.isEmpty(MainApplication.instance.country)) {
            ContactProvider.instance.init(this, MainApplication.instance.country, MainApplication.instance.phoneNumber, new FinishInitListener() {
                @Override
                public void setAll(List<String> phoneNumbers) {
                    ObservablePeopleFirebase.setPhoneNumbers(phoneNumbers);
                    ObservablePeopleFirebase.listenPeople("yovpJvDQHygJAyAu4wmvFlJ5wwu2");
                }
            });
        }
    }

    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(ObservablePeopleEvent event) {
        show(event.people);
    }

    @Subscribe
    public void onEvent(ObservableChangeProfileEvent event) {
        Toast.makeText(this, "change uid=" + event.profile.getUid(), Toast.LENGTH_LONG).show();
    }

    public void onEvent(ObservableRemoveProfileEvent event) {
        Toast.makeText(this, "remove uid=" + event.uid, Toast.LENGTH_LONG).show();
    }

    private void show(List<Profile> people) {
        List<String> uids = new ArrayList<>();
        for (Profile profile : people) {
            uids.add(profile.getUid());
        }
        tv.setText(TextUtils.join("\n", uids));
    }

}
