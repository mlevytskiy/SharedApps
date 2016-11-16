package wumf.com.sharedapps.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import wumf.com.sharedapps.R;
import wumf.com.sharedapps.firebase.pojo.AppOrFolder;

/**
 * Created by max on 20.10.16.
 */

public class SharedAppsAdapter extends BaseAdapter {

    private final static int TYPE_FOLDER = 0;
    private final static int TYPE_APP = 1;

    private List<String> folders;
    private List<AppOrFolder> apps;

    public SharedAppsAdapter() {
        this.folders = new ArrayList<>();
        this.apps = new ArrayList<>();
    }

    public SharedAppsAdapter(List<String> items) {
        this.folders = items;
    }

    @Override
    public int getCount() {
        return folders.size() + apps.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public int getItemViewType(int index) {
        if (index < folders.size()) {
            return TYPE_FOLDER;
        } else {
            return TYPE_APP;
        }
    }

    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (getItemViewType(position) == TYPE_FOLDER) {
            FolderViewHolder vh;
            if (view == null) {
                view = View.inflate(viewGroup.getContext(), R.layout.item_folder, null);
                vh = new FolderViewHolder();
                view.setTag(vh);
                vh.textView = (TextView) view.findViewById(R.id.text_view);
            } else {
                vh = (FolderViewHolder) view.getTag();
            }
            vh.textView.setText(folders.get(position));
        } else {
            AppViewHolder vh;
            if (view == null) {
                view = View.inflate(viewGroup.getContext(), R.layout.item_app, null);
                vh = new AppViewHolder();
                view.setTag(vh);
                vh.textView = (TextView) view.findViewById(R.id.label);
                vh.imageView = (ImageView) view.findViewById(R.id.icon);
            } else {
                vh = (AppViewHolder) view.getTag();
            }
            AppOrFolder app = apps.get(position - folders.size());
            Glide.with(viewGroup.getContext()).load(app.getIcon()).into(vh.imageView);
            vh.textView.setText(app.getAppName());
        }

        return view;
    }

    public void updateItems(List<String> folders, List<AppOrFolder> apps) {
        this.folders.clear();
        this.folders.addAll(folders);
        this.apps.clear();
        this.apps.addAll(apps);
        notifyDataSetChanged();
    }

    static class AppViewHolder {
        public ImageView imageView;
        public TextView textView;
    }

    static class FolderViewHolder {
        public TextView textView;
    }

}
