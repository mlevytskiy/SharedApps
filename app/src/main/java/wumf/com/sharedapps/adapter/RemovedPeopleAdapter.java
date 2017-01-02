package wumf.com.sharedapps.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import wumf.com.sharedapps.R;
import wumf.com.sharedapps.firebase.pojo.Profile;

/**
 * Created by max on 29.11.16.
 */

public class RemovedPeopleAdapter extends BaseAdapter {

    private List<Profile> users;

    public RemovedPeopleAdapter(List<Profile> users) {
        this.users = users;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void update(List<Profile> users) {
        this.users.clear();
        this.users.addAll(users);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vh;
        Profile currentUser = users.get(i);
        if (view == null) {
            vh = new ViewHolder();
            view = View.inflate(viewGroup.getContext(), R.layout.item_removed_user, null);
            view.setTag(vh);
            vh.icon = (ImageView) view.findViewById(R.id.icon);
            vh.name = (TextView) view.findViewById(R.id.name);
            vh.replay = (ImageButton) view.findViewById(R.id.replay);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        vh.replay.setTag(currentUser.getUid());
        if ( TextUtils.isEmpty(currentUser.getIcon()) ) {
            vh.icon.setImageDrawable(null);
        } else {
            Glide.with(viewGroup.getContext()).load(currentUser.getIcon()).into(vh.icon);
        }
        vh.name.setText(currentUser.getName());
        return view;
    }

    private static class ViewHolder {
        ImageView icon;
        TextView name;
        ImageButton replay;
    }

}
