package wumf.com.sharedapps.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

import wumf.com.appsprovider.App;
import wumf.com.sharedapps.R;
import wumf.com.sharedapps.adapter.AppsAdapter;
import wumf.com.sharedapps.firebase.pojo.AppOrFolder;
import wumf.com.sharedapps.util.FlipStateAnimator;

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

    public AppsAdapter.Item findItemByPackageName(String packageName) {
        for (int i = 0; i < adapter.getItemCount(); i++) {
            AppsAdapter.Item item = adapter.getItem(i);
            if (TextUtils.equals(item.appPackage, packageName)) {
                return item;
            }
        }
        return null;
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

    public boolean isNeedCloseFlippedCards() {
        return adapter.isNeedCloseFlippedCards();
    }

    public boolean closeFlippedCards() {
        List<Integer> indexes = adapter.getNeedCloseFlippedCardIndexes();
        boolean hasView = false;
        for (Integer index : indexes) {
            View view = getLayoutManager().findViewByPosition(index);
            View backCardView = view.findViewById(R.id.card_view_back_side);
            View frontCardView = view.findViewById(R.id.card_view);
            new FlipStateAnimator(view, backCardView, frontCardView).changeStateWithAnimation(true);
            hasView = true;
        }
        return hasView;
    }

}
