package wumf.com.sharedapps.eventbus;

/**
 * Created by max on 17.01.17.
 */

public class SearchByPhoneOnClickEvent {

    public final String query;

    public SearchByPhoneOnClickEvent(String str) {
        query = str;
    }

}
