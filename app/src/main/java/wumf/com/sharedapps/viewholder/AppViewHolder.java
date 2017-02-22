package wumf.com.sharedapps.viewholder;

import android.net.Uri;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.github.omadahealth.typefaceview.TypefaceTextView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import wumf.com.sharedapps.R;
import wumf.com.sharedapps.adapter.AppsAdapter;
import wumf.com.sharedapps.eventbus.OnClickAppEvent;
import wumf.com.sharedapps.eventbus.RemoveAppEvent;

/**
 * Created by max on 12.09.16.
 */
public class AppViewHolder extends RecyclerView.ViewHolder {

    private AppCompatImageView icon;
    private TypefaceTextView label;
    private ImageButton cancel;
    private ImageButton googlePlay;
    private View backView;
    private View frontView;

    public AppViewHolder(View itemView, View backView, View frontView) {
        super(itemView);
        icon = (AppCompatImageView) itemView.findViewById(R.id.icon);
        label = (TypefaceTextView) itemView.findViewById(R.id.label);
        if (backView != null) {
            this.backView = backView;
            this.frontView = frontView;
            cancel = (ImageButton) backView.findViewById(R.id.cancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppsAdapter.Item app = (AppsAdapter.Item) view.getTag();
                    EventBus.getDefault().post(new RemoveAppEvent(app.appPackage));
                }
            });
            googlePlay = (ImageButton) backView.findViewById(R.id.go_to_google_play);
            googlePlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppsAdapter.Item app = (AppsAdapter.Item) view.getTag();
                    EventBus.getDefault().post(new OnClickAppEvent(app.appPackage));
                }
            });
        }
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

        if (backView != null) {
            backView.setVisibility(View.GONE);
            frontView.setVisibility(View.VISIBLE);
            cancel.setTag(app);
            googlePlay.setTag(app);
        }
    }

}
