package wumf.com.sharedapps.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import wumf.com.appsprovider.App;
import wumf.com.sharedapps.R;
import wumf.com.sharedapps.eventbus.OnClickAppEvent;
import wumf.com.sharedapps.firebase.pojo.AppOrFolder;
import wumf.com.sharedapps.util.AppsSorting;
import wumf.com.sharedapps.util.FlipStateAnimator;
import wumf.com.sharedapps.viewholder.AppViewHolder;

/**
 * Created by max on 12.09.16.
 */
public class AppsAdapter extends RecyclerView.Adapter<AppViewHolder> {

    private List<Item> apps = new ArrayList<>();
    private List<String> flippedCards = new ArrayList<>();

    public AppsAdapter() {
    }

    public void updateMyApps(List<App> apps) {
        this.apps.clear();
        if (apps == null) {
            return;
        }
        for (App app : apps) {
            this.apps.add(new Item(app));
        }
        notifyDataSetChanged();
    }

    public void updateSharedApps(List<AppOrFolder> apps, boolean isMy) {
        this.apps.clear();
        if (apps == null) {
            return;
        }
        AppsSorting.sort(apps);
        for (AppOrFolder app : apps) {
            this.apps.add(new Item(app, isMy));
        }
        notifyDataSetChanged();
    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        boolean isMy = apps.isEmpty() ? false : apps.get(0).isMy;
        final View v;
        final View backCardView;
        final View frontCardView;
        if (isMy) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_apps_complicated, parent, false);
            backCardView = v.findViewById(R.id.card_view_back_side);
            frontCardView = v.findViewById(R.id.card_view);
            frontCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Item item = (Item) v.getTag();
                    changeFlippedCards(item.appPackage, true);
                    new FlipStateAnimator(v, frontCardView, backCardView).changeStateWithAnimation(false);
                }
            });
            backCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Item item = (Item) v.getTag();
                    changeFlippedCards(item.appPackage, false);
                    new FlipStateAnimator(v, backCardView, frontCardView).changeStateWithAnimation(true);
                }
            });
        } else {
            backCardView = null;
            frontCardView = null;
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_apps, parent, false);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Item app = (Item) view.getTag();
                    EventBus.getDefault().post(new OnClickAppEvent(app.appPackage));
                }
            });
        }
        return new AppViewHolder(v, backCardView, frontCardView);
    }

    private void changeFlippedCards(String packageName, boolean isAdd) {
        for (int i = 0; i < apps.size(); i++) {
            Item current = apps.get(i);
            if (TextUtils.equals(current.appPackage, packageName)) {
                if (isAdd) {
                    flippedCards.add(current.appPackage);
                } else {
                    flippedCards.remove(current.appPackage);
                }
            }
        }
    }

    @Override
    public void onBindViewHolder(AppViewHolder holder, int position) {
        Item item = apps.get(position);
        flippedCards.remove(item.appPackage);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    public static class Item {

        public String id;
        public String appPackage;
        public String name;
        public String icon;
        public long time;
        public boolean isInnerApp;
        public boolean isMy;

        public Item(App app) {
            id = app.id;
            appPackage = app.appPackage;
            name = app.name;
            icon = app.icon;
            time = app.installDate;
            isInnerApp = true;
        }

        public Item(AppOrFolder app) {
            this(app, false);
        }

        public Item(AppOrFolder app, boolean isMy) {
            id = app.getAppPackage();
            appPackage = app.getAppPackage();
            name = app.getAppName();
            icon = app.getIcon();
            time = app.getTimeLong();
            isInnerApp = false;
            this.isMy = isMy;
        }

    }

    public boolean isNeedCloseFlippedCards() {
        return !flippedCards.isEmpty();
    }

    public List<Integer> getNeedCloseFlippedCardIndexes() {
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < apps.size(); i++) {
            Item item  = apps.get(i);
            for (String p : flippedCards) {
                if (TextUtils.equals(item.appPackage, p)) {
                    indexes.add(i);
                    break;
                }
            }
        }
        flippedCards.clear();
        return indexes;
    }

}
