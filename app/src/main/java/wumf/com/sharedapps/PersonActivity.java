package wumf.com.sharedapps;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ns.developer.tagview.widget.TagCloudLinkView;

import java.util.List;

import wumf.com.sharedapps.firebase.pojo.AppOrFolder;
import wumf.com.sharedapps.firebase.pojo.Profile;
import wumf.com.sharedapps.util.AppFinderUtils;
import wumf.com.sharedapps.util.UserFinderUtils;
import wumf.com.sharedapps.view.AppsRecycleView;
import wumf.com.sharedapps.view.CustomTopBar;

/**
 * Created by max on 02.01.17.
 */

public class PersonActivity extends Activity {

    public static final String KEY_USER_UID = "uid";

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_person);
        ((CustomTopBar) findViewById(R.id.top_bar)).bind(this).setText("Person");
        String uid = getIntent().getStringExtra(KEY_USER_UID);
        AppsRecycleView appsRecycleView = (AppsRecycleView) findViewById(R.id.apps_recycle_view);

        Profile user = UserFinderUtils.find(uid);
        showProfileBaseInfo(user);

        List<AppOrFolder> apps = AppFinderUtils.find(user);
        appsRecycleView.updateSharedApps(apps);
    }

    private void showProfileBaseInfo(Profile user) {
        ImageView icon = (ImageView) findViewById(R.id.icon);
        Glide.with(this).load(user.getIcon()).into(icon);

        TextView name = (TextView) findViewById(R.id.name);
        name.setText(user.getName());

        TagCloudLinkView tagCloudLinkView = (TagCloudLinkView) findViewById(R.id.tags_text_view);
        tagCloudLinkView.setAll(user.getMyTags());
    }

}
