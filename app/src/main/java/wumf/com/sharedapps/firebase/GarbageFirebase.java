package wumf.com.sharedapps.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import wumf.com.sharedapps.firebase.transaction.AttachStringToListTransaction;

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


    //need listen this ref


}
