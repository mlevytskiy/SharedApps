package wumf.com.sharedapps.eventbus.observable;

import java.util.List;

import wumf.com.sharedapps.firebase.pojo.Profile;

/**
 * Created by max on 11.01.17.
 */

public class ObservablePeopleEvent {

    public final List<Profile> people;

    public ObservablePeopleEvent(List<Profile> people) {
        this.people = people;
    }

}
