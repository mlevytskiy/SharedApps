package wumf.com.sharedapps.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import wumf.com.sharedapps.firebase.transaction.AttachStringToListTransaction;

/**
 * Created by max on 30.12.16.
 */

public class FollowUnfollowPeopleFirebase {

    private static DatabaseReference waitingListRef = FirebaseDatabase.getInstance().getReference().child("waitingList");

    public void markMeAsFollowerOfContacts(final String uid, final List<String> phones) {
        waitingListRef.runTransaction(new AttachStringToListTransaction(uid, phones));
    }

}
