package wumf.com.sharedapps.view.findAndFollowSearchBarImpl;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.omadahealth.typefaceview.TypefaceTextView;

/**
 * Created by max on 15.01.17.
 */
public class PhoneOnClickSpan extends AnyOnClickSpan {

    public PhoneOnClickSpan(TextView choiceTextView, ImageButton cancel, EditText editText, TypefaceTextView search) {
        super(choiceTextView, cancel, editText, search);
    }

    @Override
    public void onClick(View view) {
        onClick(view, "phone");
    }
}
