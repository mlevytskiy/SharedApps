package wumf.com.sharedapps.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.omadahealth.typefaceview.TypefaceTextView;
import com.google.firebase.auth.FirebaseUser;

import org.greenrobot.eventbus.EventBus;

import hugo.weaving.DebugLog;
import wumf.com.sharedapps.MainApplication;
import wumf.com.sharedapps.R;
import wumf.com.sharedapps.eventbus.SignOutFromFirebaseEvent;

/**
 * Created by max on 15.09.16.
 */

@DebugLog
public class MyAccountView extends LinearLayout {

    private ImageView icon;
    private TextView name;
    private TextView email;
    private TypefaceTextView signOut;
    private View attachPhoneNumberViaViber;
    private TextView country;

    private View phoneNumberContainer;
    private View fullPhoneNumberFromViberContainer;
    private TextView phoneNumberTextView;

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

    private void init(final Context context) {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        inflate(context, R.layout.custom_item_my_account, this);
        icon = (ImageView) findViewById(R.id.icon);
        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        country = (TextView) findViewById(R.id.country);
        attachPhoneNumberViaViber = findViewById(R.id.attach_phone_number_via_viber);
        signOut = (TypefaceTextView) findViewById(R.id.sign_out);
        signOut.setTextIsSelectable(false);
        signOut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new SignOutFromFirebaseEvent());
            }
        });

        phoneNumberContainer = findViewById(R.id.phone_number_container);
        fullPhoneNumberFromViberContainer = findViewById(R.id.phone_number_from_viber_container);
        phoneNumberTextView = (TextView) findViewById(R.id.phone_number);

        if (MainApplication.instance.phoneNumber != null) {
            updatePhoneNumber(MainApplication.instance.phoneNumber);
        }

    }

    public void setVisibility(int visibility) {
        if (visibility == View.VISIBLE) {
            if (MainApplication.instance.phoneNumber != null) {
                updatePhoneNumber(MainApplication.instance.phoneNumber);
            } else {
                phoneNumberContainer.setVisibility(View.GONE);
                fullPhoneNumberFromViberContainer.setVisibility(View.VISIBLE);
            }
        }
        super.setVisibility(visibility);
    }

    public void updatePhoneNumber(String phoneNumber) {
        phoneNumberContainer.setVisibility(TextUtils.isEmpty(phoneNumber) ? View.GONE : View.VISIBLE);
        fullPhoneNumberFromViberContainer.setVisibility(TextUtils.isEmpty(phoneNumber) ? View.VISIBLE : View.GONE);
        phoneNumberTextView.setText(phoneNumber);
    }

    public void setUser(FirebaseUser user) {
        name.setText(user.getDisplayName());
        email.setText(user.getEmail());
        country.setText(MainApplication.instance.country);
        Glide.with(getContext()).load(user.getPhotoUrl()).into(icon);
    }

    public void setOnViberClickListener(OnClickListener onClickListener) {
        attachPhoneNumberViaViber.setOnClickListener(onClickListener);
    }

}
