/*
 * Copyright (c) 2020 Huawei Device Co., Ltd.
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

package com.github.clans.fab;

import com.github.clans.fab.util.Util;
import ohos.agp.animation.AnimatorProperty;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.*;
import ohos.agp.components.element.Element;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.components.element.StateElement;
import ohos.agp.render.BlendMode;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.utils.Color;
import ohos.agp.utils.Rect;
import ohos.agp.utils.RectFloat;
import ohos.app.Context;
import ohos.multimodalinput.event.TouchEvent;

import static ohos.agp.utils.TextAlignment.CENTER;

public class Label extends Text implements Component.TouchEventListener,
        Component.DrawTask {

    private int mShadowRadius;
    private int mShadowXOffset;
    private int mShadowYOffset;
    private int mShadowColor;
    private Element mBackgroundDrawable;
    private boolean mShowShadow = false;
    private int mRawWidth;
    private int mRawHeight;
    private int mColorNormal;
    private int mColorPressed;
    private int mColorRipple;
    private int mCornerRadius;
    private FloatingActionButton mFab;
    private AnimatorProperty mShowAnimation;
    private AnimatorProperty mHideAnimation;
    private boolean mUsingStyle;
    private boolean mHandleVisibilityChanges = true;

    public Label(Context context) {
        this(context, null);
    }

    public Label(Context context, AttrSet attrSet) {
        this(context, attrSet, null);
    }

    public Label(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        init(context);
    }

    private void init(Context context) {
        setTextAlignment(CENTER);
        setTouchEventListener(this);
        addDrawTask(this);
    }

    @Override
    public void onDraw(Component component, Canvas canvas) {
        onMeasure();
    }

    protected void onMeasure() {
        ComponentContainer.LayoutConfig layoutConfig = getLayoutConfig();
        layoutConfig.width = calculateMeasuredWidth();
        layoutConfig.height = calculateMeasuredHeight();
        setLayoutConfig(layoutConfig);
    }

    protected static Paint mDefaultPaint = new Paint() {
        {
            setAntiAlias(true);
            setDither(true);
            setFilterBitmap(true);
            setTextAlign(CENTER);
        }
    };

    public int calculateMeasuredWidth() {
        mDefaultPaint.setTextSize(getTextSize());
        Rect bounds = mDefaultPaint.getTextBounds(getText());

        if (mRawWidth == 0) {
            mRawWidth = (bounds.getWidth() > getMaxTextWidth() ? getMaxTextWidth() : bounds.getWidth()) +
                    Util.vpToPx(getContext(),40);
        }
        return mRawWidth + calculateShadowWidth();
    }

    public int calculateMeasuredHeight() {
        mDefaultPaint.setTextSize(getTextSize());
        Rect bounds = mDefaultPaint.getTextBounds(getText());
        if (mRawHeight == 0) {
            mRawHeight = Util.vpToPx(getContext(),30);
        }
        return mRawHeight + calculateShadowHeight();
    }

    int calculateShadowWidth() {
        return mShowShadow ? (mShadowRadius + Math.abs(mShadowXOffset)) : 0;
    }

    int calculateShadowHeight() {
        return mShowShadow ? (mShadowRadius + Math.abs(mShadowYOffset)) : 0;
    }

    void updateBackground() {
        setBackgroundCompat(createFillDrawable());
    }

    private Element createFillDrawable() {
        StateElement drawable = new StateElement();
        drawable.addState(new int[]{ComponentState.COMPONENT_STATE_PRESSED}, createRectDrawable(mColorPressed));
        drawable.addState(new int[]{}, createRectDrawable(mColorNormal));

        mBackgroundDrawable = drawable;
        return drawable;
    }

    private Element createRectDrawable(int color) {
        RectDrawable shapeDrawable = new RectDrawable(ShapeElement.RECTANGLE);
        shapeDrawable.setRgbColor(RgbColor.fromArgbInt(color));
        shapeDrawable.setCornerRadiiArray(new float[]{
                mCornerRadius,
                mCornerRadius,
                mCornerRadius,
                mCornerRadius,
                mCornerRadius,
                mCornerRadius,
                mCornerRadius,
                mCornerRadius
        });
        return shapeDrawable;
    }

    private class RectDrawable extends ShapeElement {

        private RectDrawable() {
        }

        private RectDrawable(int shape) {
            super();
            setShape(shape);
        }

        @Override
        public void drawToCanvas(Canvas canvas) {
            setBounds(0, 0, calculateMeasuredWidth(), calculateMeasuredHeight());
            super.drawToCanvas(canvas);
        }
    }

    private void setShadow(FloatingActionButton fab) {
        mShadowColor = fab.getShadowColor();
        mShadowRadius = fab.getShadowRadius();
        mShadowXOffset = fab.getShadowXOffset();
        mShadowYOffset = fab.getShadowYOffset();
        mShowShadow = fab.hasShadow();
    }

    private void setBackgroundCompat(Element drawable) {
        setBackground(drawable);
    }

    private void playShowAnimation() {
        if (mShowAnimation != null) {
            mHideAnimation.cancel();
            mShowAnimation.start();
        }
    }

    private void playHideAnimation() {
        if (mHideAnimation != null) {
            mShowAnimation.cancel();
            mHideAnimation.start();
        }
    }

    void onActionDown() {
        if (mUsingStyle) {
            mBackgroundDrawable = getBackgroundElement();
        }

        if (mBackgroundDrawable instanceof StateElement) {
            StateElement drawable = (StateElement) mBackgroundDrawable;
            drawable.selectElement(0);
        }
    }

    void onActionUp() {
        if (mUsingStyle) {
            mBackgroundDrawable = getBackgroundElement();
        }

        if (mBackgroundDrawable instanceof StateElement) {
            StateElement drawable = (StateElement) mBackgroundDrawable;
            drawable.selectElement(1);
        }
    }

    void setFab(FloatingActionButton fab) {
        mFab = fab;
        setShadow(fab);
    }

    void setShowShadow(boolean show) {
        mShowShadow = show;
    }

    void setCornerRadius(int cornerRadius) {
        mCornerRadius = cornerRadius;
    }

    void setColors(int colorNormal, int colorPressed, int colorRipple) {
        mColorNormal = colorNormal;
        mColorPressed = colorPressed;
        mColorRipple = colorRipple;
    }

    void show(boolean animate) {
        if (animate) {
            playShowAnimation();
        }
        setVisibility(VISIBLE);
    }

    void hide(boolean animate) {
        if (animate) {
            playHideAnimation();
        }
        setVisibility(INVISIBLE);
    }

    void setShowAnimation(AnimatorProperty showAnimation) {
        mShowAnimation = showAnimation;
    }

    void setHideAnimation(AnimatorProperty hideAnimation) {
        mHideAnimation = hideAnimation;
    }

    void setUsingStyle(boolean usingStyle) {
        mUsingStyle = usingStyle;
    }

    void setHandleVisibilityChanges(boolean handle) {
        mHandleVisibilityChanges = handle;
    }

    boolean isHandleVisibilityChanges() {
        return mHandleVisibilityChanges;
    }

    @Override
    public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
        if (mFab == null || mFab.getClickedListener() == null || !mFab.isEnabled()) {
            return false;
        }

        int action = touchEvent.getAction();
        switch (action) {
            case TouchEvent.PRIMARY_POINT_UP:
            case TouchEvent.CANCEL:
                onActionUp();
                mFab.onActionUp();
                break;
            default:
                break;
        }
        return false;
    }
}
