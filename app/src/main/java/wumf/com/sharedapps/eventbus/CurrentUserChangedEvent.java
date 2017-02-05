package wumf.com.sharedapps.eventbus;

/**
 * Created by max on 12.01.17.
 */

public class CurrentUserChangedEvent {

    public final String userUid;

    public CurrentUserChangedEvent(String userUid) {
        this.userUid = userUid;
    }

}
