package wumf.com.sharedapps.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.github.omadahealth.typefaceview.TypefaceTextView;

import java.util.HashMap;
import java.util.Map;

import wumf.com.sharedapps.R;

/**
 * Created by max on 03.08.16.
 */
public class CustomTopBar extends LinearLayout {

    private TypefaceTextView xTextView;
    private ImageButton back;
    private Map<Integer, ImageButton> imageButtonsMap = new HashMap<>();
    private View backOpposite;

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
        setGravity(Gravity.CENTER);
        xTextView = (TypefaceTextView) findViewById(R.id.text_view);
        back = (ImageButton) findViewById(R.id.back);
        backOpposite = findViewById(R.id.back_opposite);
    }

    public CustomTopBar setText(String text) {
        xTextView.setText(text);
        return this;
    }

    public CustomTopBar bind(final Activity activity) {
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        return this;
    }

    public CustomTopBar unbind() {
        back.setOnClickListener(null);
        return this;
    }

    public CustomTopBar addNewImage(@DrawableRes int imageId, boolean withAnimation, OnClickListener listener) {
        ImageButton imageButton = (ImageButton) View.inflate(getContext(), R.layout.view_image_button, null);
        imageButton.setImageResource(imageId);
        imageButton.setOnClickListener(listener);
        addView(imageButton);;
        imageButtonsMap.put(imageId, imageButton);
        backOpposite.setVisibility(View.GONE);

        if (withAnimation) {
            ScaleAnimation anim = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(200);
            anim.setFillAfter(true);
            imageButton.startAnimation(anim);
        }

        return this;
    }

    public CustomTopBar removeImage(@DrawableRes int imageId) {
        ImageButton imageButton = imageButtonsMap.get(imageId);
        if (imageButton != null) {
            imageButtonsMap.remove(imageId);
        }
        if (imageButtonsMap.size() == 0) {
            backOpposite.setVisibility(View.INVISIBLE);
        }
        removeView(imageButton);
        return this;
    }

    public boolean hasImage(@DrawableRes int imageId) {
        return imageButtonsMap.containsKey(imageId);
    }

}
