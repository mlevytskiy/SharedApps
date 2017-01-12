package wumf.com.sharedapps.firebase.observable;

import android.text.TextUtils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import wumf.com.sharedapps.eventbus.observable.ObservableChangeProfileEvent;
import wumf.com.sharedapps.eventbus.observable.ObservablePeopleEvent;
import wumf.com.sharedapps.eventbus.observable.ObservableRemoveProfileEvent;
import wumf.com.sharedapps.firebase.pojo.Profile;

/**
 * Created by max on 11.01.17.
 */

public class ObservablePeopleFirebase {

    private static DatabaseReference userssRef = FirebaseDatabase.getInstance().getReference().child("users");

    private static List<String> pn = new ArrayList<>();

    public static void setPhoneNumbers(List<String> phoneNumbers) {
        pn = phoneNumbers;
    }

    public static void listenPeople(String myUid) {
        userssRef.child(myUid).child("myTags").addValueEventListener(new MyTagsValueEventListener(myUid, pn));
    }

    public static List<Profile> getPeople() {
        return new ArrayList<>();
    }

    private static class MyTagsValueEventListener implements ValueEventListener {

        private String myUid;
        private List<String> phoneNumber;

        public MyTagsValueEventListener(String myUid, List<String> phoneNumber) {
            this.myUid = myUid;
            this.phoneNumber = phoneNumber;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Object value = dataSnapshot.getValue();
            List<String> tags = (value != null) ? (List<String>) value : new ArrayList<String>();
            userssRef.addListenerForSingleValueEvent(new UsersValueEventListener(tags, phoneNumber, myUid));
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

    private static class UsersValueEventListener implements ValueEventListener {

        private String myUid;
        private List<String> tags;
        private List<String> phoneNumber;

        public UsersValueEventListener(List<String> tags, List<String> phoneNumbers, String myUid) {
            this.myUid = myUid;
            this.tags = tags;
            this.phoneNumber = phoneNumbers;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            List<Profile> result = new ArrayList<>();
            for (DataSnapshot child : dataSnapshot.getChildren()) {
                Profile person = child.getValue(Profile.class);
                person.setUid(child.getKey());
                if (TextUtils.equals(myUid, person.getUid())) {
                    continue;
                }
                List<String> personTags = person.getMyTags();
                boolean hasTheSameTag = false;
                for (String tag : personTags) {
                    if (tags.contains(tag)) {
                        result.add(person);
                        userssRef.child(person.getUid()).addValueEventListener( new UserValueEventListener(person.getUid(), tags, phoneNumber, userssRef.child(person.getUid())) );
                        hasTheSameTag = true;
                        break;
                    }
                }

                if (hasTheSameTag) {
                    continue;
                }

                if ( phoneNumber.contains(person.getPhoneNumber()) ) {
                    result.add(person);
                    userssRef.child(person.getUid()).addValueEventListener( new UserValueEventListener(person.getUid(), tags, phoneNumber, userssRef.child(person.getUid())) );
                }

            }

            EventBus.getDefault().post(new ObservablePeopleEvent(result));
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            //do nothing
        }

    }

    private static class UserValueEventListener implements ValueEventListener {

        private String uid;
        private List<String> tags;
        private List<String> phones;
        private DatabaseReference parentRef;
        private boolean isFirstCall = true;

        public UserValueEventListener(String uid, List<String> tags, List<String> phones, DatabaseReference parentRef) {
            this.uid = uid;
            this.tags = tags;
            this.phones = phones;
            this.parentRef = parentRef;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (isFirstCall) {
                isFirstCall = false;
                return; //ignore
            }
            Profile profile = dataSnapshot.getValue(Profile.class);
            if (profile == null) {
                parentRef.removeEventListener(this);
                EventBus.getDefault().post( new ObservableRemoveProfileEvent(uid));
                return;
            }
            profile.setUid(uid);
            if ( phones.contains(profile.getPhoneNumber()) || hasTheSameTag(profile.getMyTags(), tags)) {
                EventBus.getDefault().post( new ObservableChangeProfileEvent(profile) );
            } else {
                parentRef.removeEventListener(this);
                EventBus.getDefault().post( new ObservableRemoveProfileEvent(uid));
            }
        }

        private boolean hasTheSameTag(List<String> tags, List<String> myTags) {
            for (String tag : tags) {
                if ( myTags.contains(tag) ) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            //do nothing
        }

    }

}
