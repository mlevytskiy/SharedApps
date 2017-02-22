package wumf.com.sharedapps.util;

import android.view.View;

/**
 * Created by Макс on 06.04.2014.
 */
public class FlipStateAnimator {

    private View mView;
    private View mCardFace;
    private View mCardBack;
    private boolean isBack = false;

    public FlipStateAnimator(View view, int cardFaceId, int cardBackId) {
        this(view, view.findViewById(cardFaceId), view.findViewById(cardBackId));
    }

    public FlipStateAnimator(View view, View face, View back) {
        mView = view;
        mCardFace = face;
        mCardBack = back;
    }

    public void changeStateWithAnimation(boolean isBack) {
        this.isBack = isBack;
        flipCard(mView, mCardFace, mCardBack);
    }

    private void flipCard(View rootView, View cardFace, View cardBack) {
        View rootLayout = mView;//(View) rootView.findViewById(R.id.main_activity_root);
        FlipAnimation flipAnimation = new FlipAnimation(cardFace, cardBack);
        flipAnimation.setBack(isBack);

        if (cardFace.getVisibility() == View.GONE) {
            flipAnimation.reverse();
        }
        rootLayout.startAnimation(flipAnimation);
    }
}
