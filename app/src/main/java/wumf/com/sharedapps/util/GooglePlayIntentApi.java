package wumf.com.sharedapps.util;

import android.content.Intent;
import android.net.Uri;

/**
 * Created by max on 01.02.17.
 */

public class GooglePlayIntentApi {

    public static Intent getOpenAppPageIntent(String appPackageName) {
        try {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName));
        } catch (android.content.ActivityNotFoundException e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName));
        }
    }
}
