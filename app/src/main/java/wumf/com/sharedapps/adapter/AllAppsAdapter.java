package wumf.com.sharedapps.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import wumf.com.appsprovider.App;
import wumf.com.sharedapps.R;
import wumf.com.sharedapps.eventbus.OnClickAppEvent;
import wumf.com.sharedapps.viewholder.AppViewHolder;

/**
 * Created by max on 12.09.16.
 */
public class AllAppsAdapter extends RecyclerView.Adapter<AppViewHolder> {

    private List<App> apps;

    public AllAppsAdapter(List<App> apps) {
        this.apps = apps;
    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_apps, parent, false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                App app = (App) view.getTag();
                EventBus.getDefault().post(new OnClickAppEvent(app));
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
}
