package wumf.com.sharedapps.firebase.transaction;

import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.List;

import wumf.com.sharedapps.firebase.transaction.common.AnyTransaction;

/**
 * Created by max on 30.12.16.
 */

public class ForChildrenTransaction extends AnyTransaction {

    private Transaction.Handler transaction;
    private List<String> children;

    public ForChildrenTransaction(Transaction.Handler transaction, List<String> children) {
        this.transaction = transaction;
        this.children = children;
    }

    @Override
    public Transaction.Result doTransaction(MutableData mutableData) {
        Transaction.Result result = Transaction.abort();
        for (String child : children) {
            if (transaction.doTransaction(mutableData.child(child)) != Transaction.abort()) {
                result = Transaction.success(mutableData);
            }
        }
        return result;
    }

}
