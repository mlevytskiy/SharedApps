package wumf.com.sharedapps;

import android.app.Activity;
import android.os.Bundle;

import wumf.com.sharedapps.view.CustomTopBar;

/**
 * Created by max on 02.01.17.
 */

public class PersonActivity extends Activity {

    public static final String KEY_USER = "user";

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_person);
        ((CustomTopBar) findViewById(R.id.top_bar)).bind(this).setText("Person");
    }

}
