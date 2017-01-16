package wumf.com.sharedapps.view.findAndFollowSearchBarImpl;

import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.omadahealth.typefaceview.TypefaceTextView;

import wumf.com.sharedapps.util.KeyboardUtils;

/**
 * Created by max on 15.01.17.
 */

public abstract class AnyOnClickSpan extends ClickableSpan {

    private TextView choiceTextView;
    private ImageButton cancel;
    private EditText editText;
    private TypefaceTextView search;

    public AnyOnClickSpan(TextView choiceTextView, ImageButton cancel, EditText editText,
                          TypefaceTextView search) {
        this.choiceTextView = choiceTextView;
        this.cancel = cancel;
        this.editText = editText;
        this.search = search;
    }

    protected final void onClick(View view, String text) {
        view.invalidate();
        editText.setHint(text);
        KeyboardUtils.showKeyboard(editText);
        choiceTextView.setVisibility(View.GONE);
        cancel.setVisibility(View.VISIBLE);
        editText.setVisibility(View.VISIBLE);
        search.setVisibility(View.VISIBLE);
    }

}