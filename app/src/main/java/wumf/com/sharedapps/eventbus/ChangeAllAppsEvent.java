package wumf.com.sharedapps.eventbus;

import java.util.List;

import wumf.com.appsprovider.App;

/**
 * Created by max on 02.09.16.
 */
public class ChangeAllAppsEvent {

    public final List<App> apps;

    public ChangeAllAppsEvent(List<App> apps) {
        this.apps = apps;
    }

}
