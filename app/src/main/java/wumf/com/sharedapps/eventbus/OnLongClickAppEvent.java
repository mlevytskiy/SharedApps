package wumf.com.sharedapps.eventbus;

/**
 * Created by max on 13.09.16.
 */
public class OnLongClickAppEvent {

    public final String appPackage;

    public OnLongClickAppEvent(String appPackage) {
        this.appPackage = appPackage;
    }

}
