package wumf.com.sharedapps.eventbus;

/**
 * Created by max on 17.01.17.
 */

public class SearchByNickOrNameOnClickEvent {

    public final String query;

    public SearchByNickOrNameOnClickEvent(String str) {
        query = str;
    }

}
