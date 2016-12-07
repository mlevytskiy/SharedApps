package wumf.com.sharedapps.util;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by max on 07.12.16.
 */

public abstract class OnTextChangedListener implements TextWatcher {

    private String old;

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        old = charSequence.toString();
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        onTextChanged(old, editable.toString());
    }

    public abstract void onTextChanged(String oldText, String newText);
}
