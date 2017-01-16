package wumf.com.sharedapps.view.findAndFollowSearchBarImpl;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.omadahealth.typefaceview.TypefaceTextView;

import wumf.com.sharedapps.util.KeyboardUtils;

/**
 * Created by max on 17.01.17.
 */

public class FindAndFollowSearchBarUtils {

    public static void resetState(TextView choiceTextView, ImageButton cancel, EditText editText, TypefaceTextView search) {
        KeyboardUtils.hideKeyboard(editText);
        editText.getText().clear();
        choiceTextView.setVisibility(View.VISIBLE);
        cancel.setVisibility(View.GONE);
        editText.setVisibility(View.GONE);
        search.setVisibility(View.GONE);
    }

}
