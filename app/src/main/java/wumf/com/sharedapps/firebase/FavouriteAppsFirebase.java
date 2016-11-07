package wumf.com.sharedapps.firebase;

import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import wumf.com.sharedapps.eventbus.ChangeAllFoldersFromFirebaseEvent;
import wumf.com.sharedapps.firebase.pojo.AppOrFolder;

/**
 * Created by max on 22.09.16.
 */

public class FavouriteAppsFirebase {

    public static final String DELIMITER_FOR_FOLDER = "|";

    private static DatabaseReference mainRef = FirebaseDatabase.getInstance().getReference().child("users");

    public static void addApp() {

    }

    public static void getNewFolderName(final String uid, final GetNewFolderNameCallback callback) {
        mainRef.child(uid).child("nextfoldername").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot == null || dataSnapshot.getValue() == null) {
                    callback.newFolderName("New Folder");
                } else {
                    String folderName = dataSnapshot.getValue().toString();
                    callback.newFolderName(folderName);
                }
                mainRef.child(uid).child("nextfoldername").removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void addFolder(String uid, String path) {
        String lastFolderName= "";
        if (path.contains(DELIMITER_FOR_FOLDER)) { //TODO: folder inside another folder!
            String[] folders = path.split(DELIMITER_FOR_FOLDER);
            DatabaseReference ref = mainRef.child(uid).child("apps");
            for (String folderName : folders) {
                ref = ref.child(folderName);
                lastFolderName = folderName;
            }
            ref.setValue("folder");
        } else {
            lastFolderName = path;
            mainRef.child(uid).child("apps").child(lastFolderName).setValue(new AppOrFolder(lastFolderName));
        }
        if (lastFolderName.startsWith("New Folder")) {
            String nextFolderName;
            if ( TextUtils.equals("New Folder", lastFolderName) ) {
                nextFolderName = "New Folder 1";
            } else {
                String[] tmp = lastFolderName.split(" ");
                String numberStr = tmp[tmp.length-1];
                int number = Integer.parseInt(numberStr);
                nextFolderName = "New Folder " + (++number);
            }
            mainRef.child(uid).child("nextfoldername").setValue(nextFolderName);
        }
    }

    public static void listenFolders(String uid) {
        Log.i("test", "listenFolders=" + uid);
        FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("apps").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();
                if (count != 0) {
                    List<String> folders = new ArrayList<String>();
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        AppOrFolder appOrFolder = child.getValue(AppOrFolder.class);
                        if ( !TextUtils.isEmpty(appOrFolder.getFolderName()) ) {
                            folders.add(appOrFolder.getFolderName());
                        } else {
                            //do nothing
                        }
                    }
                    EventBus.getDefault().post(new ChangeAllFoldersFromFirebaseEvent(folders));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void renameFolder(String path) {

    }

    public static void removeFolder(String path) {

    }

}
