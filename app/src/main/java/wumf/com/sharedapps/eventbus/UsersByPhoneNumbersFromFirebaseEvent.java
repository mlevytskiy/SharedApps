package wumf.com.sharedapps.eventbus;

import java.util.List;

import wumf.com.sharedapps.firebase.pojo.Profile;

/**
 * Created by max on 19.12.16.
 */

public class UsersByPhoneNumbersFromFirebaseEvent {

    public final List<Profile> profiles;

    public UsersByPhoneNumbersFromFirebaseEvent(List<Profile> profiles) {
        this.profiles = profiles;
    }

}
