package wumf.com.sharedapps.firebase.pojo;

/**
 * Created by max on 28.10.16.
 */

public class AppOrFolder {

    private String folderName;
    private String appName;
    private String appPackage;
    private String icon;

    public AppOrFolder() {
        super();
    }

    public AppOrFolder(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
