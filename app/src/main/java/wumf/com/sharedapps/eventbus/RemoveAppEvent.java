package wumf.com.sharedapps.eventbus;

/**
 * Created by max on 13.09.16.
 */
public class RemoveAppEvent {

    public final String appPackage;

    public RemoveAppEvent(String appPackage) {
        this.appPackage = appPackage;
    }

}
