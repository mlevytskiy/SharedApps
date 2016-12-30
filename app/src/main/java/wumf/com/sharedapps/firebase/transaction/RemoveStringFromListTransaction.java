package wumf.com.sharedapps.firebase.transaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.List;

import wumf.com.sharedapps.firebase.TransactionResult;

/**
 * Created by max on 08.12.16.
 */

public class RemoveStringFromListTransaction implements Transaction.Handler {

    private String str;
    private TransactionResult tr;

    public RemoveStringFromListTransaction(String str) {
        this.str = str;
    }

    @Override
    public Transaction.Result doTransaction(MutableData mutableData) {
        Object value = mutableData.getValue();
        if (value == null) {
            return Transaction.abort();
        } else if (value instanceof List) {
            List<String> list = (List<String>) value;
            if (!list.contains(str)) {
                return Transaction.abort();
            } else {
                list.remove(str);
            }
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
