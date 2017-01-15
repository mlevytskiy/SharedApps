package wumf.com.sharedapps.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import wumf.com.sharedapps.R;

/**
 * Created by max on 15.01.17.
 */

public class FindAndFollowSearchBar extends LinearLayout {

    private TextView choiceTextView;
    private ImageView cancel;
    private EditText editText;
    private Button search;

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
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        choiceTextView = (TextView) findViewById(R.id.choice_text_view);
        Spannable spannable = Spannable.Factory.getInstance().newSpannable("Search by phone or nick name");

        spannable.setSpan(new ClickableSpan() {

            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "test", Toast.LENGTH_LONG).show();
            }
        }, 10, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannable.setSpan(new ClickableSpan() {

            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "test2", Toast.LENGTH_LONG).show();
            }
        }, 19, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        choiceTextView.setText(spannable);
        choiceTextView.setClickable(false);
        choiceTextView.setMovementMethod(LinkMovementMethod.getInstance());

            cancel = (ImageView) findViewById(R.id.cancel_image_view);
        editText = (EditText) findViewById(R.id.edit_text);
        search = (Button) findViewById(R.id.search_button);

        choiceTextView.setVisibility(View.VISIBLE);
        cancel.setVisibility(View.GONE);
        editText.setVisibility(View.GONE);
        search.setVisibility(View.GONE);
    }

}
