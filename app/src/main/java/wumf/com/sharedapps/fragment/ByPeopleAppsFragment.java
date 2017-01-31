package wumf.com.sharedapps.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import wumf.com.sharedapps.IHideShow;
import wumf.com.sharedapps.NotMePersonActivity;
import wumf.com.sharedapps.R;
import wumf.com.sharedapps.eventbus.PersonOnClickEvent;
import wumf.com.sharedapps.eventbus.observable.ObservableChangeProfileEvent;
import wumf.com.sharedapps.eventbus.observable.ObservableGarbageEvent;
import wumf.com.sharedapps.eventbus.observable.ObservablePeopleEvent;
import wumf.com.sharedapps.eventbus.observable.ObservableRemoveProfileEvent;
import wumf.com.sharedapps.firebase.observable.ObservablePeopleFirebase;
import wumf.com.sharedapps.firebase.pojo.Profile;
import wumf.com.sharedapps.view.PeopleRecycleView;

/**
 * Created by max on 14.01.17.
 */

public class ByPeopleAppsFragment extends Fragment implements IHideShow {

    private PeopleRecycleView peopleRecycleView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_by_people_apps, container, false);
        peopleRecycleView = (PeopleRecycleView) view.findViewById(R.id.people_recycle_view);
        peopleRecycleView.update(ObservablePeopleFirebase.getPeople());
        return view;
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
    public void onEvent(ObservableChangeProfileEvent event) {
        peopleRecycleView.update(ObservablePeopleFirebase.getPeople());
    }

    @Subscribe
    public void onEvent(ObservableGarbageEvent event) {
        peopleRecycleView.update(ObservablePeopleFirebase.getPeople());
    }

    @Subscribe
    public void onEvent(ObservablePeopleEvent event) {
        peopleRecycleView.update(ObservablePeopleFirebase.getPeople());
    }

    @Subscribe
    public void onEvent(ObservableRemoveProfileEvent event) {
        peopleRecycleView.update(ObservablePeopleFirebase.getPeople());
    }

    @Subscribe
    public void onEvent(PersonOnClickEvent event) {
        Profile profile = event.profile;
        startActivity(new Intent(getContext(), NotMePersonActivity.class).putExtra(NotMePersonActivity.KEY_USER_UID, profile.getUid()));
    }

    @Override
    public void hide() {
        //do nothing
    }

    @Override
    public void show() {
        //do nothing
    }
}
