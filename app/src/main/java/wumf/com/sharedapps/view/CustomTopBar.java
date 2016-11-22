package wumf.com.sharedapps.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.github.omadahealth.typefaceview.TypefaceTextView;

import wumf.com.sharedapps.R;

/**
 * Created by max on 03.08.16.
 */
public class CustomTopBar extends LinearLayout {

    private TypefaceTextView xTextView;
    private ImageButton back;

    public CustomTopBar(Context context) {
        super(context);
        init(context);
    }

    public CustomTopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomTopBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setBackgroundColor(Color.WHITE);
        setOrientation(HORIZONTAL);
        inflate(context, R.layout.view_custom_top_bar, this);
        xTextView = (TypefaceTextView) findViewById(R.id.text_view);
        back = (ImageButton) findViewById(R.id.back);
    }

    public CustomTopBar setText(String text) {
        xTextView.setText(text);
        return this;
    }

    public void bind(final Activity activity) {
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }

    public void unbind() {
        back.setOnClickListener(null);
    }

}
