package wumf.com.sharedapps;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by max on 20.10.16.
 */

public class SharedAppsAdapter extends BaseAdapter {

    private List<String> items;

    public SharedAppsAdapter(List<String> items) {
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder vh;
        if (view == null) {
            view = View.inflate(viewGroup.getContext(), R.layout.item_folder, null);
            vh = new ViewHolder();
            view.setTag(vh);
            vh.textView = (TextView) view.findViewById(R.id.text_view);
        } else {
            vh = (ViewHolder) view.getTag();
        }

        vh.textView.setText(items.get(position));
        return view;
    }

    public void updateItems(List<String> folders) {
        items.clear();
        items.addAll(folders);
        notifyDataSetChanged();
    }

    public void updateItems(String folder) {
        updateItems(Collections.singletonList(folder));
    }

    static class ViewHolder {
        public TextView textView;
    }

}
