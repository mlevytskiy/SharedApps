package wumf.com.sharedapps.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import hugo.weaving.DebugLog;
import wumf.com.sharedapps.R;

/**
 * Created by max on 18.01.17.
 */

public class FindAndFollowQueryProgress extends LinearLayout {

    private TextView pleaseWait;

    public FindAndFollowQueryProgress(Context context) {
        super(context);
        init(context);
    }

    public FindAndFollowQueryProgress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FindAndFollowQueryProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.view_custom_find_and_follow_query_progress, this);
        setGravity(Gravity.CENTER);
        pleaseWait = (TextView) findViewById(R.id.please_wait);

    }

    @DebugLog
    public void setTopBarHeight(int topBarHeight) {
        setPadding(0, 0, 0, topBarHeight);
    }

    public void setState(State state) {
        pleaseWait.setVisibility( (state == State.DISABLED) ? View.GONE : View.VISIBLE );
    }

    public void setState(State state, String message) {
        setState(state);
        showMessage(state, message);
    }

    private void showMessage(State state, String txt) {

        if (state == State.DISABLED) {
            return;
        }

        String message;
        ForegroundColorSpan foregroundColorSpan;
        int length;

        if (state == State.IN_PROGRESS) {
            message = txt + "\nSearching...\nPlease Wait";
            int blueColor = getResources().getColor(R.color.colorAccent);
            foregroundColorSpan = new ForegroundColorSpan(blueColor);
            length = txt.length();
        } else if (state == State.ERROR) {
            message = txt;
            foregroundColorSpan = new ForegroundColorSpan(Color.RED);
            length = txt.length();
        } else if (state == State.EMPTY_RESULT) {
            pleaseWait.setText(txt);
            return;
        } else {
            throw new NullPointerException("Some fantastic error =)");
        }

        Spannable span = new SpannableString(message);
        span.setSpan(foregroundColorSpan, 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        pleaseWait.setText(span);

    }

    public enum State {
        DISABLED,
        IN_PROGRESS,
        ERROR,
        EMPTY_RESULT
    }

}
