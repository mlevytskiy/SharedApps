package wumf.com.sharedapps.eventbus.observable;

/**
 * Created by max on 11.01.17.
 */

public class ObservableRemoveProfileEvent {

    public final String uid;

    public ObservableRemoveProfileEvent(String uid) {
        this.uid = uid;
    }

}
