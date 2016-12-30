package wumf.com.sharedapps.firebase.transaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

/**
 * Created by max on 30.12.16.
 */

public class CompositeTransaction implements Transaction.Handler {

    private Transaction.Handler[] transactions;

    public CompositeTransaction(Transaction.Handler... transactions) {
        this.transactions = transactions;
    }

    @Override
    public Transaction.Result doTransaction(MutableData mutableData) {
        Transaction.Result result = Transaction.abort();
        for (Transaction.Handler tr : transactions) {
            if (tr.doTransaction(mutableData) != Transaction.abort()) {
                result = Transaction.success(mutableData);
            }
        }
        return result;
    }

    @Override
    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
        for (Transaction.Handler tr : transactions) {
            tr.onComplete(databaseError, b, dataSnapshot);
        }
    }

}
