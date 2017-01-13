package wumf.com.sharedapps.firebase.observable;

import android.text.TextUtils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import wumf.com.sharedapps.eventbus.CurrentUserChangedEvent;
import wumf.com.sharedapps.eventbus.ReceivedPushEvent;
import wumf.com.sharedapps.eventbus.observable.ObservableChangeProfileEvent;
import wumf.com.sharedapps.eventbus.observable.ObservableGarbageEvent;
import wumf.com.sharedapps.eventbus.observable.ObservablePeopleEvent;
import wumf.com.sharedapps.eventbus.observable.ObservableRemoveProfileEvent;
import wumf.com.sharedapps.firebase.pojo.Profile;

/**
 * Created by max on 11.01.17.
 */

public class ObservablePeopleFirebase {

    private static final DatabaseReference userssRef = FirebaseDatabase.getInstance().getReference().child("users");

    private static ObservablePeopleFirebase instance = new ObservablePeopleFirebase();
    private static List<String> pn = new ArrayList<>();

    private static Map<String, Profile> inGarbage = new HashMap<>();
    private static List<Profile> people = new ArrayList<>();
    private static List<String> myTags = new ArrayList<>();

    private static MyGarbageValueEventListener myGarbageValueEventListener;
    private static MyTagsValueEventListener myTagsValueEventListener;

    public static void setPhoneNumbers(List<String> phoneNumbers) {
        pn = phoneNumbers;
    }

    public static void listenPeople(String myUid) {
        if ( !EventBus.getDefault().isRegistered(instance) ) {
            EventBus.getDefault().register(instance);
        }

        if (TextUtils.isEmpty(myUid)) {
            return;
        }

        initListeners(myUid);
    }

    @Subscribe
    public void onEvent(CurrentUserChangedEvent event) {
        if (event.firebaseUser == null) {
            //do nothing
        } else {
            if (myGarbageValueEventListener == null) {
                String myUid = event.firebaseUser.getUid();
                initListeners(myUid);
            }
        }
    }

    @Subscribe
    public void onEvent(ReceivedPushEvent event) {
        if (inGarbage.get(event.uid) == null) {
            userssRef.child(event.uid).addValueEventListener( new UserValueEventListener(event.uid, myTags, pn, people, userssRef.child(event.uid), false) );
        } else {
            //do nothing
        }
    }

    private static void initListeners(String uid) {
        myGarbageValueEventListener = new MyGarbageValueEventListener();
        myTagsValueEventListener = new MyTagsValueEventListener(uid, pn);
        userssRef.child(uid).child("garbage").addValueEventListener(myGarbageValueEventListener);
        userssRef.child(uid).child("myTags").addValueEventListener(myTagsValueEventListener);
    }

    public static List<Profile> getPeople() {
        return people;
    }

    public static List<Profile> getGarbage() {
        return new ArrayList<>(inGarbage.values());
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
            myTags.clear();
            myTags.addAll(tags);
            userssRef.addListenerForSingleValueEvent(new UsersValueEventListener(tags, phoneNumber, myUid));
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

    private static class UsersValueEventListener implements ValueEventListener {

        private String myUid;
        private List<String> tags;
        private List<String> phoneNumbers;

        public UsersValueEventListener(List<String> tags, List<String> phoneNumbers, String myUid) {
            this.myUid = myUid;
            this.tags = tags;
            this.phoneNumbers = phoneNumbers;
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
                List<String> personTags = (person.getMyTags() == null) ? new ArrayList<String>() : person.getMyTags();
                boolean hasTheSameTag = false;
                for (String tag : personTags) {
                    if (tags.contains(tag)) {
                        result.add(person);
                        userssRef.child(person.getUid()).addValueEventListener( new UserValueEventListener(person.getUid(), tags, phoneNumbers, people, userssRef.child(person.getUid())) );
                        hasTheSameTag = true;
                        break;
                    }
                }

                if (hasTheSameTag) {
                    continue;
                }

                if ( phoneNumbers.contains(person.getPhoneNumber()) ) {
                    result.add(person);
                    userssRef.child(person.getUid()).addValueEventListener( new UserValueEventListener(person.getUid(), tags, phoneNumbers, people, userssRef.child(person.getUid())) );
                }

            }

            result.removeAll(inGarbage.values());
            people.clear();
            people.addAll(result);
            EventBus.getDefault().post(new ObservablePeopleEvent(people));
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
        private List<Profile> people;

        public UserValueEventListener(String uid, List<String> tags, List<String> phones, List<Profile> people, DatabaseReference parentRef) {
            this.uid = uid;
            this.tags = tags;
            this.phones = phones;
            this.parentRef = parentRef;
            this.people = people;
        }

        public UserValueEventListener(String uid, List<String> tags, List<String> phones, List<Profile> people, DatabaseReference parentRef,
                                      boolean ignoreFirstCall) {
            this(uid, tags, phones, people, parentRef);
            isFirstCall = false;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (isFirstCall) {
                isFirstCall = false;
                return; //ignore
            }
            Profile profile = dataSnapshot.getValue(Profile.class);
            if (profile == null) {
                Profile pr = new Profile();
                pr.setUid(uid);
                people.remove(pr);
                parentRef.removeEventListener(this);
                EventBus.getDefault().post( new ObservableRemoveProfileEvent(uid));
                return;
            }
            profile.setUid(uid);
            if ( phones.contains(profile.getPhoneNumber()) || hasTheSameTag(profile.getMyTags(), tags)) {
                people.remove(profile); //existed profile with the same uid;
                people.add(profile);
                EventBus.getDefault().post( new ObservableChangeProfileEvent(profile) );
            } else {
                parentRef.removeEventListener(this);
                people.remove(profile);
                EventBus.getDefault().post( new ObservableRemoveProfileEvent(uid));
            }

        }

        private boolean hasTheSameTag(List<String> tags, List<String> myTags) {
            if (tags == null || tags.isEmpty() || myTags == null || myTags.isEmpty()) {
                return false;
            }
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

    private static class MyGarbageValueEventListener implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Object value = dataSnapshot.getValue();
            List<String> uids = (value == null) ? new ArrayList<String>() : (List<String>) value;

            for (Profile profile : new ArrayList<>(people)) {
                if (uids.contains(profile.getUid())) {
                    inGarbage.put(profile.getUid(), profile);
                    people.remove(profile);
                }
            }

            for (Map.Entry<String, Profile> entry : new HashSet<>(inGarbage.entrySet())) {
                if ( !uids.contains(entry.getKey()) ) {
                    people.add(entry.getValue());
                    inGarbage.remove(entry.getKey());
                }
            }

            EventBus.getDefault().post(new ObservableGarbageEvent(new ArrayList(inGarbage.values()), people));

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }

    }

}
