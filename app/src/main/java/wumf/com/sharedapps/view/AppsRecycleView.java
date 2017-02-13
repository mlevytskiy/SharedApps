package wumf.com.sharedapps.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;

import java.util.List;

import wumf.com.appsprovider.App;
import wumf.com.sharedapps.adapter.AppsAdapter;
import wumf.com.sharedapps.firebase.pojo.AppOrFolder;

/**
 * Created by max on 02.01.17.
 */

public class AppsRecycleView extends RecyclerViewEmptySupport {

    private AppsAdapter adapter;

    public AppsRecycleView(Context context) {
        super(context);
        setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapter = new AppsAdapter();
        setAdapter(adapter);
    }

    public AppsRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapter = new AppsAdapter();
        setAdapter(adapter);
    }

    public void updateMyApps(List<App> apps) {
        adapter.updateMyApps(apps);
    }

    public boolean isEmpty() {
        return adapter.getItemCount() == 0;
    }

    public void updateSharedApps(List<AppOrFolder> apps) {
        adapter.updateSharedApps(apps, false);
    }

    public void updateSharedApps(List<AppOrFolder> apps, boolean isMy) {
        adapter.updateSharedApps(apps, isMy);
    }

}
