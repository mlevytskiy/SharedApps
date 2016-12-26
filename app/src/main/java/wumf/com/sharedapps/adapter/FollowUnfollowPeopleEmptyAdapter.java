package wumf.com.sharedapps.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import wumf.com.sharedapps.R;

/**
 * Created by max on 29.11.16.
 */

public class FollowUnfollowPeopleEmptyAdapter extends BaseAdapter {

    private static final int VIEW_TYPE_NOBODY = 0;
    private static final int VIEW_TYPE_NO_PEOPLE_NO_BAGS = 1;

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_NOBODY;
        } else {
            return VIEW_TYPE_NO_PEOPLE_NO_BAGS;
        }
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (getItemViewType(i) == VIEW_TYPE_NOBODY) {
            if (view == null) {
                view = View.inflate(viewGroup.getContext(), R.layout.item_nobody_use_this_app, null);
            }
        } else {
            if (view == null) {
                view = View.inflate(viewGroup.getContext(), R.layout.item_no_people_no_bags, null);
            }
        }
        return view;
    }

}
