package wumf.com.sharedapps.firebase;

import android.util.Log;

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
import wumf.com.sharedapps.util.TagsBuilder;

/**
 * Created by max on 30.12.16.
 */

public class FollowUnfollowPeopleFirebase {

    private static final String TAG = new TagsBuilder().add(FollowUnfollowPeopleFirebase.class).add("firebase").build();

    private static DatabaseReference waitingListRef = FirebaseDatabase.getInstance().getReference().child("waitingList");
    private static DatabaseReference userssRef = FirebaseDatabase.getInstance().getReference().child("users");
    private static DatabaseReference tagsRef = FirebaseDatabase.getInstance().getReference().child("tags");

    public static void markMeAsFollowerOfContacts(final String uid, final List<String> newPhones, final List<String> removedPhones, TransactionResultListener listener) {
        Log.i(TAG, "markMeAsFollowerOfContacts( uid=" + uid + "newPhones.size=" + newPhones.size() + " removedPhones.size=" + removedPhones.size() + " )");
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

    public static void sendPushesPeopleWithTheSameTags(List<String> tags) {
        Log.i(TAG, "sendPushesPeopleWithTheSameTags( tags=" + tags.size() + " )");
        for (String tag : tags) {
            sendPushesPeopleWithTheSameTags(tag);
        }
    }

    public static void sendPushesPeopleWithTheSameTags(String tag) {
        Log.i(TAG, "sendPushesPeopleWithTheSameTag( tag=" + tag + " )");
        tagsRef.child(tag).child("userIds").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sendPushForUidsList(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void sendPushesPeopleWhoWaitingMe(String phoneNumber) {
        Log.i(TAG, "sendPushesPeopleWhoWaitingMe( phoneNumber=" + phoneNumber + " )");
        waitingListRef.child(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sendPushForUidsList(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private static void sendPushForUidsList(DataSnapshot dataSnapshot) {
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

}
