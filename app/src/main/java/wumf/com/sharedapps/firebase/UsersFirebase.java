package wumf.com.sharedapps.firebase;

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import wumf.com.sharedapps.eventbus.NewCountryCodeFromFirebaseEvent;
import wumf.com.sharedapps.eventbus.NewPhoneNumberFromFirebaseEvent;
import wumf.com.sharedapps.util.TagsBuilder;

/**
 * Created by max on 22.09.16.
 */

public class UsersFirebase {

    private static final String TAG = new TagsBuilder().add("firebase").build();

    public static void addMe(FirebaseUser fUser) {
        User user = new User();
        user.email = fUser.getEmail();
        user.name = fUser.getDisplayName();
        DatabaseReference r = FirebaseDatabase.getInstance().getReference().child("users").child(fUser.getUid());
        r.setValue(user);
    }

    public static void removeMe(String uid) {
        FirebaseDatabase.getInstance().getReference().child("users").child(uid).removeValue();
    }

    public static void addAppToFavourite() {

    }

    public static void removeAppFromFavourite() {

    }

    public static void addFolderToFavourite(String folderName) {

    }

    public static void removeFolderFromFavourite(String folderName) {

    }

    public static void updatePhoneNumber(String uid, String phoneNumber) {
        FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("phoneNumber").setValue(phoneNumber);
    }

    public static void updateCountryCode(String uid, String countryCode) {
        FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("countryCode").setValue(countryCode);
    }

    public static void listenPhoneNumber(String uid) {
        FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("phoneNumber").addValueEventListener(new ValueEventListener() {
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
        FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("countryCode").addListenerForSingleValueEvent(new ValueEventListener() {
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

    public static class User {
        public String name;
        public String email;
        public String phoneNumber;
        public String countryCode;
    }

}
