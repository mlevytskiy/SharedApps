package wumf.com.sharedapps.eventbus;

import wumf.com.appsprovider.App;

/**
 * Created by max on 13.09.16.
 */
public class OnClickAppEvent {

    public final App app;

    public OnClickAppEvent(App app) {
        this.app = app;
    }

}
