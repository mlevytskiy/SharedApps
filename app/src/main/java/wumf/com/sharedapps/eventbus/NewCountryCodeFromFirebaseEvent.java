package wumf.com.sharedapps.eventbus;

/**
 * Created by max on 25.09.16.
 */

public class NewCountryCodeFromFirebaseEvent {

    public final String countryCode;

    public NewCountryCodeFromFirebaseEvent(String countryCode) {
        this.countryCode = countryCode;
    }

}
