package wumf.com.sharedapps.eventbus;

/**
 * Created by max on 13.09.16.
 */
public class OnClickAppEvent {

    public final boolean isNeedAddOnFirebase;
    public final String appPackage;

    public OnClickAppEvent(String appPackage) {
        this(appPackage, false);
    }

    public OnClickAppEvent(String appPackage, boolean isNeedAddOnFirebase) {
        this.appPackage = appPackage;
        this.isNeedAddOnFirebase = isNeedAddOnFirebase;
    }

}
