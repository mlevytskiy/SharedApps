package wumf.com.sharedapps.eventbus;

/**
 * Created by max on 13.09.16.
 */
public class OnClickAppEvent {

    public final boolean isForMainActivity;
    public final String appPackage;

    public OnClickAppEvent(String appPackage) {
        this(appPackage, false);
    }

    public OnClickAppEvent(String appPackage, boolean isForMainActivity) {
        this.appPackage = appPackage;
        this.isForMainActivity = isForMainActivity;
    }

}
