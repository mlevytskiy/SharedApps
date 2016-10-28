package wumf.com.sharedapps.eventbus;

import java.util.List;

/**
 * Created by max on 19.10.16.
 */

public class ChangeAllFoldersFromFirebaseEvent {

    public final List<String> folders;

    public ChangeAllFoldersFromFirebaseEvent(List<String> folders) {
        this.folders = folders;
    }

}
