package wumf.com.sharedapps.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import wumf.com.sharedapps.R;
import wumf.com.sharedapps.eventbus.observable.ObservableChangeProfileEvent;
import wumf.com.sharedapps.eventbus.observable.ObservableGarbageEvent;
import wumf.com.sharedapps.eventbus.observable.ObservablePeopleEvent;
import wumf.com.sharedapps.eventbus.observable.ObservableRemoveProfileEvent;
import wumf.com.sharedapps.firebase.observable.ObservablePeopleFirebase;
import wumf.com.sharedapps.view.PeopleRecycleView;

/**
 * Created by max on 14.01.17.
 */

public class ByPeopleAppsFragment extends Fragment {

    private PeopleRecycleView peopleRecycleView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_by_people_apps, container, false);
        peopleRecycleView = (PeopleRecycleView) view.findViewById(R.id.people_recycle_view);
        return view;
    }

    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        peopleRecycleView.update(ObservablePeopleFirebase.getPeople());
    }

    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(ObservableChangeProfileEvent event) {

    }

    @Subscribe
    public void onEvent(ObservableGarbageEvent event) {

    }

    @Subscribe
    public void onEvent(ObservablePeopleEvent event) {

    }

    @Subscribe
    public void onEvent(ObservableRemoveProfileEvent event) {

    }

}
