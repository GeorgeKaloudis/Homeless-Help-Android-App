package com.example.homelessapp;

import android.content.Context;
import android.util.AttributeSet;

public class AnchoredImageView extends androidx.appcompat.widget.AppCompatImageView {
    public AnchoredImageView(Context context) {
        super(context);
    }

    public AnchoredImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnchoredImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        setTranslationY(-h/2);
    }
}
