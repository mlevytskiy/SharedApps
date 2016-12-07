package wumf.com.sharedapps.firebase;

import android.text.TextUtils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by max on 07.12.16.
 */

public class TagsFirebase {

    private static DatabaseReference tagsRef = FirebaseDatabase.getInstance().getReference().child("tags");
    private static DatabaseReference userssRef = FirebaseDatabase.getInstance().getReference().child("users");

    public static void attachTag(final String uid, final String tag) {
        tagsRef.child(tag).child("userIds").runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Object value = mutableData.getValue();
                if (value == null) {
                    mutableData.setValue(uid);
                } else if (value instanceof  String) {
                    if (TextUtils.equals((String) value, uid)) {
                        return Transaction.abort();
                    }
                    List<String> newValue = new ArrayList<String>();
                    newValue.add( (String) value );
                    newValue.add(uid);
                    mutableData.setValue(newValue);
                } else if (value instanceof List) {
                    List<String> list = (List<String>) value;
                    if (list.contains(uid)) {
                        return Transaction.abort();
                    }
                    list.add(uid);
                    mutableData.setValue(list);
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

            }
        });
        userssRef.child(uid).child("myTags").runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Object value = mutableData.getValue();
                if (value == null) {
                    mutableData.setValue(tag);
                } else if (value instanceof  String) {
                    if (TextUtils.equals((String) value, tag)) {
                        return Transaction.abort();
                    }
                    List<String> newValue = new ArrayList<String>();
                    newValue.add( (String) value );
                    newValue.add(tag);
                    mutableData.setValue(newValue);
                } else if (value instanceof List) {
                    List<String> list = (List<String>) value;
                    if (list.contains(tag)) {
                        return Transaction.abort();
                    }
                    list.add(tag);
                    mutableData.setValue(list);
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

            }
        });
                //.push().setValue(tag);
    }

}
