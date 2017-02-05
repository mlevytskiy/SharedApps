package wumf.com.sharedapps.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import hugo.weaving.DebugLog;
import wumf.com.sharedapps.firebase.transaction.AttachStringToListTransaction;
import wumf.com.sharedapps.firebase.transaction.RemoveStringFromListTransaction;

/**
 * Created by max on 01.01.17.
 */

@DebugLog
public class GarbageFirebase {

    private static DatabaseReference userssRef = FirebaseDatabase.getInstance().getReference().child("users");

    public static void moveUserToGarbage(String myUid, String userUid) {
        userssRef.child(myUid).child("follow").runTransaction(new RemoveStringFromListTransaction(userUid));
        userssRef.child(myUid).child("garbage").runTransaction(new AttachStringToListTransaction(userUid));
    }

    public static void restoreAllRemovedPeople(String myUid) {
        userssRef.child(myUid).child("garbage").removeValue();
    }

    public static void restore(String myUid, String userUid) {
        userssRef.child(myUid).child("garbage").runTransaction(new RemoveStringFromListTransaction(userUid));
    }

}
