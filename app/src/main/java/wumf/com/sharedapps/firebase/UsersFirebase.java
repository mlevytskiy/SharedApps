package wumf.com.sharedapps.firebase;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import hugo.weaving.DebugLog;
import wumf.com.sharedapps.eventbus.NewCountryCodeFromFirebaseEvent;
import wumf.com.sharedapps.eventbus.NewPhoneNumberFromFirebaseEvent;
import wumf.com.sharedapps.firebase.pojo.Profile;


/**
 * Created by max on 22.09.16.
 */

@DebugLog
public class UsersFirebase {

    private static DatabaseReference userssRef = FirebaseDatabase.getInstance().getReference().child("users");

    public static void addMe(FirebaseUser fUser) {
        Profile user = new Profile();
        user.setEmail(fUser.getEmail());
        user.setName(fUser.getDisplayName());
        user.setIcon(fUser.getPhotoUrl().toString());
        String token = FirebaseInstanceId.getInstance().getToken();
        user.setPushId(token);
        DatabaseReference r = FirebaseDatabase.getInstance().getReference().child("users").child(fUser.getUid());
        r.setValue(user);
    }

    public static void refreshPushId(String uid) {
        String token = FirebaseInstanceId.getInstance().getToken();
        getUserRef(uid).child("pushId").setValue(token);
    }

    public static void removeMe(String uid) {
        getUserRef(uid).removeValue();
    }

    public static void updatePhoneNumber(String uid, String phoneNumber) {
        getUserRef(uid).child("phoneNumber").setValue(phoneNumber);
    }

    public static void updateCountryCode(String uid, String countryCode) {
        getUserRef(uid).child("countryCode").setValue(countryCode);
    }

    public static void listenPhoneNumber(String uid) {
        getUserRef(uid).child("phoneNumber").addValueEventListener(new PhoneNumberValueEventListener());
    }

    public static void listenCountryCode(String uid) {
        getUserRef(uid).child("countryCode").addListenerForSingleValueEvent(new CountryCodeValueEventListener());
    }

    public static void getUsers(final List<String> phoneNumbers, final List<String> tags, final GetUsersListener listener) {
        userssRef.orderByChild("phoneNumber").addListenerForSingleValueEvent(new UsersValueEventListener(phoneNumbers, tags, listener));

    }

    private static DatabaseReference getUserRef(String uid) {
        return FirebaseDatabase.getInstance().getReference().child("users").child(uid);
    }

    public static void getUsers() {
        List<String> mock = new ArrayList<>();
        mock.add("+380 93 320 9152");
        getUsers(mock, new ArrayList<String>(), new GetUsersListener() {
            @Override
            public void users(List<Profile> profiles) {
                //do nothing
            }
        });
    }

    @DebugLog
    private static class PhoneNumberValueEventListener implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Object value = dataSnapshot.getValue();
            if (value != null) {
                EventBus.getDefault().post(new NewPhoneNumberFromFirebaseEvent(value.toString()));
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

    @DebugLog
    private static class CountryCodeValueEventListener implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Object value = dataSnapshot.getValue();
            if (value != null) {
                EventBus.getDefault().post(new NewCountryCodeFromFirebaseEvent(value.toString()));
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

    @DebugLog
    private static class UsersValueEventListener implements ValueEventListener {

        private List<String> phoneNumbers;
        private List<String> tags;
        private GetUsersListener listener;

        public UsersValueEventListener(List<String> phoneNumbers, List<String> tags, GetUsersListener listener) {
            this.phoneNumbers = phoneNumbers;
            this.tags = tags;
            this.listener = listener;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            List<Profile> result = new ArrayList<>();
            for( DataSnapshot child : dataSnapshot.getChildren() ) {
                String phoneNumber = (String) child.child("phoneNumber").getValue();
                Object userTagsObj = child.child("myTags").getValue();
                List<String> userTags = (userTagsObj != null) ? (List<String>) userTagsObj : new ArrayList<String>();
                if ( phoneNumbers.contains(phoneNumber) ) {
                    Profile profile = child.getValue(Profile.class);
                    profile.setUid(child.getKey());
                    result.add(profile);
                } else {
                    if ( tags != null && userTags != null ) {
                        for (String tag : tags) {
                            if (userTags.contains(tag)) {
                                Profile profile = child.getValue(Profile.class);
                                profile.setUid(child.getKey());
                                result.add(profile);
                                break;
                            }
                        }
                    }
                }
            }
            listener.users(result);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

}
