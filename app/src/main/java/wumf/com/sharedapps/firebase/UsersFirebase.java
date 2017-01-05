package wumf.com.sharedapps.firebase;

import android.util.Log;

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

import wumf.com.sharedapps.eventbus.NewCountryCodeFromFirebaseEvent;
import wumf.com.sharedapps.eventbus.NewPhoneNumberFromFirebaseEvent;
import wumf.com.sharedapps.firebase.pojo.Profile;
import wumf.com.sharedapps.util.TagsBuilder;

/**
 * Created by max on 22.09.16.
 */

public class UsersFirebase {

    private static final String TAG = new TagsBuilder().add(UsersFirebase.class).add("firebase").build();
    private static DatabaseReference userssRef = FirebaseDatabase.getInstance().getReference().child("users");


    public static void addMe(FirebaseUser fUser) {
        Log.i(TAG, "addMe(fUser uid=" + fUser.getUid() + ")");
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
        Log.i(TAG, "refreshPushId(uid=" + uid + ")");
        String token = FirebaseInstanceId.getInstance().getToken();
        getUserRef(uid).child("pushId").setValue(token);
    }

    public static void removeMe(String uid) {
        Log.i(TAG, "removeMe(uid=" + uid + ")");
        getUserRef(uid).removeValue();
    }

    public static void updatePhoneNumber(String uid, String phoneNumber) {
        Log.i(TAG, "updatePhoneNumber(uid=" + uid + " phoneNumber=" + phoneNumber + ")");
        getUserRef(uid).child("phoneNumber").setValue(phoneNumber);
    }

    public static void updateCountryCode(String uid, String countryCode) {
        Log.i(TAG, "updateCountryCode(uid=" + uid + " countryCode=" + countryCode + ")");
        getUserRef(uid).child("countryCode").setValue(countryCode);
    }

    public static void listenPhoneNumber(String uid) {
        Log.i(TAG, "listenPhoneNumber(uid=" + uid + ")");
        getUserRef(uid).child("phoneNumber").addValueEventListener(new ValueEventListener() {
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
        });
    }

    public static void listenCountryCode(String uid) {
        Log.i(TAG, "listenCountryCode=" + uid);
        getUserRef(uid).child("countryCode").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object value = dataSnapshot.getValue();
                Log.i(TAG, "onDataChange" + (value == null) );
                if (value != null) {
                    EventBus.getDefault().post(new NewCountryCodeFromFirebaseEvent(value.toString()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void getUsers(final List<String> phoneNumbers, final List<String> tags, final GetUsersListener listener) {
        Log.i(TAG, "getUsers(phoneNumbers.size=" + phoneNumbers.size() + " tags=" + tags.size() + ")");
        userssRef.orderByChild("phoneNumber").addListenerForSingleValueEvent(new ValueEventListener() { //problems with startAt("+"); sometimes wrong result
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Profile> result = new ArrayList<>();
                Log.i(TAG, "users with phones count=" + dataSnapshot.getChildrenCount());
                for( DataSnapshot child : dataSnapshot.getChildren() ) {
                    String phoneNumber = (String) child.child("phoneNumber").getValue();
                    Object userTagsObj = child.child("myTags").getValue();
                    List<String> userTags = (userTagsObj != null) ? (List<String>) userTagsObj : new ArrayList<String>();
                    Log.i(TAG, "phoneNumber=" + phoneNumber);
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
                //do nothing
            }
        });

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

}
