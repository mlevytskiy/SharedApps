package wumf.com.sharedapps.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import wumf.com.sharedapps.eventbus.RemovedFollowedUsersChangeEvent;
import wumf.com.sharedapps.firebase.transaction.AttachStringToListTransaction;
import wumf.com.sharedapps.firebase.transaction.RemoveStringFromListTransaction;

/**
 * Created by max on 01.01.17.
 */

public class GarbageFirebase {

    private static DatabaseReference userssRef = FirebaseDatabase.getInstance().getReference().child("users");

    public static void moveUserToGarbage(String myUid, String userUid) {
        userssRef.child(myUid).child("garbage").runTransaction(new AttachStringToListTransaction(userUid));
    }

    public static void restoreAllRemovedPeople(String myUid) {
        userssRef.child(myUid).child("garbage").removeValue();
    }

    public static void restore(String myUid, String userUid) {
        userssRef.child(myUid).child("garbage").runTransaction(new RemoveStringFromListTransaction(userUid));
    }

    public static void listenAndNotify(String uid) {
        userssRef.child(uid).child("garbage").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object value = dataSnapshot.getValue();
                List<String> uids = (value == null) ? new ArrayList<String>() : (List<String>) value;
                EventBus.getDefault().post(new RemovedFollowedUsersChangeEvent(uids));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //need listen this ref


}
