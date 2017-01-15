package wumf.com.sharedapps.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import wumf.com.sharedapps.R;
import wumf.com.sharedapps.firebase.pojo.AppOrFolder;
import wumf.com.sharedapps.firebase.pojo.Profile;

/**
 * Created by max on 14.01.17.
 */

public class PersonViewHolder extends RecyclerView.ViewHolder {

    private Context context;
    private ImageView personImageView;
    private TextView personNameTextView;
    private ImageView[] appIcons = new ImageView[6];

    public PersonViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        personImageView = (ImageView) itemView.findViewById(R.id.person_icon);
        personNameTextView = (TextView) itemView.findViewById(R.id.person_name);
        appIcons[0] = (ImageView) itemView.findViewById(R.id.person_app_icon1);
        appIcons[1] = (ImageView) itemView.findViewById(R.id.person_app_icon2);
        appIcons[2] = (ImageView) itemView.findViewById(R.id.person_app_icon3);
        appIcons[3] = (ImageView) itemView.findViewById(R.id.person_app_icon4);
        appIcons[4] = (ImageView) itemView.findViewById(R.id.person_app_icon5);
        appIcons[5] = (ImageView) itemView.findViewById(R.id.person_app_icon6);
    }

    public void bind(Profile profile) {
        personNameTextView.setText(profile.getName());
        Glide.with(context).load(profile.getIcon()).into(personImageView);
        Map<String, AppOrFolder> appsMap = profile.getApps();
        Collection<AppOrFolder> appsCollection = appsMap.values();
        List<AppOrFolder> apps;
        int appsSize;
        if (appsMap == null) {
            appsSize = 0;
            apps = new ArrayList<>();
        } else {
            appsSize = appsMap.size();
            apps = new ArrayList<>(appsCollection);
        }
        for (int i = 0; i < appIcons.length; i++) {
            if (i < appsSize) {
                Glide.with(context).load(apps.get(i).getIcon()).into(appIcons[i]);
            } else {
                appIcons[i].setImageDrawable(null);
            }
        }
    }

}
