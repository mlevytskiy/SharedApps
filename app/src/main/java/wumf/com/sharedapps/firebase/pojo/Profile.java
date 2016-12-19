package wumf.com.sharedapps.firebase.pojo;

import java.util.List;

/**
 * Created by max on 19.12.16.
 */

public class Profile {

    private String countryCode;
    private String email;
    private String name;
    private String phoneNumber;
    private List<AppOrFolder> apps;

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<AppOrFolder> getApps() {
        return apps;
    }

    public void setApps(List<AppOrFolder> apps) {
        this.apps = apps;
    }
}
