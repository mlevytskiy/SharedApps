package wumf.com.sharedapps.eventbus;

/**
 * Created by max on 25.09.16.
 */

public class GetNewCountryEvent {

    public final String country;

    public GetNewCountryEvent(String country) {
        this.country = country;
    }

}
