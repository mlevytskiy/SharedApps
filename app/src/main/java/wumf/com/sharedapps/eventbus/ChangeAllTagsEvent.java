package wumf.com.sharedapps.eventbus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by max on 08.12.16.
 */

public class ChangeAllTagsEvent {

    public List<String> tags;

    public ChangeAllTagsEvent(List<String> tags) {
        if (tags != null) {
            this.tags = tags;
        } else {
            tags = new ArrayList<>();
        }
    }

}
