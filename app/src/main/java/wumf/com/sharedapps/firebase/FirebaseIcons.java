package wumf.com.sharedapps.firebase;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import wumf.com.sharedapps.util.TagsBuilder;

/**
 * Created by max on 16.11.16.
 */

public class FirebaseIcons {

    private static final String TAG = new TagsBuilder().add(FirebaseIcons.class).add("firebase").build();

    public static void getIconUrl(final String packageName, final IconUrlCallback callback,
                                  final String iconFilePath) {
        Log.i(TAG, "getIconUrl( packageName=" + packageName + "iconFilePath=" + iconFilePath + " )");
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference(packageName + ".webp");
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                callback.receive(uri.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                uploadIcon(callback, storageReference, iconFilePath);
            }
        });
    }

    private static void uploadIcon(final IconUrlCallback callback, StorageReference storageReference,
                                   String iconFilePath) {
        Log.i(TAG, "uploadIcon( iconFilePath=" + iconFilePath + " )");
        UploadTask uploadTask = storageReference.putFile(Uri.fromFile(new File(iconFilePath)));
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //do nothing
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                callback.receive(taskSnapshot.getDownloadUrl().toString());
            }
        });

    }

}
