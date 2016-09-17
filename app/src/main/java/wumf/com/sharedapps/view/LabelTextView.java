package wumf.com.sharedapps.view;

import android.content.Context;
import android.util.AttributeSet;

import wumf.com.appsprovider.App;


/**
 * Created by max on 30.03.16.
 */
public class LabelTextView extends com.github.omadahealth.typefaceview.TypefaceTextView {

    public LabelTextView(Context context) {
        super(context);
    }

    public LabelTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LabelTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setApp(App app) {
        setText(app.name);
    }

}
