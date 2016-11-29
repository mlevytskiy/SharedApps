package wumf.com.sharedapps.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import wumf.com.sharedapps.R;

/**
 * Created by max on 29.11.16.
 */

public class FollowUnfollowPeopleAdapter extends BaseAdapter {

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(viewGroup.getContext(), R.layout.item_nobody_use_this_app, null);
        }
        return view;
    }

}
