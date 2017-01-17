package wumf.com.sharedapps.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.omadahealth.typefaceview.TypefaceTextView;

import wumf.com.sharedapps.R;
import wumf.com.sharedapps.view.findAndFollowSearchBarImpl.NickNameOnClickSpan;
import wumf.com.sharedapps.view.findAndFollowSearchBarImpl.OnClickCancel;
import wumf.com.sharedapps.view.findAndFollowSearchBarImpl.PhoneOnClickSpan;
import wumf.com.sharedapps.view.findAndFollowSearchBarImpl.FindAndFollowSearchBarUtils;

/**
 * Created by max on 15.01.17.
 */

public class FindAndFollowSearchBar extends LinearLayout {

    private TextView choiceTextView;
    private ImageButton cancel;
    private EditText editText;
    private TypefaceTextView search;

    public FindAndFollowSearchBar(Context context) {
        super(context);
        init(context);
    }

    public FindAndFollowSearchBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FindAndFollowSearchBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.custom_item_find_and_follow_search_bar, this);
        setPadding(0, 0, 8, 0);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        choiceTextView = (TextView) findViewById(R.id.choice_text_view);
        cancel = (ImageButton) findViewById(R.id.cancel_image_button);
        editText = (EditText) findViewById(R.id.edit_text);
        search = (TypefaceTextView) findViewById(R.id.search_button);

        cancel.setOnClickListener(new OnClickCancel(choiceTextView, cancel, editText, search));

        Spannable spannable = Spannable.Factory.getInstance().newSpannable("Search by phone or nick name");

        spannable.setSpan(new PhoneOnClickSpan(choiceTextView, cancel, editText, search), 10, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannable.setSpan(new NickNameOnClickSpan(choiceTextView, cancel, editText, search), 19, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        choiceTextView.setText(spannable);
        choiceTextView.setClickable(false);
        choiceTextView.setMovementMethod(LinkMovementMethod.getInstance());

        choiceTextView.setVisibility(View.VISIBLE);
        cancel.setVisibility(View.GONE);
        editText.setVisibility(View.GONE);
        search.setVisibility(View.GONE);

    }

    public boolean onBackPressed() {
        boolean result = choiceTextView.getVisibility() != View.VISIBLE;
        if (result) {
            FindAndFollowSearchBarUtils.resetState(choiceTextView, cancel, editText, search);
        }
        return result;
    }

}
