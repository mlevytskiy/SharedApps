package wumf.com.sharedapps.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import interesting.com.contactsprovider.ContactProvider;
import wumf.com.sharedapps.MainApplication;
import wumf.com.sharedapps.eventbus.SearchQueryFirebaseResultEvent;
import wumf.com.sharedapps.firebase.pojo.Profile;

/**
 * Created by max on 17.01.17.
 */

public class SearchQueryFirebase {

    private static DatabaseReference userssRef = FirebaseDatabase.getInstance().getReference().child("users");

    public static void searchByPhone(String phone) {
        String countryCode = MainApplication.instance.country;
        phone = ContactProvider.instance.getPhoneNumber(phone, countryCode);

        userssRef.orderByChild("phoneNumber").startAt(phone).endAt(phone)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Profile> result = new ArrayList<>();
                for( DataSnapshot child : dataSnapshot.getChildren() ) {
                    Profile profile = child.getValue(Profile.class);
                    profile.setUid(child.getKey());
                    result.add(profile);
                }
                EventBus.getDefault().post( new SearchQueryFirebaseResultEvent(result) );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                EventBus.getDefault().post( new SearchQueryFirebaseResultEvent() );
            }
        });
    }

    public static void searchByName(String text) {
        text = text.toLowerCase();
        Query query = userssRef.orderByChild("nameForSearch").startAt(text, "nameForSearch").endAt(text + "\uf8ff");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Profile> result = new ArrayList<>();
                for( DataSnapshot child : dataSnapshot.getChildren() ) {
                    Profile profile = child.getValue(Profile.class);
                    profile.setUid(child.getKey());
                    result.add(profile);
                }
                EventBus.getDefault().post( new SearchQueryFirebaseResultEvent(result) );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                EventBus.getDefault().post( new SearchQueryFirebaseResultEvent() );
            }
        });
    }

}
