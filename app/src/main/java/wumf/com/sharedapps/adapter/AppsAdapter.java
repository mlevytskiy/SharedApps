package wumf.com.sharedapps.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import wumf.com.appsprovider.App;
import wumf.com.sharedapps.R;
import wumf.com.sharedapps.eventbus.OnClickAppEvent;
import wumf.com.sharedapps.eventbus.OnLongClickAppEvent;
import wumf.com.sharedapps.firebase.pojo.AppOrFolder;
import wumf.com.sharedapps.util.AppsSorting;
import wumf.com.sharedapps.viewholder.AppViewHolder;

/**
 * Created by max on 12.09.16.
 */
public class AppsAdapter extends RecyclerView.Adapter<AppViewHolder> {

    private List<Item> apps = new ArrayList<>();

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

    public void updateSharedApps(List<AppOrFolder> apps) {
        this.apps.clear();
        if (apps == null) {
            return;
        }
        AppsSorting.sort(apps);
        for (AppOrFolder app : apps) {
            this.apps.add(new Item(app));
        }
        notifyDataSetChanged();
    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_apps, parent, false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Item app = (Item) view.getTag();
                EventBus.getDefault().post(new OnClickAppEvent(app.appPackage));
            }
        });
        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Item app = (Item) view.getTag();
                EventBus.getDefault().post(new OnLongClickAppEvent(app.appPackage));
                return true;
            }
        });
        return new AppViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AppViewHolder holder, int position) {
        holder.bind(apps.get(position));
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

        public Item(App app) {
            id = app.id;
            appPackage = app.appPackage;
            name = app.name;
            icon = app.icon;
            time = app.installDate;
            isInnerApp = true;
        }

        public Item(AppOrFolder app) {
            id = app.getAppPackage();
            appPackage = app.getAppPackage();
            name = app.getAppName();
            icon = app.getIcon();
            time = app.getTimeLong();
            isInnerApp = false;
        }

    }

}
