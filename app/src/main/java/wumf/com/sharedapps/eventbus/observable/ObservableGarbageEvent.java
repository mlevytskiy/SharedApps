package wumf.com.sharedapps.eventbus.observable;

import java.util.List;

import wumf.com.sharedapps.firebase.pojo.Profile;

/**
 * Created by max on 01.01.17.
 */

public class ObservableGarbageEvent {

    public final List<Profile> inGarbage;
    public final List<Profile> people;

    public ObservableGarbageEvent(List<Profile> inGarbage, List<Profile> people) {
        this.inGarbage = inGarbage;
        this.people = people;
    }

}
