package wumf.com.sharedapps;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

import wumf.com.sharedapps.dialog.PhoneFromViberDialog;
import wumf.com.sharedapps.util.ViberUtils;

/**
 * Created by max on 17.09.16.
 */

public class ViberTransparentActivity extends Activity {

    private static final String TAG = ViberTransparentActivity.class.getSimpleName();

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if ( ViberUtils.hasViber() ) {
            if ( ViberUtils.hasPhoneFromViber(this) ) {
                PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
                String displayPhoneNumber = "?";
                try {
                     displayPhoneNumber = phoneNumberUtil.format(phoneNumberUtil.parse(ViberUtils.getPhoneNumber(), "CH"),
                            PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
                } catch (NumberParseException e) {
                    Log.e(TAG, e.getMessage());
                }
                new PhoneFromViberDialog( this, displayPhoneNumber );
            } else {
                setTheme(R.style.AppTheme);
                setContentView(R.layout.activity_viber_help_registration_on_viber);
            }
        } else {
            setTheme(R.style.AppTheme);
            setContentView(R.layout.activity_viber_help_install_viber);
        }
    }

    public void onStart() {
        super.onStart();
    }

}
