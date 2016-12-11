package wumf.com.sharedapps;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.support.multidex.MultiDex;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import wumf.com.appsprovider.App;
import wumf.com.appsprovider.AppProvider;
import wumf.com.appsprovider.OnChangeLastInstalledAppsListener;
import wumf.com.sharedapps.eventbus.ChangeAllFoldersAndAppsFromFirebaseEvent;
import wumf.com.sharedapps.eventbus.ChangeTop6AppsEvent;
import wumf.com.sharedapps.eventbus.NewCountryCodeFromFirebaseEvent;
import wumf.com.sharedapps.firebase.pojo.AppOrFolder;

/**
 * Created by max on 01.09.16.
 */
public class MainApplication extends Application {

    public List<App> top6apps = new ArrayList<>();
    public List<App> allApps = new ArrayList<>();
    public GoogleSignInOptions gso;
    public String phoneNumber;
    public static MainApplication instance;
    public String country;

    private AppProvider appProvider;

    public void onCreate() {
        super.onCreate();

        instance = this;

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        appProvider = AppProvider.instance.setContext(this)
                .setMyAppPackageName(getPackageName())
                .setListener(new OnChangeLastInstalledAppsListener(6) {
                    @Override
                    public void change(List<App> apps) {
                        top6apps.clear();
                        top6apps.addAll(apps);
                        EventBus.getDefault().post(new ChangeTop6AppsEvent(apps));
                    }

                    @Override
                    public void changeOtherApps(List<App> apps) {
                        allApps.clear();
                        allApps.addAll(top6apps);
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
        appProvider.updateAlreadySharedAppPackages(appPackages);
    }

    @Subscribe
    public void onEvent(NewCountryCodeFromFirebaseEvent event) {
        country = event.countryCode;
    }

}
