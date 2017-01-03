package wumf.com.sharedapps.util;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import wumf.com.appsprovider.App;
import wumf.com.sharedapps.MainApplication;
import wumf.com.sharedapps.firebase.pojo.AppOrFolder;
import wumf.com.sharedapps.firebase.pojo.Profile;

/**
 * Created by max on 16.11.16.
 */

public class AppFinderUtils {

    public static App find(String packageName) {
        if (MainApplication.instance.top6apps == null) {
            return null;
        }

        for (App item : MainApplication.instance.top6apps) {
            if ( TextUtils.equals(item.appPackage, packageName) ) {
                return item;
            }
        }

        if (MainApplication.instance.allApps == null) {
            return null;
        }

        for (App item : MainApplication.instance.allApps) {
            if ( TextUtils.equals(item.appPackage, packageName) ) {
                return item;
            }
        }

        return null;
    }

    public static List<AppOrFolder> find(Profile user) {
        if (user == null) {
            return new ArrayList<>();
        } else {
            return new ArrayList<>(user.getApps().values());
        }
    }

}
