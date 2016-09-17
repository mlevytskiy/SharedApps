package wumf.com.sharedapps.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import wumf.com.appsprovider.App;
import wumf.com.sharedapps.R;
import wumf.com.sharedapps.view.IconImageView;
import wumf.com.sharedapps.view.LabelTextView;

/**
 * Created by max on 12.09.16.
 */
public class AppViewHolder extends RecyclerView.ViewHolder {

    private IconImageView icon;
    private LabelTextView label;

    public AppViewHolder(View itemView) {
        super(itemView);
        icon = (IconImageView) itemView.findViewById(R.id.icon);
        label = (LabelTextView) itemView.findViewById(R.id.label);

    }

    public void bind(App app) {
        itemView.setTag(app);
        icon.setApp(app);
        label.setApp(app);
    }

}
