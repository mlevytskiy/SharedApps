package wumf.com.sharedapps.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import wumf.com.sharedapps.firebase.pojo.Profile;

/**
 * Created by max on 19.12.16.
 */

public class GetUsersFirebase {

    private static DatabaseReference userssRef = FirebaseDatabase.getInstance().getReference().child("users");

    public static void getUsers() {
        List<String> mock = new ArrayList<>();
        mock.add("+380 93 320 9152");
        getUsers(mock, new GetUsersListener() {
            @Override
            public void users(List<Profile> profiles) {
                //do nothing
            }
        });
    }

    public static void getUsers(final List<String> phoneNumbers, final GetUsersListener listener) {

        userssRef.orderByChild("phoneNumber").startAt("+").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Profile> result = new ArrayList<>();
                for( DataSnapshot child : dataSnapshot.getChildren() ) {
                    if ( phoneNumbers.contains(child.child("phoneNumber").getValue()) ) {
                        result.add(child.getValue(Profile.class));
                    } else {
                        //do nothing
                    }
                }
                listener.users(result);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //do nothing
            }
        });

    }

}
