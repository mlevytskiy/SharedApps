package wumf.com.sharedapps.eventbus;

import wumf.com.sharedapps.firebase.pojo.Profile;

/**
 * Created by max on 30.01.17.
 */

public class PersonOnClickEvent {

    public final Profile profile;

    public PersonOnClickEvent(Profile profile) {
        this.profile = profile;
    }

}
