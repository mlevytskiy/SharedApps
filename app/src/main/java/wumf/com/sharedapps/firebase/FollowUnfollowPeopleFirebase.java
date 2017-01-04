package wumf.com.sharedapps.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import wumf.com.sharedapps.firebase.transaction.AttachStringToListTransaction;
import wumf.com.sharedapps.firebase.transaction.CompositeTransaction;
import wumf.com.sharedapps.firebase.transaction.ForChildrenTransaction;
import wumf.com.sharedapps.firebase.transaction.RemoveStringFromListTransaction;
import wumf.com.sharedapps.firebase.transaction.common.AnyTransaction;
import wumf.com.sharedapps.retrofit.GCMSender;

/**
 * Created by max on 30.12.16.
 */

public class FollowUnfollowPeopleFirebase {

    private static DatabaseReference waitingListRef = FirebaseDatabase.getInstance().getReference().child("waitingList");
    private static DatabaseReference userssRef = FirebaseDatabase.getInstance().getReference().child("users");

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

    public static void sendPushesPeopleWhoWaitingMe(String phoneNumber) {
        waitingListRef.child(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object value = dataSnapshot.getValue();
                if (value == null) {
                    //do nothing
                } else {
                    final GCMSender gcmSender = new GCMSender();
                    List<String> uids = (List<String>) value;
                    for (String currentUid : uids) {
                        userssRef.child(currentUid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String pushId = (String) dataSnapshot.child("pushId").getValue();
                                gcmSender.send(pushId, "New user");
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
