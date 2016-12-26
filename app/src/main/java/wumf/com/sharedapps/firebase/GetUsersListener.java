package wumf.com.sharedapps.firebase;

import java.util.List;

import wumf.com.sharedapps.firebase.pojo.Profile;

/**
 * Created by max on 26.12.16.
 */

public interface GetUsersListener {

    void users(List<Profile> profiles);

}
