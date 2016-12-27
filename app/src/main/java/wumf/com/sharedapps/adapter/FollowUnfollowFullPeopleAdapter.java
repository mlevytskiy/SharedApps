package wumf.com.sharedapps.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import wumf.com.sharedapps.R;
import wumf.com.sharedapps.firebase.pojo.AppOrFolder;
import wumf.com.sharedapps.firebase.pojo.Profile;
import wumf.com.sharedapps.view.CircularImageView;

/**
 * Created by max on 29.11.16.
 */

public class FollowUnfollowFullPeopleAdapter extends BaseAdapter {

    private List<Profile> users;

    public FollowUnfollowFullPeopleAdapter(List<Profile> users) {
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vh;
        Profile currentUser = users.get(i);
        if (view == null) {
            vh = new ViewHolder();
            view = View.inflate(viewGroup.getContext(), R.layout.item_user, null);
            view.setTag(vh);
            vh.icon = (CircularImageView) view.findViewById(R.id.icon);
            vh.name = (TextView) view.findViewById(R.id.name);
            vh.appIcon1 = (ImageView) view.findViewById(R.id.app_icon1);
            vh.appIcon2 = (ImageView) view.findViewById(R.id.app_icon2);
            vh.appIcon3 = (ImageView) view.findViewById(R.id.app_icon3);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        if ( TextUtils.isEmpty(currentUser.getIcon()) ) {
            vh.icon.setImageDrawable(null);
        } else {
            Glide.with(viewGroup.getContext()).load(currentUser.getIcon()).into(vh.icon);
        }
        vh.name.setText(currentUser.getName());
        fillAppIcons(get3TopAppIcons(currentUser), vh, viewGroup.getContext());
        return view;
    }

    private void fillAppIcons(String[] appIcons, ViewHolder vh, Context context) {
        RelativeLayout.LayoutParams layoutParams =
                (RelativeLayout.LayoutParams) vh.name.getLayoutParams();
        if (appIcons[0] == null && appIcons[1] == null && appIcons[2] == null) {
            vh.name.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        } else {
            vh.name.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, 0);
        }
        setIcon(vh.appIcon1, appIcons[0], context);
        setIcon(vh.appIcon2, appIcons[1], context);
        setIcon(vh.appIcon3, appIcons[2], context);
    }

    private void setIcon(ImageView iv, String icon, Context context) {
        if (TextUtils.isEmpty(icon)) {
            iv.setImageDrawable(null);
        } else {
            Glide.with(context).load(icon).into(iv);
        }
    }

    private String[] get3TopAppIcons(Profile user) {
        List<AppOrFolder> apps;

        if (user.getApps()==null) {
            apps = new ArrayList<>();
        } else {
            apps = new ArrayList<>(user.getApps().values());
            Collections.sort(apps, new Comparator<AppOrFolder>() {
                @Override
                public int compare(AppOrFolder app0, AppOrFolder app1) {
                    if (app0.getTime() < app1.getTime()) {
                        return 1;
                    } else if (app0.getTime() > app1.getTime()) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });
        }

        String[] result = new String[3];

        for (int i = 0; i < 3; i++) {
            result[i] = (i < apps.size()) ? apps.get(i).getIcon() : null ;
        }

        return result;
    }

    private static class ViewHolder {
        CircularImageView icon;
        TextView name;
        ImageView appIcon1;
        ImageView appIcon2;
        ImageView appIcon3;
    }

}
