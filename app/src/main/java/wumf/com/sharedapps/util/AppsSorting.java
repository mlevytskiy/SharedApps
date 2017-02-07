package wumf.com.sharedapps.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import wumf.com.sharedapps.firebase.pojo.AppOrFolder;

/**
 * Created by max on 06.02.17.
 */

public class AppsSorting {

    public static AppOrFolder[] getSortedArray(List<AppOrFolder> apps, Integer minLength) {
        apps = new ArrayList<>(apps);
        sort(apps);
        return apps.toArray(new AppOrFolder[minLength]);
    }

    public static void sort(List<AppOrFolder> apps) {
        Collections.sort(apps, new Comparator<AppOrFolder>() {
            @Override
            public int compare(AppOrFolder t0, AppOrFolder t1) {
                if (t0.getTimeLong() < t1.getTimeLong()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
    }

}
