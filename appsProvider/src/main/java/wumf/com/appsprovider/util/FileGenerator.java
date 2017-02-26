package wumf.com.appsprovider.util;

import android.content.Context;
import android.content.pm.ResolveInfo;

import java.io.File;

/**
 * Created by max on 30.09.15.
 */
public class FileGenerator {

    private Context context;

    public FileGenerator(Context context) {
        this.context = context;
    }

    public File generateImage(ResolveInfo resolveInfo) {
        String mainActivityName = resolveInfo.activityInfo.name.substring(resolveInfo.activityInfo.name.lastIndexOf('.'));
        return new File(context.getFilesDir(), resolveInfo.activityInfo.packageName + mainActivityName + ".image");
    }

    public File generatePngImage(ResolveInfo resolveInfo) {
        String mainActivityName = resolveInfo.activityInfo.name.substring(resolveInfo.activityInfo.name.lastIndexOf('.'));
        return new File(context.getFilesDir(),  resolveInfo.activityInfo.packageName + mainActivityName + ".png");
    }

}
