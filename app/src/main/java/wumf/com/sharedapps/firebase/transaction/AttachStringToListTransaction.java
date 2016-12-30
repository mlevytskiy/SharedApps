package wumf.com.sharedapps.firebase.transaction;

import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.Collections;
import java.util.List;

import wumf.com.sharedapps.firebase.transaction.common.AnyTransaction;

/**
 * Created by max on 08.12.16.
 */

public class AttachStringToListTransaction extends AnyTransaction {

    private String str;

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

}
