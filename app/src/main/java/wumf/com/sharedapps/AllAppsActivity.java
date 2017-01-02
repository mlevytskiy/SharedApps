package wumf.com.sharedapps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.github.omadahealth.typefaceview.TypefaceTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import wumf.com.sharedapps.eventbus.OnClickAppEvent;
import wumf.com.sharedapps.view.AppsRecycleView;

/**
 * Created by max on 12.09.16.
 */
public class AllAppsActivity extends Activity {

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_all_apps);
        setResult(RESULT_CANCELED);
        AppsRecycleView appsRecycleView = (AppsRecycleView) findViewById(R.id.recycler_view);
        appsRecycleView.updateMyApps(((MainApplication) getApplication()).allApps);
        TypefaceTextView close = (TypefaceTextView) findViewById(R.id.close);
        close.setTextIsSelectable(false);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        overridePendingTransition(R.anim.enter_show, R.anim.enter_hide);
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
        if ( event.isForMainActivity ) {
            return;
        }

        Intent data = new Intent();
        data.putExtra(MainActivity.PACKAGE_NAME, event.appPackage);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.exit_show, R.anim.exit_hide);
    }

}
