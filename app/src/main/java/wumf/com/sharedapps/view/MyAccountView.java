package wumf.com.sharedapps.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;

import wumf.com.sharedapps.R;

/**
 * Created by max on 15.09.16.
 */

public class MyAccountView extends LinearLayout {

    private ImageView icon;
    private TextView name;
    private TextView email;

    public MyAccountView(Context context) {
        super(context);
        init(context);
    }

    public MyAccountView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyAccountView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        inflate(context, R.layout.custom_item_my_account, this);
        icon = (ImageView) findViewById(R.id.icon);
        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
    }

    public void setUser(FirebaseUser user) {
        name.setText(user.getDisplayName());
        email.setText(user.getEmail());
        Glide.with(getContext()).load(user.getPhotoUrl()).into(icon);
    }

}
