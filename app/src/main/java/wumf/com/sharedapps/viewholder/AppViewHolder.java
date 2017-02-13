package wumf.com.sharedapps.viewholder;

import android.net.Uri;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.github.omadahealth.typefaceview.TypefaceTextView;

import java.io.File;

import wumf.com.sharedapps.R;
import wumf.com.sharedapps.adapter.AppsAdapter;

/**
 * Created by max on 12.09.16.
 */
public class AppViewHolder extends RecyclerView.ViewHolder {

    private AppCompatImageView icon;
    private TypefaceTextView label;
    private View close;

    public AppViewHolder(View itemView) {
        super(itemView);
        icon = (AppCompatImageView) itemView.findViewById(R.id.icon);
        label = (TypefaceTextView) itemView.findViewById(R.id.label);
        close = itemView.findViewById(R.id.close);
    }

    public void bind(AppsAdapter.Item app) {
        itemView.setTag(app);
        label.setText(app.name);
        if ( TextUtils.isEmpty(app.icon) ) {
            icon.setImageDrawable(null);
            return;
        }

        if (app.isInnerApp) {
            icon.setImageURI(Uri.fromFile(new File(app.icon)));
        } else {
            Glide.with(icon.getContext()).load(app.icon).into(icon);
        }

        close.setVisibility(app.isMy ? View.VISIBLE : View.GONE);
    }

}
