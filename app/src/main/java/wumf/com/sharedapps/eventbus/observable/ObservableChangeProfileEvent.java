package wumf.com.sharedapps.eventbus.observable;

import wumf.com.sharedapps.firebase.pojo.Profile;

/**
 * Created by max on 11.01.17.
 */

public class ObservableChangeProfileEvent {

    public final Profile profile;

    public ObservableChangeProfileEvent(Profile profile) {
        this.profile = profile;
    }

}
