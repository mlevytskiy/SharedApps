package wumf.com.sharedapps.eventbus;

/**
 * Created by max on 13.01.17.
 */

public class ReceivedPushEvent {

    public final String uid;

    public ReceivedPushEvent(String uid) {
        this.uid = uid;
    }

}
