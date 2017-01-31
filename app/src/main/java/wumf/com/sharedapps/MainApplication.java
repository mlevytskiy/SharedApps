package wumf.com.sharedapps;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.leakcanary.LeakCanary;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import hugo.weaving.DebugLog;
import interesting.com.contactsprovider.ContactProvider;
import interesting.com.contactsprovider.FinishInitListener;
import wumf.com.appsprovider.App;
import wumf.com.appsprovider.AppProvider;
import wumf.com.appsprovider.OnChangeLastInstalledAppsListener;
import wumf.com.sharedapps.eventbus.ChangeAllFoldersAndAppsFromFirebaseEvent;
import wumf.com.sharedapps.eventbus.ChangeMyTagsEvent;
import wumf.com.sharedapps.eventbus.ChangeTop6AppsEvent;
import wumf.com.sharedapps.eventbus.NewCountryCodeFromFirebaseEvent;
import wumf.com.sharedapps.eventbus.NewPhoneNumberFromFirebaseEvent;
import wumf.com.sharedapps.firebase.FollowUnfollowPeopleFirebase;
import wumf.com.sharedapps.firebase.UsersFirebase;
import wumf.com.sharedapps.firebase.observable.ObservablePeopleFirebase;
import wumf.com.sharedapps.firebase.pojo.AppOrFolder;
import wumf.com.sharedapps.memory.Key;
import wumf.com.sharedapps.memory.MemoryCommunicator;

/**
 * Created by max on 01.09.16.
 */
@DebugLog
public class MainApplication extends Application {

    public List<App> top6apps = new ArrayList<>();
    public List<App> allApps = new ArrayList<>();
    public GoogleSignInOptions gso;
    public String phoneNumber;
    public static MainApplication instance;
    public String country;
    public List<String> myTags;

    private AppProvider appProvider;
    private MemoryCommunicator memory;

    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        instance = this;
        memory = MemoryCommunicator.getInstance();
        myTags = memory.loadList(Key.oldTags);
        phoneNumber = memory.loadStr(Key.myPhone);

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

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        country = "UNKNOWN";

        EventBus.getDefault().register(this);

        OnShowOrHideAppListener listener = new OnShowOrHideAppListener() {
            @Override
            public void onHide() {
                //do nothing
            }

            @Override
            public void onShow() {
                //do nothing
            }
        };

        registerActivityLifecycleCallbacks(listener);
        registerComponentCallbacks(listener);

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

    @DebugLog
    @Subscribe
    public void onEvent(ChangeMyTagsEvent event) {
        List<String> oldTags = myTags;
        myTags = event.tags;
        memory.saveList(myTags, Key.oldTags);
        UsersFirebase.listenCountryCode(CurrentUser.getUID());
        List<String> newTags = getNewTags(myTags, oldTags);
        FollowUnfollowPeopleFirebase.sendPushesPeopleWithTheSameTags(newTags);
    }

    @Subscribe
    public void onEvent(NewPhoneNumberFromFirebaseEvent event) {
        if (TextUtils.equals(phoneNumber, event.phone)) {
            return;
        }
        phoneNumber = event.phone;
        FollowUnfollowPeopleFirebase.sendPushesPeopleWhoWaitingMe(phoneNumber);
        memory.saveStr(phoneNumber, Key.myPhone);
    }

    @Subscribe
    public void onEvent(NewCountryCodeFromFirebaseEvent event) {
        country = event.countryCode;

        if (!TextUtils.isEmpty(country)) {
            ContactProvider.instance.init(this, country, MainApplication.instance.phoneNumber, new FinishInitListener() {
                @Override
                public void setAll(List<String> phoneNumbers) {
                    ObservablePeopleFirebase.setPhoneNumbers(phoneNumbers);
                    ObservablePeopleFirebase.listenPeople(CurrentUser.getUID());
                }
            });
        }

    }

    private List<String> getNewTags(List<String> newTagList, List<String> oldTagList) {
        if (newTagList == null || newTagList.isEmpty()) {
            return new ArrayList<>();
        }

        if (oldTagList == null || oldTagList.isEmpty()) {
            return newTagList;
        }

        List<String> result = new ArrayList<>();
        for (String tag : newTagList) {
            if ( !oldTagList.contains(tag) ) {
                result.add(tag);
            }
        }
        return result;
    }

    public void signOut() {
        phoneNumber = null;
        myTags.clear();
    }

}
