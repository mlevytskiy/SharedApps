package wumf.com.sharedapps.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by max on 25.02.17.
 */

public class TouchyRecyclerView extends RecyclerView {

    public TouchyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchyRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TouchyRecyclerView(Context context) {
        super(context);
    }

    private OnNoChildClickListener listener;

    public interface OnNoChildClickListener {
        void onNoChildClick();
    }

    public void setOnNoChildClickListener(OnNoChildClickListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        // The findChildViewUnder() method returns null if the touch event
        // occurs outside of a child View.
        // Change the MotionEvent action as needed. Here we use ACTION_DOWN
        // as a simple, naive indication of a click.
        if (event.getAction() == MotionEvent.ACTION_DOWN
                && findChildViewUnder(event.getX(), event.getY()) == null) {
            if (listener != null) {
                listener.onNoChildClick();
            }
        }
        return super.dispatchTouchEvent(event);
    }
}

