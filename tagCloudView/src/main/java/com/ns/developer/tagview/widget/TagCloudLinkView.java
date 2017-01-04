package com.ns.developer.tagview.widget;
/*
 * Copyright 2014 Namito.S
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ns.developer.tagview.R;
import com.ns.developer.tagview.util.AutocompleteHelper;
import com.ns.developer.tagview.util.OnTextChangedListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * TagCloudLinkView Class
 * Simple Tagcloud Widget
 */
public class TagCloudLinkView extends RelativeLayout {

    /**
     * const
     */
    private static final int HEIGHT_WC = ViewGroup.LayoutParams.WRAP_CONTENT;
    private static final int TAG_LAYOUT_TOP_MERGIN = 10;
    private static final int TAG_LAYOUT_LEFT_MERGIN = 10;
    private static final int INNER_VIEW_PADDING = 10;
    private static final int LAYOUT_WIDTH_OFFSET = 5;
    private static final int DEFAULT_TEXT_SIZE = 14;
    private static final int DEFAULT_TAG_LAYOUT_COLOR = Color.parseColor("#aa66cc");
    private static final int DEFAULT_TAG_TEXT_COLOR = Color.parseColor("#1a1a1a");
    private static final int DEFAULT_DELETABLE_TEXT_COLOR = Color.parseColor("#1a1a1a");
    private static final String DEFAULT_DELETABLE_STRING = "×";
    private static final int AUTOCOMPLETE_MIN_COUNT = 3;

    /** tag list */
    private List<String> mTags = new ArrayList<String>();

    /**
     * System Service
     */
    private LayoutInflater mInflater;
    private Display mDisplay;
    private ViewTreeObserver mViewTreeObserber;

    /**
     * listener
     */
    private OnTagSelectListener mSelectListener;
    private OnTagDeleteListener mDeleteListener;
    private OnAddTagListener mAddTagListener;

    /** view size param */
    private int mWidth;
    private int mHeight;

    /**
     * layout initialize flag
     */
    private boolean mInitialized = false;

    /**
     * custom layout param
     */
    private int mTagLayoutColor;
    private int mTagTextColor;
    private boolean mIsDeletable;

    private boolean isAutoCompleteMode = false;
    private String lastAutocompleteText = null;
    private List<String> autocompleteAllTags = new ArrayList<>();

    /**
     * constructor
     * @param ctx
     * @param attrs
     */
    public TagCloudLinkView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        initialize(ctx, attrs, 0);
    }

    /**
     * constructor
     * @param ctx
     * @param attrs
     * @param defStyle
     */
    public TagCloudLinkView(Context ctx, AttributeSet attrs, int defStyle) {
        super(ctx, attrs, defStyle);
        initialize(ctx, attrs, defStyle);
    }

    /**
     * initalize instance
     * @param ctx
     * @param attrs
     * @param defStyle
     */
    private void initialize(Context ctx, AttributeSet attrs, int defStyle) {
        mInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDisplay  = ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        mViewTreeObserber = getViewTreeObserver();
        mViewTreeObserber.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(!mInitialized) {
                    mInitialized = true;
                    drawTags();
                }
            }
        });

        // get AttributeSet
        TypedArray typeArray = ctx.obtainStyledAttributes(attrs, R.styleable.TagCloudLinkView, defStyle, defStyle);
        mTagLayoutColor = typeArray.getColor(
                R.styleable.TagCloudLinkView_tagLayoutColor,DEFAULT_TAG_LAYOUT_COLOR);
        mTagTextColor = typeArray.getColor(
                R.styleable.TagCloudLinkView_tagTextColor,DEFAULT_TAG_TEXT_COLOR);
        mIsDeletable = typeArray.getBoolean(
                R.styleable.TagCloudLinkView_isDeletable, false);
    }

    /**
     * onSizeChanged
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mHeight = h;
    }

    /**
     * view width
     * @return layout width
     */
    public int width() {
        return mWidth;
    }

    /**
     * view height
     * @return int layout height
     */
    public int height() {
        return mHeight;
    }

    public void setAll(List<String> tags) {
        if (tags == null) {
            tags = new ArrayList<>();
        }
        mTags.clear();

        if (isAutoCompleteMode) {
            autocompleteAllTags.clear();
        }

        (isAutoCompleteMode ? autocompleteAllTags : mTags).addAll(tags);

        if (isAutoCompleteMode) {
            if ( TextUtils.isEmpty(lastAutocompleteText) ) {
                //do nothing
            } else {
                mTags = autocompleFilter(lastAutocompleteText, AUTOCOMPLETE_MIN_COUNT);
            }
        }

        drawTags();
    }

    private List<String> autocompleFilter(String text, int minCount) {
        return AutocompleteHelper.filter(autocompleteAllTags, text, minCount);
    }

    private void setAutocompleteText(String text) {
        lastAutocompleteText = text;
        mTags = autocompleFilter(text, AUTOCOMPLETE_MIN_COUNT);
        drawTags();
    }

    public void enableAutocompleteMode(EditText editText) {
        isAutoCompleteMode = true;
        mIsDeletable = false;
        editText.addTextChangedListener(new CustomOnChangeTextListener(this));
    }

    /**
     * tag draw
     */
    public void drawTags() {

        if(!mInitialized) {
            return;
        }

        // clear all tag
        removeAllViews();

        // layout padding left & layout padding right
        float total = getPaddingLeft() + getPaddingRight();
        // 現在位置のindex
        int index = 1;
        // 相対位置起点
        int pindex = index;

        // List Index
        int listIndex = 0;
        for (String item : mTags) {
            final int position = listIndex;
            final String tag = item;

            // inflate tag layout
            View tagLayout = (View) mInflater.inflate(R.layout.tag, null);
            tagLayout.setId(index);
//            tagLayout.setBackgroundColor(mTagLayoutColor);

            // tag text
            TextView tagView = (TextView) tagLayout.findViewById(R.id.tag_txt);
            tagView.setText(tag);
            tagView.setPadding(INNER_VIEW_PADDING, INNER_VIEW_PADDING, INNER_VIEW_PADDING, INNER_VIEW_PADDING);
            tagView.setTextColor(mTagTextColor);
            tagView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if( mSelectListener != null) {
                        mSelectListener.onTagSelected(tag, position);
                    }
                }
            });

            // calculate　of tag layout width
            float tagWidth = tagView.getPaint().measureText(tag)
                    + INNER_VIEW_PADDING * 2;  // tagView padding (left & right)

            // deletable text
            TextView deletableView = (TextView) tagLayout.findViewById(R.id.delete_txt);
            if(mIsDeletable) {
                deletableView.setVisibility(View.VISIBLE);
                deletableView.setText(DEFAULT_DELETABLE_STRING);
                deletableView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mDeleteListener != null) {
                            String targetTag = tag;
                            TagCloudLinkView.this.remove(position);
                            mDeleteListener.onTagDeleted(targetTag, position);
                        }
                    }
                });
                tagWidth += deletableView.getPaint().measureText(DEFAULT_DELETABLE_STRING)
                        + INNER_VIEW_PADDING * 2; // deletableView Padding (left & right)
            } else {
                deletableView.setVisibility(View.GONE);
            }

            LayoutParams tagParams = new LayoutParams(HEIGHT_WC, HEIGHT_WC);
            tagParams.setMargins(0, 0, 0, 0);

            if (mWidth <= total + tagWidth + LAYOUT_WIDTH_OFFSET) {
                tagParams.addRule(RelativeLayout.BELOW, pindex);
                tagParams.topMargin = TAG_LAYOUT_TOP_MERGIN;
                // initialize total param (layout padding left & layout padding right)
                total = getPaddingLeft() + getPaddingRight();
                pindex = index;
            } else {
                tagParams.addRule(RelativeLayout.ALIGN_TOP, pindex);
                tagParams.addRule(RelativeLayout.RIGHT_OF, index - 1);
                if (index > 1) {
                    tagParams.leftMargin = TAG_LAYOUT_LEFT_MERGIN;
                    total += TAG_LAYOUT_LEFT_MERGIN;
                }
            }
            total += tagWidth;
            addView(tagLayout, tagParams);
            index++;
            listIndex++;
        }

        LayoutParams tagParams = new LayoutParams(HEIGHT_WC, HEIGHT_WC);
        tagParams.setMargins(0, 0, 0, 0);

        if (isAutoCompleteMode || !mIsDeletable) {
            return;
        }

        int tagWidth = 50; //25dp

        if (mWidth <= total + tagWidth + LAYOUT_WIDTH_OFFSET) {
            tagParams.addRule(RelativeLayout.BELOW, pindex);
            tagParams.topMargin = TAG_LAYOUT_TOP_MERGIN;
            // initialize total param (layout padding left & layout padding right)
            total = getPaddingLeft() + getPaddingRight();
            pindex = index;
        } else {
            tagParams.addRule(RelativeLayout.ALIGN_TOP, pindex);
            tagParams.addRule(RelativeLayout.RIGHT_OF, index - 1);
            if (index > 1) {
                tagParams.leftMargin = TAG_LAYOUT_LEFT_MERGIN;
                total += TAG_LAYOUT_LEFT_MERGIN;
            }
        }
        total += tagWidth;
        View tagLayout = (View) mInflater.inflate(R.layout.tag, null);
        addView(tagLayout, tagParams);
        tagLayout.findViewById(R.id.add_image_view).setVisibility(VISIBLE);
        tagLayout.findViewById(R.id.tag_txt).setVisibility(GONE);
        tagLayout.setClickable(true);
        tagLayout.setFocusable(true);
        tagLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAddTagListener != null) {
                    mAddTagListener.onAddTag();
                }
            }
        });
        int transparentColor = ContextCompat.getColor(getContext(), android.R.color.transparent);
        tagLayout.setBackgroundColor(transparentColor);

    }

    /**
     * remove tag
     *
     * @param position
     */
    public void remove(int position) {
        mTags.remove(position);
        drawTags();
    }

    /**
     * setter for OnTagSelectListener
     * @param selectListener
     */
    public void setOnTagSelectListener(OnTagSelectListener selectListener) {
        mSelectListener = selectListener;
    }

    /**
     * setter for OnTagDeleteListener
     * @param deleteListener
     */
    public void setOnTagDeleteListener(OnTagDeleteListener deleteListener) {
        mDeleteListener = deleteListener;
    }

    public void setOnAddTagListener(OnAddTagListener addTagListener) {
        mAddTagListener = addTagListener;
    }

    /**
     * listener for tag select
     */
    public interface OnTagSelectListener {
        void onTagSelected(String tag, int position);
    }

    /**
     * listener for tag delete
     */
    public interface OnTagDeleteListener {
        void onTagDeleted(String tag, int position);
    }

    public interface OnAddTagListener {
        void onAddTag();
    }

    private static class CustomOnChangeTextListener extends OnTextChangedListener {

        private WeakReference<TagCloudLinkView> reference;

        public CustomOnChangeTextListener(TagCloudLinkView tagCloudLinkView) {
            reference = new WeakReference<TagCloudLinkView>(tagCloudLinkView);
        }

        @Override
        public void onTextChanged(String oldText, String newText) {
            TagCloudLinkView tagCloudLinkView = reference.get();
            if ( tagCloudLinkView != null ) {
                tagCloudLinkView.setAutocompleteText(newText);
            }
        }

    }

}
