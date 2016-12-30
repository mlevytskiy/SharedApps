package wumf.com.sharedapps.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import wumf.com.sharedapps.firebase.transaction.AttachStringToListTransaction;
import wumf.com.sharedapps.firebase.transaction.CompositeTransaction;
import wumf.com.sharedapps.firebase.transaction.ForChildrenTransaction;
import wumf.com.sharedapps.firebase.transaction.RemoveStringFromListTransaction;
import wumf.com.sharedapps.firebase.transaction.common.AnyTransaction;

/**
 * Created by max on 30.12.16.
 */

public class FollowUnfollowPeopleFirebase {

    private static DatabaseReference waitingListRef = FirebaseDatabase.getInstance().getReference().child("waitingList");

    public static void markMeAsFollowerOfContacts(final String uid, final List<String> newPhones, final List<String> removedPhones, TransactionResultListener listener) {
        AnyTransaction transaction = null;
        if ( !newPhones.isEmpty() && !removedPhones.isEmpty() ) {
            transaction = new CompositeTransaction(new ForChildrenTransaction(new AttachStringToListTransaction(uid), newPhones),
                    new ForChildrenTransaction(new RemoveStringFromListTransaction(uid), removedPhones));
            transaction.setTransactionResultListener(listener);
        } else if ( !newPhones.isEmpty() ) {
            transaction = new ForChildrenTransaction(new AttachStringToListTransaction(uid), newPhones);
            transaction.setTransactionResultListener(listener);
        } else if ( !removedPhones.isEmpty() ) {
            transaction = new ForChildrenTransaction(new RemoveStringFromListTransaction(uid), removedPhones);
            transaction.setTransactionResultListener(listener);
        }

        if (transaction != null) {
            waitingListRef.runTransaction(transaction);
        }
    }

}
