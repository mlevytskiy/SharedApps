package wumf.com.sharedapps.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wumf.com.sharedapps.R;
import wumf.com.sharedapps.firebase.pojo.Profile;

/**
 * Created by max on 29.01.17.
 */

public class FindFollowPeopleAdapter extends BaseAdapter {

    private List<Profile> people = new ArrayList<>();

    @Override
    public int getCount() {
        return people.size();
    }

    @Override
    public Profile getItem(int i) {
        return people.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void update(List<Profile> users) {
        people.clear();
        people.addAll(users);
        notifyDataSetChanged();
    }

    public void clear() {
        people.clear();
        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vh;
        if (view == null) {
            view = View.inflate(viewGroup.getContext(), R.layout.item_find_and_follow_user, null);
            vh = new ViewHolder(view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        vh.fill(people.get(i));
        return view;
    }


    private static class ViewHolder {

        private TextView nameTextView;
        private TextView emailTextView;
        private TextView phoneTextView;

        public ViewHolder(View view) {
            nameTextView = (TextView) view.findViewById(R.id.name);
            emailTextView = (TextView) view.findViewById(R.id.email);
            phoneTextView = (TextView) view.findViewById(R.id.phone_number);
        }

        public void fill(Profile user) {
            nameTextView.setText(user.getName());
            emailTextView.setText(user.getEmail());
            String phoneNumber = user.getPhoneNumber();
            phoneTextView.setText(TextUtils.isEmpty(phoneNumber) ? "" : phoneNumber);
        }

    }


}
