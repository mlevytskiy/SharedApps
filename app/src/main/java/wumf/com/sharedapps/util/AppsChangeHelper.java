package wumf.com.sharedapps.util;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import wumf.com.sharedapps.eventbus.observable.ObservableChangeProfileEvent;
import wumf.com.sharedapps.eventbus.observable.ObservableGarbageEvent;
import wumf.com.sharedapps.eventbus.observable.ObservablePeopleEvent;
import wumf.com.sharedapps.eventbus.observable.ObservableRemoveProfileEvent;
import wumf.com.sharedapps.firebase.pojo.AppOrFolder;
import wumf.com.sharedapps.firebase.pojo.Profile;

/**
 * Created by max on 13.01.17.
 */

public class AppsChangeHelper {

    public void onEvent(List<Profile> people, List<AppOrFolder> apps, ObservableChangeProfileEvent event) {
        Profile oldProfile = getOldProfile(people, event.profile);
        removeProfileApps(apps, oldProfile);
        addProfileApps(apps, event.profile);
        people.remove(oldProfile);
        people.add(event.profile);
    }

    public void onEvent(List<Profile> people, List<AppOrFolder> apps, ObservableGarbageEvent event) {
        removeProfileApps(apps, event.inGarbage);
        people.removeAll(event.inGarbage);
    }

    public void onEvent(List<Profile> people, List<AppOrFolder> apps, ObservablePeopleEvent event) {
        people.clear();
        people.addAll(event.people);
        apps.clear();
        apps.addAll(getApps(people));
    }

    public void onEvent(List<Profile> people, List<AppOrFolder> apps, ObservableRemoveProfileEvent event) {
        Profile oldProfile = getOldProfile(people, event.uid);
        removeProfileApps(apps, oldProfile);
        people.remove(oldProfile);
    }

    public List<AppOrFolder> getApps(List<Profile> people) {
        List<AppOrFolder> apps = new ArrayList<>();
        if (people == null) {
            return apps;
        }
        Map<String, AppOrFolder> personApps;
        for (Profile profile : people) {
            personApps = profile.getApps();
            if ( personApps != null ) {
                apps.addAll( personApps.values() );
            }
        }
        return apps;
    }

    private Profile getOldProfile(List<Profile> people, Profile newProfile) {
        for (Profile profile : people) {
            if (profile.equals(newProfile)) {
                return profile;
            }
        }
        return null;
    }

    private Profile getOldProfile(List<Profile> people, String uid) {
        for (Profile profile : people) {
            if ( TextUtils.equals(profile.getUid(), uid) ) {
                return profile;
            }
        }
        return null;
    }

    private void removeProfileApps(List<AppOrFolder> apps, Profile profile) {
        if (profile != null) {
            Map<String, AppOrFolder> oldProfileApps = profile.getApps();
            if (oldProfileApps != null) {
                apps.removeAll(oldProfileApps.values());
            }
        }
    }

    private void removeProfileApps(List<AppOrFolder> apps, List<Profile> profiles) {
        for (Profile profile : profiles) {
            removeProfileApps(apps, profile);
        }
    }

    private void addProfileApps(List<AppOrFolder> apps, Profile profile) {
        Map<String, AppOrFolder> newProfileApps = profile.getApps();
        if (newProfileApps != null) {
            apps.addAll(newProfileApps.values());
        }
    }

}
