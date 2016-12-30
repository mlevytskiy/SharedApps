package wumf.com.sharedapps.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import wumf.com.sharedapps.OnChangeAllTagsListener;
import wumf.com.sharedapps.eventbus.ChangeMyTagsEvent;
import wumf.com.sharedapps.firebase.transaction.AttachStringToListTransaction;
import wumf.com.sharedapps.firebase.transaction.RemoveStringFromListTransaction;

/**
 * Created by max on 07.12.16.
 */

public class TagsFirebase {

    private static DatabaseReference tagsRef = FirebaseDatabase.getInstance().getReference().child("tags");
    private static DatabaseReference userssRef = FirebaseDatabase.getInstance().getReference().child("users");

    public static void attachTag(final String uid, final String tag) {
        tagsRef.child(tag).child("userIds").runTransaction(new AttachStringToListTransaction(uid));
        userssRef.child(uid).child("myTags").runTransaction(new AttachStringToListTransaction(tag));
    }

    public static void removeTag(final String uid, final String tag) {
        tagsRef.child(tag).child("userIds").runTransaction(new RemoveStringFromListTransaction(uid));
        userssRef.child(uid).child("myTags").runTransaction(new RemoveStringFromListTransaction(tag));
    }

    public static void listenMyTags(String uid) {
        userssRef.child(uid).child("myTags").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object value = dataSnapshot.getValue();
                List<String> tags = (value != null) ? (List<String>) value : new ArrayList<String>();
                EventBus.getDefault().post(new ChangeMyTagsEvent(tags));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void listenAllTags(final OnChangeAllTagsListener listener) {
        tagsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();
                List<String> tags = new ArrayList<String>();
                if (count != 0) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        String tag = child.getKey();
                        tags.add(tag);
                    }
                }
                listener.onChange(tags);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
