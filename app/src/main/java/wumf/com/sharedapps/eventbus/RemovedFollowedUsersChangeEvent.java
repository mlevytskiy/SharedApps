package wumf.com.sharedapps.eventbus;

import java.util.List;

/**
 * Created by max on 01.01.17.
 */

public class RemovedFollowedUsersChangeEvent {

    public final List<String> uids;

    public RemovedFollowedUsersChangeEvent(List<String> uids) {
        this.uids = uids;
    }

}
