package wumf.com.sharedapps.eventbus;

import java.util.List;

import wumf.com.appsprovider.App;

/**
 * Created by max on 02.09.16.
 */
public class ChangeTop6AppsEvent {

    public final List<App> apps;

    public ChangeTop6AppsEvent(List<App> apps) {
        this.apps = apps;
    }

}
