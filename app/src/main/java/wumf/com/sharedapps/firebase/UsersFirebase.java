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

    private static final String TAG = new TagsBuilder().add("firebase").build();
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

    public static void getUsers(final List<String> phoneNumbers, final GetUsersListener listener) {

        userssRef.orderByChild("phoneNumber").startAt("+").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Profile> result = new ArrayList<>();
                for( DataSnapshot child : dataSnapshot.getChildren() ) {
                    if ( phoneNumbers.contains(child.child("phoneNumber").getValue()) ) {
                        result.add(child.getValue(Profile.class));
                    } else {
                        //do nothing
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
        getUsers(mock, new GetUsersListener() {
            @Override
            public void users(List<Profile> profiles) {
                //do nothing
            }
        });
    }

}
