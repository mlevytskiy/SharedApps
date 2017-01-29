package wumf.com.sharedapps.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import wumf.com.sharedapps.IHideShow;
import wumf.com.sharedapps.R;
import wumf.com.sharedapps.eventbus.observable.ObservableChangeProfileEvent;
import wumf.com.sharedapps.eventbus.observable.ObservableGarbageEvent;
import wumf.com.sharedapps.eventbus.observable.ObservablePeopleEvent;
import wumf.com.sharedapps.eventbus.observable.ObservableRemoveProfileEvent;
import wumf.com.sharedapps.firebase.observable.ObservablePeopleFirebase;
import wumf.com.sharedapps.firebase.pojo.AppOrFolder;
import wumf.com.sharedapps.firebase.pojo.Profile;
import wumf.com.sharedapps.util.AppsChangeHelper;
import wumf.com.sharedapps.view.AppsRecycleView;

/**
 * Created by max on 14.01.17.
 */

public class AllAppsFragment extends Fragment implements IHideShow {

    private AppsRecycleView appsRecycleView;
    private AppsChangeHelper appsChangeHelper;
    private List<AppOrFolder> apps;
    private List<Profile> oldPeople = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_apps, container, false);
        appsRecycleView = (AppsRecycleView) view.findViewById(R.id.apps_recycler_view_in_sub_fragment);
        appsChangeHelper = new AppsChangeHelper();
        return view;
    }

    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        oldPeople.clear();
        List<Profile> people = ObservablePeopleFirebase.getPeople();
        oldPeople.addAll(people);
        apps = appsChangeHelper.getApps(people);
        appsRecycleView.updateSharedApps(apps);
    }

    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(ObservableChangeProfileEvent event) {
        appsChangeHelper.onEvent(oldPeople, apps, event);
        appsRecycleView.updateSharedApps(apps);
    }

    @Subscribe
    public void onEvent(ObservableGarbageEvent event) {
        appsChangeHelper.onEvent(oldPeople, apps, event);
        appsRecycleView.updateSharedApps(apps);
    }

    @Subscribe
    public void onEvent(ObservablePeopleEvent event) {
        appsChangeHelper.onEvent(oldPeople, apps, event);
        appsRecycleView.updateSharedApps(apps);
    }

    @Subscribe
    public void onEvent(ObservableRemoveProfileEvent event) {
        appsChangeHelper.onEvent(oldPeople, apps, event);
        appsRecycleView.updateSharedApps(apps);
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
