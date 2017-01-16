package wumf.com.sharedapps.view.findAndFollowSearchBarImpl;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.omadahealth.typefaceview.TypefaceTextView;

/**
 * Created by max on 15.01.17.
 */
public class OnClickCancel implements View.OnClickListener {

    private TextView choiceTextView;
    private ImageButton cancel;
    private EditText editText;
    private TypefaceTextView search;

    public OnClickCancel(TextView choiceTextView, ImageButton cancel, EditText editText, TypefaceTextView search) {
        this.choiceTextView = choiceTextView;
        this.cancel = cancel;
        this.editText = editText;
        this.search = search;
    }

    @Override
    public void onClick(View view) {
        FindAndFollowSearchBarUtils.resetState(choiceTextView, cancel, editText, search);
    }

}
