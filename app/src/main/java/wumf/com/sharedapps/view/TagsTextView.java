package wumf.com.sharedapps.view;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import wumf.com.sharedapps.R;

/**
 * Created by max on 06.08.16.
 */
public class TagsTextView extends com.github.omadahealth.typefaceview.TypefaceTextView {

    private List<String> tags;
    private OnClickTag onClickTag;

    public TagsTextView(Context context) {
        super(context);
    }

    public TagsTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TagsTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void autocomplete(String text) {

        if (text.length() < 3) {
            setText(null);
            return;
        }

        List<String> newTags = filterTags(text);

        if (newTags.isEmpty()) {
            setText(null);
            return;
        }

        this.setMovementMethod(LinkMovementMethod.getInstance());
        this.setHighlightColor(Color.TRANSPARENT);

        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();

        int currIndex = 0;
        int offset;

        stringBuilder.append(" ");
        RoundedBackgroundSpan roundedBackgroundSpan = new RoundedBackgroundSpan(Color.parseColor("#ececf9"),         //"#f7e0e3"),
                getResources().getDimension(R.dimen.round_corner));
        stringBuilder.setSpan(roundedBackgroundSpan, 0, 1, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);

        for (final String tag : newTags) {
            String prevStr = stringBuilder.toString().substring(1, stringBuilder.length());
            float div = getRectDiv(prevStr);
            float[] rect = getBackgroundRect(tag);
            roundedBackgroundSpan.addRect(rect[0] + div, rect[1] + div);
            stringBuilder.append(" " + tag + " ");
            offset = tag.length() + 2;

            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    if (onClickTag != null) {
                        onClickTag.onClick(tag);
                    }
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(TagsTextView.this.getTextColors().getDefaultColor());
                    ds.setUnderlineText(false);
                }
            };
            stringBuilder.setSpan(clickableSpan, currIndex, currIndex + offset, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            currIndex = currIndex + offset;

        }

        setText(stringBuilder);
    }

    public void setOnTagListener(OnClickTag onTagListener) {
        this.onClickTag = onTagListener;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    private List<String> filterTags(String text) {
        List<String> newTags = new ArrayList<>();
        for (String tag : tags) {
            if (tag.equals(text)) {
                continue;
            }
            if (hasSubstring(text, tag)) {
                newTags.add(0, tag);
            } else if (startWith(text, tag)) {
                newTags.add(tag);
            }
        }
        return newTags;
    }

    private boolean hasSubstring(String text, String tag) {
        String textUpCase = text.toUpperCase();
        if (tag.toUpperCase().contains(textUpCase)) {
            return true;
        }
        return false;
    }

    private boolean startWith(String text, String tag) {
        String textUpCase = text.toUpperCase();
        if (tag.toUpperCase().startsWith(textUpCase)) {
            return true;
        }
        return false;
    }

    private float[] getBackgroundRect(String text) {

        float textSize = getPaint().measureText(text);
        float spaceSize = getPaint().measureText(" ");

        return new float[]{
                spaceSize/2,
                textSize + spaceSize/2 + spaceSize
        };

    }

    private float getRectDiv(String text) {
        return getPaint().measureText(text);
    }

}
