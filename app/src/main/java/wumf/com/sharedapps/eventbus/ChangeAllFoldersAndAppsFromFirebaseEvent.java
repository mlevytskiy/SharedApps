package wumf.com.sharedapps.eventbus;

import java.util.List;

import wumf.com.sharedapps.firebase.pojo.AppOrFolder;

/**
 * Created by max on 19.10.16.
 */

public class ChangeAllFoldersAndAppsFromFirebaseEvent {

    public final List<String> folders;
    public final List<AppOrFolder> apps;

    public ChangeAllFoldersAndAppsFromFirebaseEvent(List<String> folders, List<AppOrFolder> apps) {
        this.folders = folders;
        this.apps = apps;
    }

}
