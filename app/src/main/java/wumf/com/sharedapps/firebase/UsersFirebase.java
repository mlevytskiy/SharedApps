package wumf.com.sharedapps.firebase;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import wumf.com.sharedapps.eventbus.NewPhoneNumberFromFirebaseEvent;

/**
 * Created by max on 22.09.16.
 */

public class UsersFirebase {

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

    public static void updatePhoneNumber(String uid, String phoneNumber) {
        FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("phoneNumber").setValue(phoneNumber);
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

    public static class User {
        public String name;
        public String email;
        public String phoneNumber;
    }

}
