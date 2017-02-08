package wumf.com.sharedapps.adapter;

import java.util.ArrayList;
import java.util.List;

import wumf.com.sharedapps.firebase.pojo.Profile;

/**
 * Created by max on 29.01.17.
 */

public class FindFollowPeopleAdapter extends FollowUnfollowFullPeopleAdapter {

    public FindFollowPeopleAdapter() {
        super(new ArrayList<Profile>(), true);
    }

    public void update(List<Profile> people) {
        users.clear();
        users.addAll(people);
        notifyDataSetChanged();
    }

    public void clear() {
        users.clear();
        notifyDataSetChanged();
    }

}
