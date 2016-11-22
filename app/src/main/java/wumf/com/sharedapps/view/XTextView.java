package wumf.com.sharedapps.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.AttributeSet;

import wumf.com.sharedapps.R;

/**
 * Created by max on 06.08.16.
 */
public class XTextView extends com.github.omadahealth.typefaceview.TypefaceTextView {

    public XTextView(Context context) {
        super(context);
    }

    public XTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void set(String country, String city, int usersCount) {
        String usersCountStr = String.valueOf(usersCount);
        city = " " + city + " ";
        country = " " + country + " ";

        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();

        RoundedBackgroundSpan roundedBackgroundSpan = new RoundedBackgroundSpan(Color.parseColor("#f7e0e3"),
                getResources().getDimension(R.dimen.round_corner));
        float countrySize = getPaint().measureText(country);
        float spaceSize = getPaint().measureText(" ");
        roundedBackgroundSpan.addRect(spaceSize/2, countrySize + spaceSize/2 + spaceSize);
        roundedBackgroundSpan.addRect(countrySize + spaceSize/2 + 2*spaceSize, countrySize + spaceSize/2 + 3*spaceSize + getPaint().measureText(city));

        float usersCountStart = countrySize + spaceSize/2 + 4*spaceSize + getPaint().measureText(city);
        roundedBackgroundSpan.addRect(usersCountStart+spaceSize/2, usersCountStart + getLineHeight() + getPaint().measureText(usersCountStr) + 3*spaceSize);

        stringBuilder.append(" ");
        stringBuilder.setSpan(roundedBackgroundSpan, 0, 1, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);

        stringBuilder.append(" ");
        stringBuilder.append(country);
        stringBuilder.append(" ");
        stringBuilder.append(" ");
        stringBuilder.append(city);

        stringBuilder.append(" ");
        stringBuilder.append(" ");
        stringBuilder.append(" ");
        int startIndexForCount = stringBuilder.length();
        stringBuilder.append(" ");
        stringBuilder.append(usersCountStr);
        stringBuilder.append(" ");
        stringBuilder.append(" ");

        Drawable dr = getResources().getDrawable(R.drawable.ic_person);
        dr.setBounds(0, 0, getLineHeight(), getLineHeight());
        dr.setColorFilter(getCurrentTextColor(), PorterDuff.Mode.SRC_ATOP);
        ImageSpan imageSpan = new ImageSpan(dr);
        stringBuilder.setSpan( imageSpan, startIndexForCount, startIndexForCount+1, SpannableStringBuilder.SPAN_INCLUSIVE_EXCLUSIVE );

        setText(stringBuilder);
    }

}
