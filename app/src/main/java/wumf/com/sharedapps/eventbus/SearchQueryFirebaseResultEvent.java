package wumf.com.sharedapps.eventbus;

import java.util.ArrayList;
import java.util.List;

import wumf.com.sharedapps.firebase.pojo.Profile;

/**
 * Created by max on 18.01.17.
 */

public class SearchQueryFirebaseResultEvent {

    public final List<Profile> users;
    public boolean hasError = false;

    public SearchQueryFirebaseResultEvent(List<Profile> users) {
        this.users = users;
    }

    public SearchQueryFirebaseResultEvent() {
        hasError = true;
        users = new ArrayList<>();
    }

}
