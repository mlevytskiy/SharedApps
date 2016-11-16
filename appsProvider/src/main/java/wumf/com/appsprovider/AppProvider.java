package wumf.com.appsprovider;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import wumf.com.appsprovider.util.FileGenerator;
import wumf.com.appsprovider.util.SaveIconUtils;

/**
 * Created by max on 02.09.16.
 */
public class AppProvider {

    public final static AppProvider instance = new AppProvider();

    private OnChangeLastInstalledAppsListener listener;
    private Context context;
    private PackageManager pm;
    private String myAppPN;

    private SaveIconUtils saveIconUtils;
    private FileGenerator fileGenerator;
    private List<String> alreadyShareAppPackages = new ArrayList<>();

    private List<App> limitedApps;
    private List<App> otherApps;

    private AppProvider() { }

    public AppProvider setListener(OnChangeLastInstalledAppsListener listener) {
        this.listener = listener;
        return this;
    }

    public AppProvider setMyAppPackageName(String packageName) {
        myAppPN = packageName;
        return this;
    }

    public AppProvider setContext(Context context) {
        this.context = context;
        this.pm = context.getPackageManager();
        saveIconUtils = new SaveIconUtils(context);
        fileGenerator = new FileGenerator(context);
        return this;
    }

    public void updateAlreadySharedAppPackages(List<String> appPackages) {
        alreadyShareAppPackages.clear();
        alreadyShareAppPackages.addAll(appPackages);

        List<App> newLimitedApps = new ArrayList<>();
        List<App> newOtherApps = new ArrayList<>();
        for (App app : limitedApps) {
            if (appPackages.contains(app.appPackage)) {
                //do nothing
            } else {
                newLimitedApps.add(app);
            }
        }

        for (App app : otherApps) {
            if (appPackages.contains(app.appPackage)) {
                //do nothing
            } else {
                newOtherApps.add(app);
            }
        }

        if (newLimitedApps.size() < limitedApps.size()) {
            moveAppsToLimitedApps(limitedApps.size()-newLimitedApps.size(), newLimitedApps, newOtherApps);
            listener.change(limitedApps);
            listener.changeOtherApps(otherApps);
        } else if (newOtherApps.size() != otherApps.size()) {
            otherApps = newOtherApps;
            listener.change(limitedApps);
            listener.changeOtherApps(otherApps);
        } else {
            //do nothing
        }

    }

    private void moveAppsToLimitedApps(int count, List<App> newLimitedApps, List<App> newOtherApps) {
        for (int i = 0; i < count; i++) {
            newLimitedApps.add(newOtherApps.get(0));
            newOtherApps.remove(0);
        }
        limitedApps = newLimitedApps;
        otherApps = newOtherApps;
    }

    public AppProvider initBaseInfo() {
        //load all apps without label and icon
        List<ResolveInfo> resolveInfos = getResolveInfos();
        Map<String, ResolveInfo> map = new HashMap<>();
        List<App> apps = resolveInfoToApp(resolveInfos, map);
        limitedApps = new ArrayList<>();
        otherApps = new ArrayList<>();
        for (int i = apps.size()-1; i >= apps.size()-listener.appsCount; i--) {
            limitedApps.add(apps.get(i));
        }
        for (int i = apps.size()-1-listener.appsCount; i >= 0; i--) {
            otherApps.add(apps.get(i));
        }
        listener.setMap(map);
        listener.change(limitedApps);
        initFullAppInfo(limitedApps, new Runnable() {
                    @Override
                    public void run() {
                        listener.change(limitedApps);
                    }
                },
                otherApps, new Runnable() {
                    @Override
                    public void run() {
                        listener.changeOtherApps(otherApps);
                    }
                });
        return this;
    }

    private AppProvider initFullAppInfo(final List<App> apps, final Runnable finishInit, final List<App> otherApps,
                                        final Runnable otherAppsRunnable) {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                for (App app : apps) {
                    ResolveInfo ri = listener.getMap().get(app.appPackage);
                    app.name = ri.loadLabel(pm).toString();
                    app.iconWithBadQuality = loadAndSaveIconInFile(pm, ri);
                    app.icon = loadAndSaveIconInFileGoodQuality(pm, ri);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                finishInit.run();
            }
        }.execute();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                for (App app : otherApps) {
                    ResolveInfo ri = listener.getMap().get(app.appPackage);
                    app.name = ri.loadLabel(pm).toString();
                    app.iconWithBadQuality = loadAndSaveIconInFile(pm, ri);
                    app.icon = loadAndSaveIconInFileGoodQuality(pm, ri);

                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                otherAppsRunnable.run();
            }

        }.execute();

        return this;
    }

    private List<ResolveInfo> getResolveInfos() {
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        final List<android.content.pm.ResolveInfo> appList = pm.queryIntentActivities(mainIntent, PackageManager.GET_META_DATA);
        return appList;
    }

    private List<App> resolveInfoToApp(List<ResolveInfo> list, Map<String, ResolveInfo> map) {
        List<App> result = new ArrayList<>();
        long systemInstallDate = -1;
        int i = 0;
        for (ResolveInfo resolveInfo : list) {
            if (TextUtils.equals(myAppPN, resolveInfo.activityInfo.packageName)) {
                continue; //skip my app
            } else {
                map.put(resolveInfo.activityInfo.packageName, resolveInfo);
                result.add(resolveInfoToApp(resolveInfo));
            }
        }

        systemInstallDate = systemInstallDate + TimeUnit.MINUTES.toMillis(30);

        for (App app : new ArrayList<>(result)) {
            if (app.installDate < systemInstallDate) {
                result.remove(app); //remove also some system apps
            }
        }

        Collections.sort(result, new Comparator<App>() {
            @Override
            public int compare(App app1, App app2) {
                if (app1.installDate == app2.installDate) {
                    return 0;
                }

                if (app1.installDate > app2.installDate) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });

        return result;
    }

    private App resolveInfoToApp(ResolveInfo resolveInfo) {
        return new App(resolveInfo.activityInfo.name, resolveInfo.activityInfo.packageName,
                null, null, getInstallDate(resolveInfo.activityInfo.packageName));
    }

    private long getInstallDate(String packageName) {
        try {
            PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
            return packageInfo.firstInstallTime;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private String loadAndSaveIconInFileGoodQuality(PackageManager pm, ResolveInfo resolveInfo) {
        return loadAndSaveIconInFileImpl(resolveInfo, true);
    }

    private String loadAndSaveIconInFile(PackageManager pm, ResolveInfo resolveInfo) {
        return loadAndSaveIconInFileImpl(resolveInfo, false);
    }

    private String loadAndSaveIconInFileImpl(ResolveInfo resolveInfo, boolean isGoodQuality) {
        File file;
        if (isGoodQuality) {
            file = fileGenerator.generatePngImage(resolveInfo);
        } else {
            file = fileGenerator.generateImage(resolveInfo);
        }
        if (!file.exists()) {
            Drawable drawable = resolveInfo.loadIcon(pm);
            if (isGoodQuality) {
                saveIconUtils.saveInFileInWithGoodQuality(file, drawable);
            } else {
                saveIconUtils.saveInFile(file, drawable);
            }
        }
        return file.getAbsolutePath();
    }

}
