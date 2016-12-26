package wumf.com.sharedapps;

import android.app.Activity;
import android.os.Bundle;

import interesting.com.contactsprovider.ContactProvider;
import wumf.com.sharedapps.dialog.PhoneFromViberDialog;
import wumf.com.sharedapps.util.ViberUtils;

/**
 * Created by max on 17.09.16.
 */

public class ViberTransparentActivity extends Activity {

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if ( ViberUtils.hasViber() ) {
            if ( ViberUtils.hasPhoneFromViber(this) ) {
                String countryCode = ((MainApplication) getApplication()).country;
                String displayPhoneNumber = ContactProvider.instance.getPhoneNumber(ViberUtils.getPhoneNumber(), countryCode);
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
