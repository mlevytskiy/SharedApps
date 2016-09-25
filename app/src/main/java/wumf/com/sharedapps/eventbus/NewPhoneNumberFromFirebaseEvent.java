package wumf.com.sharedapps.eventbus;

/**
 * Created by max on 22.09.16.
 */

public class NewPhoneNumberFromFirebaseEvent {

    public final String phone;

    public NewPhoneNumberFromFirebaseEvent(String phone) {
        this.phone = phone;
    }

}
