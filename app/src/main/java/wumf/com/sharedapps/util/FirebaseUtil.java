package wumf.com.sharedapps.util;

/**
 * Created by max on 16.11.16.
 */

public class FirebaseUtil {

    public static String createIdFromPackageName(String packageName) {
        return packageName.replace(".", " ");
    }

}
