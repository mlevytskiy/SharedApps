package wumf.com.sharedapps;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import interesting.com.contactsprovider.ContactProvider;
import interesting.com.contactsprovider.FinishInitListener;
import wumf.com.appsprovider.App;
import wumf.com.appsprovider.AppProvider;
import wumf.com.appsprovider.OnChangeLastInstalledAppsListener;
import wumf.com.sharedapps.eventbus.ChangeAllFoldersAndAppsFromFirebaseEvent;
import wumf.com.sharedapps.eventbus.ChangeMyTagsEvent;
import wumf.com.sharedapps.eventbus.ChangeTop6AppsEvent;
import wumf.com.sharedapps.eventbus.NewCountryCodeFromFirebaseEvent;
import wumf.com.sharedapps.eventbus.RemovedFollowedUsersChangeEvent;
import wumf.com.sharedapps.eventbus.UsersByPhoneNumbersFromFirebaseEvent;
import wumf.com.sharedapps.firebase.FollowUnfollowPeopleFirebase;
import wumf.com.sharedapps.firebase.GetUsersListener;
import wumf.com.sharedapps.firebase.TransactionResultListener;
import wumf.com.sharedapps.firebase.UsersFirebase;
import wumf.com.sharedapps.firebase.pojo.AppOrFolder;
import wumf.com.sharedapps.firebase.pojo.Profile;
import wumf.com.sharedapps.memory.MemoryCommunicator;
import wumf.com.sharedapps.util.TagsBuilder;

/**
 * Created by max on 01.09.16.
 */
public class MainApplication extends Application {

    private static final String TAG = new TagsBuilder().add("MainApplication").build();

    public List<App> top6apps = new ArrayList<>();
    public List<App> allApps = new ArrayList<>();
    public GoogleSignInOptions gso;
    public String phoneNumber;
    public static MainApplication instance;
    public String country;
    public List<String> myTags;
    public List<Profile> users = new ArrayList<>(); //allUsers-removedUsers =)
    public List<Profile> removedUsers = new ArrayList<>();
    private List<String> removedUserUids = new ArrayList<>();
    private List<Profile> allUsers = new ArrayList<>();

    private AppProvider appProvider;

    public void onCreate() {
        super.onCreate();

        instance = this;

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        appProvider = AppProvider.instance.setContext(this)
                .setMyPackageName(getPackageName())
                .setListener(new OnChangeLastInstalledAppsListener() {
                    @Override
                    public void changedTop6(List<App> apps) {
                        top6apps.clear();
                        top6apps.addAll(apps);
                        EventBus.getDefault().post(new ChangeTop6AppsEvent(apps));
                    }

                    @Override
                    public void changedAll(List<App> apps) {
                        allApps.clear();
                        allApps.addAll(apps);
                    }
                });

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                appProvider.initBaseInfo();
            }
        });

        if (!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }

        country = "UNKNOWN";

        EventBus.getDefault().register(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Subscribe
    public void onEvent(ChangeAllFoldersAndAppsFromFirebaseEvent event) {
        List<String> appPackages = new ArrayList<>();
        for (AppOrFolder app : event.apps) {
            appPackages.add(app.getAppPackage());
        }
        appProvider.updateAlreadySharedApps(appPackages);
    }

    @Subscribe
    public void onEvent(ChangeMyTagsEvent event) {
        myTags = event.tags;
        UsersFirebase.listenCountryCode(CurrentUser.getUID());
    }

    @Subscribe
    public void onEvent(NewCountryCodeFromFirebaseEvent event) {
        Log.i(TAG, "receive new country code event");
        country = event.countryCode;
        if ( !TextUtils.isEmpty(country) ) {
            ContactProvider.instance.init(this, event.countryCode, phoneNumber, new FinishInitListener() {
                @Override
                public void setAll(final List<String> phoneNumbers) {
                    UsersFirebase.getUsers(phoneNumbers, myTags, new GetUsersListener() {
                        @Override
                        public void users(List<Profile> profiles) {
                            users = profiles;
                            allUsers.clear();
                            allUsers.addAll(profiles);
                            remove(users, removedUserUids);
                            EventBus.getDefault().post( new UsersByPhoneNumbersFromFirebaseEvent(users) );
                        }
                    });
                    List<String> oldContacts = new MemoryCommunicator().loadList();
                    List<String> removed = removedContacts(phoneNumbers, oldContacts);
                    List<String> newContacts = newContacts(phoneNumbers, oldContacts);
                    FollowUnfollowPeopleFirebase.markMeAsFollowerOfContacts(CurrentUser.getUID(), newContacts, removed, new TransactionResultListener() {
                        @Override
                        public void onSuccess() {
                            new MemoryCommunicator().saveList(phoneNumbers);
                        }

                        @Override
                        public void onError() {
                            //do nothing
                        }
                    });
                }
            });
        }
    }

    @Subscribe
    public void onEvent(RemovedFollowedUsersChangeEvent event) {
        removedUserUids = event.uids;
        users.clear();
        users.addAll(allUsers);
        remove(users, removedUserUids);
    }

    private void remove(List<Profile> users, List<String> uids) {
        if (uids.isEmpty()) {
            return;
        }

        removedUsers.clear();

        for (int i = 0; i < allUsers.size(); i++) {
            Profile user = allUsers.get(i);
            if ( uids.contains(user.getUid()) ) {
                users.remove(user);
                removedUsers.add(user);
            } else {
                //do nothing
            }
        }
    }

    private List<String> newContacts(List<String> contacts, List<String> oldContacts) {
        if (oldContacts.isEmpty()) {
            return contacts;
        } else {
            List<String> result = new ArrayList<>();
            for (String str : contacts) {
                if ( !oldContacts.contains(str) ) {
                    result.add(str);
                }
            }
            return result;
        }
    }

    private List<String> removedContacts(List<String> contacts, List<String> oldContacts) {
        if (oldContacts.isEmpty()) {
            return oldContacts; //empty list
        } else {
            List<String> result = new ArrayList<>();
            for (String str : oldContacts) {
                if ( !contacts.contains(str) ) {
                    result.add(str);
                }
            }
            return result;
        }
    }

}
