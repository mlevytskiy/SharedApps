package wumf.com.sharedapps;

import android.app.Application;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import wumf.com.appsprovider.App;
import wumf.com.appsprovider.AppProvider;
import wumf.com.appsprovider.OnChangeLastInstalledAppsListener;
import wumf.com.sharedapps.eventbus.ChangeTop6AppsEvent;

/**
 * Created by max on 01.09.16.
 */
public class MainApplication extends Application {

    public List<App> top6apps;
    public List<App> allApps;
    public GoogleSignInOptions gso;

    public void onCreate() {
        super.onCreate();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        final AppProvider appProvider = AppProvider.instance.setContext(this)
                .setMyAppPackageName(getPackageName())
                .setListener(new OnChangeLastInstalledAppsListener(6) {
                    @Override
                    public void change(List<App> apps) {
                        top6apps = apps;
                        Toast.makeText(getBaseContext(), "apps size=" + apps.size(), Toast.LENGTH_LONG).show();
                        EventBus.getDefault().post(new ChangeTop6AppsEvent(apps));
                    }

                    @Override
                    public void changeOtherApps(List<App> apps) {
                        allApps = new ArrayList<App>();
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
    }

}
