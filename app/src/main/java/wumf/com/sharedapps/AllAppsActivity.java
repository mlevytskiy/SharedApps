package wumf.com.sharedapps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.omadahealth.typefaceview.TypefaceTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import wumf.com.appsprovider.App;
import wumf.com.sharedapps.adapter.AllAppsAdapter;
import wumf.com.sharedapps.eventbus.OnClickAppEvent;

/**
 * Created by max on 12.09.16.
 */
public class AllAppsActivity extends Activity {

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_all_apps);
        setResult(RESULT_CANCELED);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        List<App> apps = ((MainApplication) getApplication()).allApps;
        if (apps != null) {
            recyclerView.setAdapter(new AllAppsAdapter(apps));
        } else {
            //do nothing
        }

        TypefaceTextView close = (TypefaceTextView) findViewById(R.id.close);
        close.setTextIsSelectable(false);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(OnClickAppEvent event) {
        Intent data = new Intent();
        data.putExtra(MainActivity.PACKAGE_NAME, event.appPackage);
        setResult(RESULT_OK, data);
        finish();
    }

}
