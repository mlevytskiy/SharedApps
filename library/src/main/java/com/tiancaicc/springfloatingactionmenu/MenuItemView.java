package com.tiancaicc.springfloatingactionmenu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.ui.Util;
import com.tumblr.backboard.imitator.ToggleImitator;
import com.tumblr.backboard.performer.MapPerformer;
import com.tumblr.backboard.performer.Performer;

import java.io.File;


/**
 * Copyright (C) 2016 tiancaiCC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class MenuItemView extends LinearLayout implements OnMenuActionListener {

    private static final String TAG = "MIV";
    private ImageButton mBtn;
    private com.github.omadahealth.typefaceview.TypefaceTextView mLabel;
    private MenuItem mMenuItem;
    private static int mGapSize = 4;
    private static int mTextSize = 14;
    private int mDiameter;
    private boolean mAlphaAnimation = true;

    public MenuItemView(Context context, MenuItem menuItem, boolean hasMargin) {
        super(context);
        this.mMenuItem = menuItem;
        init(context, hasMargin);
    }

    private void init(Context context, boolean hasMargin) {

        Resources resources = getResources();
        int diameterPX = Utils.dpToPx(mMenuItem.getDiameter(), resources);
        this.mDiameter = diameterPX;
        mBtn = new ImageButton(context);
        mBtn.setScaleType(ImageView.ScaleType.FIT_CENTER);
        if (hasMargin) {
            int p = Util.dpToPx(12, getResources());
            mBtn.setPadding(p, p, p, p);
        }
        LayoutParams btnLp = new LayoutParams(diameterPX, diameterPX);
        btnLp.gravity = Gravity.CENTER_HORIZONTAL;
        btnLp.bottomMargin = Util.dpToPx(mGapSize, resources);
        mBtn.setLayoutParams(btnLp);
        OvalShape ovalShape = new OvalShape();
        ShapeDrawable shapeDrawable = new ShapeDrawable(ovalShape);
        shapeDrawable.getPaint().setColor(resources.getColor(mMenuItem.getBgColor()));
        mBtn.setBackgroundDrawable(shapeDrawable);
        if (TextUtils.isEmpty(mMenuItem.getIconFilePath())) {
            mBtn.setImageResource(mMenuItem.getIcon());
        } else {
            mBtn.setImageURI( Uri.fromFile(new File(mMenuItem.getIconFilePath())) );
        }
        mBtn.setClickable(false);
        addView(mBtn);

        mLabel = (com.github.omadahealth.typefaceview.TypefaceTextView) View.inflate(getContext(), R.layout.c_text_view, null);
        mLabel.setPaintFlags(mLabel.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        mLabel.setTextIsSelectable(false);
        LayoutParams labelLp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        labelLp.gravity = Gravity.CENTER_HORIZONTAL;
        mLabel.setLayoutParams(labelLp);
        if (!TextUtils.isEmpty(mMenuItem.getLabel())) {
            mLabel.setPadding(0, Util.dpToPx(90, getResources()), 0, 0);
        }
        mLabel.setText(mMenuItem.getLabel());
        mLabel.setTextColor(resources.getColor(mMenuItem.getTextColor()));
//        mLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextSize);
        addView(mLabel);

        setOrientation(LinearLayout.VERTICAL);
        if(mAlphaAnimation) {
            setAlpha(0);
        }

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
                applyPressAnimation();
                ViewGroup parent = (ViewGroup) getParent();
                parent.setClipChildren(false);
                parent.setClipToPadding(false);
                setClipChildren(false);
                setClipToPadding(false);
            }
        });


    }

    public void setListener(final OpenAllAppsListener listener) {
        mLabel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick();
            }
        });
    }

    public void updateIcon() {
        if (TextUtils.isEmpty(mMenuItem.getIconFilePath())) {
            mBtn.setImageResource(mMenuItem.getIcon());
        } else {
            mBtn.setImageURI( Uri.fromFile(new File(mMenuItem.getIconFilePath())) );
        }
        mBtn.invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(
                Math.max(mBtn.getMeasuredWidth(), mLabel.getMeasuredWidth()),
                mBtn.getMeasuredHeight() + mLabel.getMeasuredHeight() + Util.dpToPx(4, getResources())
        );
    }


    private void applyPressAnimation() {
        SpringSystem springSystem = SpringSystem.create();
        final Spring spring = springSystem.createSpring();
        spring.addListener(new Performer(mBtn, View.SCALE_X));
        spring.addListener(new Performer(mBtn, View.SCALE_Y));
        mBtn.setOnTouchListener(new ToggleImitator(spring, 1, 1.2){
            @Override
            public void imitate(MotionEvent event) {
                super.imitate(event);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;

                    case MotionEvent.ACTION_UP:
                        callOnClick();
                        break;

                    default:
                }
            }
        });
        spring.setCurrentValue(1);
    }

    public ImageButton getButton() {
        return mBtn;
    }

    public TextView getLabelTextView() {
        return mLabel;
    }

    public int getDiameter() {
        return mDiameter;
    }

    public void showLabel() {
        SpringSystem springSystem = SpringSystem.create();
        final Spring spring = springSystem.createSpring();
        spring.addListener(new MapPerformer(mLabel, View.SCALE_X, 0, 1));
        spring.addListener(new MapPerformer(mLabel, View.SCALE_Y, 0, 1));
        spring.setCurrentValue(0);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                spring.setEndValue(1);
            }
        }, 200);
    }

    public void disableAlphaAnimation(){
        mAlphaAnimation = false;
        setAlpha(1);
    }

    @Override
    public void onMenuOpen() {
        showLabel();
        if(mAlphaAnimation) {
            animate().alpha(1).setDuration(120).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    Log.d(TAG,"now enable clickListener");
                    MenuItemView.this.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mMenuItem.getOnClickListener().onClick(mMenuItem.packageName);
                        }
                    });
                }
            });
        }else {
            MenuItemView.this.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMenuItem.getOnClickListener().onClick(mMenuItem.packageName);
                }
            });
        }
    }

    @Override
    public void onMenuClose() {
        if(mAlphaAnimation) {
            animate().alpha(0).setDuration(120).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    Log.d(TAG,"now disable clickListener");
                    MenuItemView.this.setOnClickListener(null);
                }
            });
        }else {
            MenuItemView.this.setOnClickListener(null);
        }
    }
}
