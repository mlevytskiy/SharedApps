package wumf.com.sharedapps.firebase.pojo;

import android.text.TextUtils;

import com.google.firebase.database.Exclude;

import java.util.List;
import java.util.Map;

/**
 * Created by max on 19.12.16.
 */

public class Profile {

    @Exclude
    private String uid;

    private String countryCode;
    private String email;
    private String name;
    private String nameForSearch;
    private String phoneNumber;
    private Map<String, AppOrFolder> apps;
    private List<String> myTags;
    private String icon;
    private String pushId;
    private List<String> follow;

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

    public Map<String, AppOrFolder> getApps() {
        return apps;
    }

    public void setApps(Map<String, AppOrFolder> apps) {
        this.apps = apps;
    }

    public List<String> getMyTags() {
        return myTags;
    }

    public void setMyTags(List<String> myTags) {
        this.myTags = myTags;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    @Exclude
    public String getUid() {
        return uid;
    }

    @Exclude
    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Profile) {
            return TextUtils.equals(uid, ((Profile) obj).uid);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (uid == null) ? super.hashCode() : uid.hashCode();
    }

    public String getNameForSearch() {
        return nameForSearch;
    }

    public void setNameForSearch(String nameForSearch) {
        this.nameForSearch = nameForSearch;
    }

    public List<String> getFollow() {
        return follow;
    }

    public void setFollow(List<String> follow) {
        this.follow = follow;
    }
}
