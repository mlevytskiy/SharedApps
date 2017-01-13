package wumf.com.sharedapps.eventbus;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by max on 12.01.17.
 */

public class CurrentUserChangedEvent {

    public final FirebaseUser firebaseUser;

    public CurrentUserChangedEvent(FirebaseUser firebaseUser) {
        this.firebaseUser = firebaseUser;
    }

}
