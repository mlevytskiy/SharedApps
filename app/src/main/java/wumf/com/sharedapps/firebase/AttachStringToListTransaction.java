package wumf.com.sharedapps.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.Collections;
import java.util.List;

/**
 * Created by max on 08.12.16.
 */

public class AttachStringToListTransaction implements Transaction.Handler {

    private String str;
    private TransactionResult tr;

    public AttachStringToListTransaction(String str) {
        this.str = str;
    }

    @Override
    public Transaction.Result doTransaction(MutableData mutableData) {
        Object value = mutableData.getValue();
        if (value == null) {
            mutableData.setValue(Collections.singletonList(str));
        } else if (value instanceof List) {
            List<String> list = (List<String>) value;
            if (list.contains(str)) {
                return Transaction.abort();
            }
            list.add(str);
            mutableData.setValue(list);
        }
        return Transaction.success(mutableData);
    }

    public void setTransactionResult(TransactionResult tr) {
        this.tr = tr;
    }

    @Override
    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
        if (tr == null) {
            return;
        }

        if (databaseError == null) {
            tr.onSuccess();
        } else {
            tr.onError();
        }
    }
}
