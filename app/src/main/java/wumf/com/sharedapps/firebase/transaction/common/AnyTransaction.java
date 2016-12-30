package wumf.com.sharedapps.firebase.transaction.common;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Transaction;

import wumf.com.sharedapps.firebase.TransactionResultListener;

/**
 * Created by max on 30.12.16.
 */

public abstract class AnyTransaction implements Transaction.Handler {

    private TransactionResultListener listener;

    @Override
    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
        if (listener == null) {
            return;
        }
        if (databaseError == null) {
            listener.onSuccess();
        } else {
            listener.onError();
        }
    }

    public void setTransactionResultListener(TransactionResultListener listener) {
        this.listener = listener;
    }

}
