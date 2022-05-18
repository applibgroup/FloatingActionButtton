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

import com.github.clans.fab.util.AnimationUtil;
import com.github.clans.fab.util.ResUtil;
import com.github.clans.fab.util.Util;
import ohos.agp.animation.AnimatorGroup;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.ComponentState;
import ohos.agp.components.Image;
import ohos.agp.components.Text;
import ohos.agp.components.element.Element;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.components.element.StateElement;
import ohos.agp.render.Arc;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.utils.Color;
import ohos.agp.utils.RectFloat;
import ohos.app.Context;
import ohos.miscservices.timeutility.Time;
import ohos.multimodalinput.event.TouchEvent;

public class FloatingActionButton extends Image implements Component.TouchEventListener,
        Component.DrawTask, Component.LayoutRefreshedListener {

    public static final int SIZE_NORMAL = 0;
    public static final int SIZE_MINI = 1;

    int mFabSize;
    boolean mShowShadow;
    int mShadowColor;
    int mShadowRadius = Util.vpToPx(getContext(), 4f);
    int mShadowXOffset = Util.vpToPx(getContext(), 1f);
    int mShadowYOffset = Util.vpToPx(getContext(), 3f);

    private static final long PAUSE_GROWING_TIME = 200;
    private static final double BAR_SPIN_CYCLE_TIME = 500;
    private static final int BAR_MAX_LENGTH = 270;

    private int mColorNormal;
    private int mColorPressed;
    private int mColorDisabled;
    private int mColorRipple;
    private Element mIcon;
    private AnimatorGroup mShowAnimation;
    private AnimatorGroup mHideAnimation;
    private String mLabelText;
    private Component.ClickedListener mClickListener;
    private Element mBackgroundDrawable;
    private boolean mUsingElevation;
    private boolean mUsingElevationCompat;
    private AttrSet mAttrSet;

    // Progress
    private boolean mProgressBarEnabled;
    private int mProgressWidth = Util.vpToPx(getContext(), 6f);
    private int mProgressColor;
    private int mProgressBackgroundColor;
    private boolean mShouldUpdateButtonPosition;
    private float mOriginalX = -1;
    private float mOriginalY = -1;
    private boolean mButtonPositionSaved;
    private Context mContext;
    private RectFloat mProgressCircleBounds = new RectFloat();
    private Paint mBackgroundPaint = new Paint() {
        {
            setAntiAlias(true);
        }
    };
    private Paint mProgressPaint = new Paint() {
        {
            setAntiAlias(true);
        }
    };
    private boolean mProgressIndeterminate;
    private long mLastTimeAnimated;
    private float mSpinSpeed = 195.0f; //The amount of degrees per second
    private long mPausedTimeWithoutGrowing = 0;
    private double mTimeStartGrowing;
    private boolean mBarGrowingFromFront = true;
    private int mBarLength = 16;
    private float mBarExtraLength;
    private float mCurrentProgress;
    private float mTargetProgress;
    private int mProgress;
    private boolean mAnimateProgress;
    private boolean mShouldProgressIndeterminate;
    private boolean mShouldSetProgress;
    private int mProgressMax = 100;
    private boolean mShowProgressBackground;

    //custom attributes
    private static final String FAB_COLOR_NORMAL = "fab_colorNormal";
    private static final String FAB_COLOR_PRESSED = "fab_colorPressed";
    private static final String FAB_COLOR_DISABLED = "fab_colorDisabled";
    //private static final String FAB_COLOR_RIPPLE = "fab_colorRipple";
    private static final String FAB_SHOW_SHADOW = "fab_showShadow";
    private static final String FAB_SHADOW_COLOR = "fab_shadowColor";
    private static final String FAB_SHADOW_RADIUS = "fab_shadowRadius";
    private static final String FAB_SHADOW_XOFFSET = "fab_shadowXOffset";
    private static final String FAB_SHADOW_YOFFSET = "fab_shadowYOffset";
    private static final String FAB_SIZE = "fab_size";
    private static final String FAB_LABEL = "fab_label";
    private static final String FAB_SHOW_ANIMATION = "fab_showAnimation";
    private static final String FAB_HIDE_ANIMATION = "fab_hideAnimation";
    private static final String FAB_PROGRESS_INDETERMINATE = "fab_progress_indeterminate";
    private static final String FAB_PROGRESS_COLOR = "fab_progress_color";
    private static final String FAB_PROGRESS_BACKGROUND_COLOR = "fab_progress_backgroundColor";
    private static final String FAB_PROGRESS_MAX = "fab_progress_max";
    private static final String FAB_PROGRESS_SHOW_BACKGROUND = "fab_progress_showBackground";
    private static final String FAB_PROGRESS = "fab_progress";
    private static final String FAB_ELEVATION = "fab_elevation";

    public FloatingActionButton(Context context) {
        this(context, null);
    }

    public FloatingActionButton(Context context, AttrSet attrSet) {
        this(context, attrSet, null);
    }

    public FloatingActionButton(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        init(context, attrSet, styleName);
    }

    private void init(Context context, AttrSet attrSet, String defStyleAttr) {
        mContext = context;
        if (attrSet != null) {
            mAttrSet = attrSet;
            mColorNormal = attrSet.getAttr(FAB_COLOR_NORMAL).isPresent() ?
                    attrSet.getAttr(FAB_COLOR_NORMAL).get().getColorValue().getValue() : 0xFFDA4336;
            mColorPressed = attrSet.getAttr(FAB_COLOR_PRESSED).isPresent() ?
                    attrSet.getAttr(FAB_COLOR_PRESSED).get().getColorValue().getValue() : 0xFFE75043;
            mColorDisabled = attrSet.getAttr(FAB_COLOR_PRESSED).isPresent() ?
                    attrSet.getAttr(FAB_COLOR_PRESSED).get().getColorValue().getValue() : 0xFFAAAAAA;
            mColorRipple = attrSet.getAttr(FAB_COLOR_PRESSED).isPresent() ?
                    attrSet.getAttr(FAB_COLOR_PRESSED).get().getColorValue().getValue() : 0x99FFFFFF;
            mShowShadow = attrSet.getAttr(FAB_SHOW_SHADOW).isPresent() ?
                    attrSet.getAttr(FAB_SHOW_SHADOW).get().getBoolValue() : false;
            mShadowColor = attrSet.getAttr(FAB_COLOR_PRESSED).isPresent() ?
                    attrSet.getAttr(FAB_COLOR_PRESSED).get().getColorValue().getValue() : 0x66000000;
            mShadowRadius = attrSet.getAttr(FAB_SHADOW_RADIUS).isPresent() ?
                    attrSet.getAttr(FAB_SHADOW_RADIUS).get().getDimensionValue() : mShadowRadius;
            mShadowXOffset = attrSet.getAttr(FAB_SHADOW_XOFFSET).isPresent() ?
                    attrSet.getAttr(FAB_SHADOW_XOFFSET).get().getDimensionValue() : mShadowXOffset;
            mShadowYOffset = attrSet.getAttr(FAB_SHADOW_YOFFSET).isPresent() ?
                    attrSet.getAttr(FAB_SHADOW_YOFFSET).get().getDimensionValue() : mShadowYOffset;
            mFabSize = attrSet.getAttr(FAB_SIZE).isPresent() ?
                    attrSet.getAttr(FAB_SIZE).get().getIntegerValue() : SIZE_NORMAL;
            mLabelText = attrSet.getAttr(FAB_LABEL).isPresent() ?
                    attrSet.getAttr(FAB_LABEL).get().getStringValue() : "";
            mShouldProgressIndeterminate = attrSet.getAttr(FAB_PROGRESS_INDETERMINATE).isPresent() ?
                    attrSet.getAttr(FAB_PROGRESS_INDETERMINATE).get().getBoolValue() : false;
            mProgressColor = attrSet.getAttr(FAB_PROGRESS_COLOR).isPresent() ?
                    attrSet.getAttr(FAB_PROGRESS_COLOR).get().getColorValue().getValue() : 0xFF009688;
            mProgressBackgroundColor = attrSet.getAttr(FAB_PROGRESS_BACKGROUND_COLOR).isPresent() ?
                    attrSet.getAttr(FAB_PROGRESS_BACKGROUND_COLOR).get().getColorValue().getValue() : 0x4D000000;
            mProgressMax = attrSet.getAttr(FAB_PROGRESS_MAX).isPresent() ?
                    attrSet.getAttr(FAB_PROGRESS_MAX).get().getIntegerValue() : mProgressMax;
            mShowProgressBackground = attrSet.getAttr(FAB_PROGRESS_SHOW_BACKGROUND).isPresent() ?
                    attrSet.getAttr(FAB_PROGRESS_SHOW_BACKGROUND).get().getBoolValue() : true;
            if (attrSet.getAttr(FAB_PROGRESS).isPresent()) {
                mProgress = attrSet.getAttr(FAB_PROGRESS).get().getIntegerValue();
                mShouldSetProgress = true;
            } else {
                mProgress = 0;
            }

            float elevation;
            if (attrSet.getAttr(FAB_ELEVATION).isPresent()) {
                elevation = attrSet.getAttr(FAB_ELEVATION).get().getDimensionValue();
                setElevationCompat(elevation);
            }
        } else {
            mColorNormal = 0xFFDA4336;
            mColorPressed = 0xFFE75043;
            mColorDisabled = 0xFFAAAAAA;
            mColorRipple = 0x99FFFFFF;
            mShowShadow = false;
            mShadowColor = 0x66000000;
            mFabSize = SIZE_NORMAL;
            mLabelText = "";
            mShouldProgressIndeterminate = false;
            mProgressColor = 0xFF009688;
            mProgressBackgroundColor = 0x4D000000;
            mShowProgressBackground = true;
            mProgress = 0;
        }

        onMeasure();
        initShowAnimation();
        initHideAnimation();

        setClickable(true);
        addDrawTask(this);
        setLayoutRefreshedListener(this);
        setTouchEventListener(this);
    }

    private void initShowAnimation() {
        if (mAttrSet != null && mAttrSet.getAttr(FAB_SHOW_ANIMATION).isPresent()) {
            String showAnimationType = mAttrSet.getAttr(FAB_SHOW_ANIMATION).get().getStringValue();
            mShowAnimation = AnimationUtil.loadAnimation(showAnimationType, this, 500);
        } else {
            mShowAnimation = AnimationUtil.loadAnimation("fab_default_show", this, 500);
        }
    }

    private void initHideAnimation() {
        if (mAttrSet != null && mAttrSet.getAttr(FAB_HIDE_ANIMATION).isPresent()) {
            String hideAnimationType = mAttrSet.getAttr(FAB_HIDE_ANIMATION).get().getStringValue();
            mHideAnimation = AnimationUtil.loadAnimation(hideAnimationType, this, 500);
        } else {
            mHideAnimation = AnimationUtil.loadAnimation("fab_default_hide", this, 500);
        }
    }

    private int getCircleSize() {
        int size = (int) ResUtil.getDimen(mContext,
                mFabSize == SIZE_NORMAL ? ResourceTable.Float_fab_size_normal :
                        ResourceTable.Float_fab_size_mini);
        return size;
    }

    public int calculateMeasuredWidth() {
        int width = getCircleSize() + calculateShadowWidth();
        if (mProgressBarEnabled) {
            width += mProgressWidth * 2;
        }
        return width;
    }

    public int calculateMeasuredHeight() {
        int height = getCircleSize() + calculateShadowHeight();
        if (mProgressBarEnabled) {
            height += mProgressWidth * 2;
        }
        return height;
    }

    int calculateShadowWidth() {
        int shadowWidth = hasShadow() ? getShadowX() * 2 : 0;
        return shadowWidth;
    }

    int calculateShadowHeight() {
        int shadowHeight = hasShadow() ? getShadowY() * 2 : 0;
        return shadowHeight;
    }

    private int getShadowX() {
        return mShadowRadius + Math.abs(mShadowXOffset);
    }

    private int getShadowY() {
        return mShadowRadius + Math.abs(mShadowYOffset);
    }

    private float calculateCenterX() {
        return (float) (getWidth() / 2);
    }

    private float calculateCenterY() {
        return (float) (getHeight() / 2);
    }

    protected void onMeasure() {
        ComponentContainer.LayoutConfig layoutConfig = getLayoutConfig();
        layoutConfig.width = calculateMeasuredWidth();
        layoutConfig.height = calculateMeasuredHeight();
        setLayoutConfig(layoutConfig);
    }


    @Override
    public void onDraw(Component component, Canvas canvas) {
        onMeasure();
        onSizeChanged();
        if (mProgressBarEnabled) {
            if (mShowProgressBackground) {
                canvas.drawArc(mProgressCircleBounds,
                        new Arc(360, 360, false), mBackgroundPaint);
            }

            shouldInvalidate = false;

            if (mProgressIndeterminate) {
                shouldInvalidate = true;

                long deltaTime = Time.getRealActiveTime() - mLastTimeAnimated;
                float deltaNormalized = deltaTime * mSpinSpeed / 1000.0f;

                updateProgressLength(deltaTime);

                mCurrentProgress += deltaNormalized;
                if (mCurrentProgress > 360f) {
                    mCurrentProgress -= 360f;
                }

                mLastTimeAnimated = Time.getRealActiveTime();
                float from = mCurrentProgress - 90;
                float to = mBarLength + mBarExtraLength;

                canvas.drawArc(mProgressCircleBounds, new Arc(from, to, false), mProgressPaint);
            } else {
                if (mCurrentProgress != mTargetProgress) {
                    shouldInvalidate = true;
                    float deltaTime = (float) (Time.getRealActiveTime() - mLastTimeAnimated) / 1000;
                    float deltaNormalized = deltaTime * mSpinSpeed;

                    if (mCurrentProgress > mTargetProgress) {
                        mCurrentProgress = Math.max(mCurrentProgress - deltaNormalized, mTargetProgress);
                    } else {
                        mCurrentProgress = Math.min(mCurrentProgress + deltaNormalized, mTargetProgress);
                    }
                    mLastTimeAnimated = Time.getRealActiveTime();
                }

                canvas.drawArc(mProgressCircleBounds, new Arc(-90, mCurrentProgress, false), mProgressPaint);
            }

        }
    }

    private boolean shouldInvalidate;

    @Override
    public void onRefreshed(Component component) {
        if (shouldInvalidate) {
            invalidate();
        }
    }

    private void updateProgressLength(long deltaTimeInMillis) {
        if (mPausedTimeWithoutGrowing >= PAUSE_GROWING_TIME) {
            mTimeStartGrowing += deltaTimeInMillis;

            if (mTimeStartGrowing > BAR_SPIN_CYCLE_TIME) {
                mTimeStartGrowing -= BAR_SPIN_CYCLE_TIME;
                mPausedTimeWithoutGrowing = 0;
                mBarGrowingFromFront = !mBarGrowingFromFront;
            }

            float distance = (float) Math.cos((mTimeStartGrowing / BAR_SPIN_CYCLE_TIME + 1) * Math.PI) / 2 + 0.5f;
            float length = BAR_MAX_LENGTH - mBarLength;

            if (mBarGrowingFromFront) {
                mBarExtraLength = distance * length;
            } else {
                float newLength = length * (1 - distance);
                mCurrentProgress += (mBarExtraLength - newLength);
                mBarExtraLength = newLength;
            }
        } else {
            mPausedTimeWithoutGrowing += deltaTimeInMillis;
        }
    }

    protected void onSizeChanged() {
        saveButtonOriginalPosition();

        if (mShouldProgressIndeterminate) {
            setIndeterminate(true);
            mShouldProgressIndeterminate = false;
        } else if (mShouldSetProgress) {
            setProgress(mProgress, mAnimateProgress);
            mShouldSetProgress = false;
        } else if (mShouldUpdateButtonPosition) {
            mShouldUpdateButtonPosition = false;
        }

        setupProgressBounds();
        setupProgressBarPaints();
        updateBackground();
    }

    @Override
    public void setLayoutConfig(ComponentContainer.LayoutConfig config) {
        if (mUsingElevationCompat) {
            config.setMarginLeft(config.getMarginLeft() + getShadowX());
            config.setMarginTop(config.getMarginTop() + getShadowY());
            config.setMarginRight(config.getMarginRight() + getShadowX());
            config.setMarginBottom(config.getMarginBottom() + getShadowY());
        }
        super.setLayoutConfig(config);
    }

    void updateBackground() {
        setBackgroundCompat(createFillDrawable());
    }

    public Element getIconDrawable() {
        if (mIcon != null) {
            return mIcon;
        } else {
            ShapeElement element = new ShapeElement();
            element.setRgbColor(new RgbColor(Color.TRANSPARENT.getValue()));
            return element;
        }
    }

    private Element createFillDrawable() {
        StateElement drawable = new StateElement();
        drawable.addState(new int[]{ComponentState.COMPONENT_STATE_DISABLED}, createCircleDrawable(mColorDisabled));
        drawable.addState(new int[]{ComponentState.COMPONENT_STATE_PRESSED}, createCircleDrawable(mColorPressed));
        drawable.addState(new int[]{}, createCircleDrawable(mColorNormal));

        mBackgroundDrawable = drawable;
        return drawable;
    }

    public Element createCircleDrawable(int color) {
        CircleDrawable shapeDrawable = new CircleDrawable(ShapeElement.OVAL);
        shapeDrawable.setRgbColor(RgbColor.fromArgbInt(color));
        return shapeDrawable;
    }

    private void setBackgroundCompat(Element drawable) {
        setBackground(drawable);
    }

    private void saveButtonOriginalPosition() {
        if (!mButtonPositionSaved) {
            if (mOriginalX == -1) {
                mOriginalX = getContentPositionX();
            }

            if (mOriginalY == -1) {
                mOriginalY = getContentPositionY();
            }

            mButtonPositionSaved = true;
        }
    }

    private void setupProgressBarPaints() {
        mBackgroundPaint.setColor(new Color(mProgressBackgroundColor));
        mBackgroundPaint.setStyle(Paint.Style.STROKE_STYLE);
        mBackgroundPaint.setStrokeWidth(mProgressWidth);

        mProgressPaint.setColor(new Color(mProgressColor));
        mProgressPaint.setStyle(Paint.Style.STROKE_STYLE);
        mProgressPaint.setStrokeWidth(mProgressWidth);
    }

    private void setupProgressBounds() {
        int circleInsetHorizontal = hasShadow() ? getShadowX() : 0;
        int circleInsetVertical = hasShadow() ? getShadowY() : 0;
        mProgressCircleBounds = new RectFloat(
                circleInsetHorizontal + mProgressWidth / 2,
                circleInsetVertical + mProgressWidth / 2,
                calculateMeasuredWidth() - circleInsetHorizontal - mProgressWidth / 2,
                calculateMeasuredHeight() - circleInsetVertical - mProgressWidth / 2
        );
    }

    void playShowAnimation() {
        mHideAnimation.cancel();
        initShowAnimation();
        mShowAnimation.start();
    }

    void playHideAnimation() {
        mShowAnimation.cancel();
        initHideAnimation();
        mHideAnimation.start();
    }

    ClickedListener getOnClickListener() {
        return mClickListener;
    }

    Label getLabelView() {
        return (Label) getTag();
    }

    void setColors(int colorNormal, int colorPressed, int colorRipple) {
        mColorNormal = colorNormal;
        mColorPressed = colorPressed;
        mColorRipple = colorRipple;
    }

    public void onActionDown() {
        if (mBackgroundDrawable instanceof StateElement) {
            StateElement drawable = (StateElement) mBackgroundDrawable;
            drawable.selectElement(0);
        }
    }

    public void onActionUp() {
        if (mBackgroundDrawable instanceof StateElement) {
            StateElement drawable = (StateElement) mBackgroundDrawable;
            drawable.selectElement(1);
        }
    }

    @Override
    public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
        if (mClickListener != null && isEnabled()) {
            Label label = (Label) getTag();
            if (label == null) {
                return true;
            }

            int action = touchEvent.getAction();
            switch (action) {
                case TouchEvent.PRIMARY_POINT_UP:
                case TouchEvent.CANCEL:
                    label.onActionUp();
                    onActionUp();
                    break;
                default:
                    break;
            }
        }
        return true;
    }

    private class CircleDrawable extends ShapeElement {

        private int circleInsetHorizontal;
        private int circleInsetVertical;

        private CircleDrawable() {
        }

        private CircleDrawable(int shape) {
            super();
            setShape(shape);
            circleInsetHorizontal = hasShadow() ? mShadowRadius + Math.abs(mShadowXOffset) : 0;
            circleInsetVertical = hasShadow() ? mShadowRadius + Math.abs(mShadowYOffset) : 0;

            if (mProgressBarEnabled) {
                circleInsetHorizontal += mProgressWidth;
                circleInsetVertical += mProgressWidth;
            }
        }

        @Override
        public void drawToCanvas(Canvas canvas) {
            setBounds(circleInsetHorizontal, circleInsetVertical, calculateMeasuredWidth()
                    - circleInsetHorizontal, calculateMeasuredHeight() - circleInsetVertical);
            super.drawToCanvas(canvas);
        }
    }

    /* ===== API methods ===== */

    @Override
    public void setImageElement(Element drawable) {
        if (mIcon != drawable) {
            mIcon = drawable;
            super.setImageElement(mIcon);
            updateBackground();
        }
    }

    @Override
    public void setPixelMap(int resId) {
        Element drawable = ResUtil.getPixelMapDrawable(getContext(), resId);
        if (mIcon != drawable) {
            mIcon = drawable;
            super.setImageElement(mIcon);
            updateBackground();
        }
    }

    @Override
    public void setClickedListener(ClickedListener listener) {
        super.setClickedListener(listener);
        mClickListener = listener;
        Component label = (Component) getTag();
        if (label != null) {
            label.setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component v) {
                    if (mClickListener != null) {
                        mClickListener.onClick(FloatingActionButton.this);
                    }
                }
            });
        }
    }

    /**
     * Sets the size of the <b>FloatingActionButton</b> and invalidates its layout.
     *
     * @param size size of the <b>FloatingActionButton</b>. Accepted values: SIZE_NORMAL, SIZE_MINI.
     */
    public void setButtonSize(int size) {
        if (size != SIZE_NORMAL && size != SIZE_MINI) {
            throw new IllegalArgumentException("Use @FabSize constants only!");
        }

        if (mFabSize != size) {
            mFabSize = size;
            updateBackground();
        }
    }

    public int getButtonSize() {
        return mFabSize;
    }

    public void setColorNormal(int color) {
        if (mColorNormal != color) {
            mColorNormal = color;
            updateBackground();
        }
    }

    public void setColorNormalResId(int colorResId) {
        setColorNormal(ResUtil.getColor(getContext(), colorResId));
    }

    public int getColorNormal() {
        return mColorNormal;
    }

    public void setColorPressed(int color) {
        if (color != mColorPressed) {
            mColorPressed = color;
            updateBackground();
        }
    }

    public void setColorPressedResId(int colorResId) {
        setColorPressed(ResUtil.getColor(getContext(), colorResId));
    }

    public int getColorPressed() {
        return mColorPressed;
    }

    public void setColorRipple(int color) {
        if (color != mColorRipple) {
            mColorRipple = color;
            updateBackground();
        }
    }

    public void setColorRippleResId(int colorResId) {
        setColorRipple(ResUtil.getColor(getContext(), colorResId));
    }

    public int getColorRipple() {
        return mColorRipple;
    }

    public void setColorDisabled(int color) {
        if (color != mColorDisabled) {
            mColorDisabled = color;
            updateBackground();
        }
    }

    public void setColorDisabledResId(int colorResId) {
        setColorDisabled(ResUtil.getColor(getContext(), colorResId));
    }

    public int getColorDisabled() {
        return mColorDisabled;
    }

    public void setShowShadow(boolean show) {
        if (mShowShadow != show) {
            mShowShadow = show;
            updateBackground();
        }
    }

    public boolean hasShadow() {
        return !mUsingElevation && mShowShadow;
    }

    /**
     * Sets the shadow radius of the <b>FloatingActionButton</b> and invalidates its layout.
     *
     * @param dimenResId the resource identifier of the dimension
     */
    public void setShadowRadius(int dimenResId) {
        int shadowRadius = ResUtil.getIntDimen(getContext(), dimenResId);
        if (mShadowRadius != shadowRadius) {
            mShadowRadius = shadowRadius;
            postLayout();
            updateBackground();
        }
    }

    /**
     * Sets the shadow radius of the <b>FloatingActionButton</b> and invalidates its layout.
     * <p>
     * Must be specified in density-independent (dp) pixels, which are then converted into actual
     * pixels (px).
     *
     * @param shadowRadiusDp shadow radius specified in density-independent (dp) pixels
     */
    public void setShadowRadius(float shadowRadiusDp) {
        mShadowRadius = Util.vpToPx(getContext(), shadowRadiusDp);
        postLayout();
        updateBackground();
    }

    public int getShadowRadius() {
        return mShadowRadius;
    }

    /**
     * Sets the shadow x offset of the <b>FloatingActionButton</b> and invalidates its layout.
     *
     * @param dimenResId the resource identifier of the dimension
     */
    public void setShadowXOffset(int dimenResId) {
        int shadowXOffset = ResUtil.getIntDimen(getContext(), dimenResId);
        if (mShadowXOffset != shadowXOffset) {
            mShadowXOffset = shadowXOffset;
            postLayout();
            updateBackground();
        }
    }

    /**
     * Sets the shadow x offset of the <b>FloatingActionButton</b> and invalidates its layout.
     * <p>
     * Must be specified in density-independent (dp) pixels, which are then converted into actual
     * pixels (px).
     *
     * @param shadowXOffsetDp shadow radius specified in density-independent (dp) pixels
     */
    public void setShadowXOffset(float shadowXOffsetDp) {
        mShadowXOffset = Util.vpToPx(getContext(), shadowXOffsetDp);
        postLayout();
        updateBackground();
    }

    public int getShadowXOffset() {
        return mShadowXOffset;
    }

    /**
     * Sets the shadow y offset of the <b>FloatingActionButton</b> and invalidates its layout.
     *
     * @param dimenResId the resource identifier of the dimension
     */
    public void setShadowYOffset(int dimenResId) {
        int shadowYOffset = ResUtil.getIntDimen(getContext(), dimenResId);
        if (mShadowYOffset != shadowYOffset) {
            mShadowYOffset = shadowYOffset;
            postLayout();
            updateBackground();
        }
    }

    /**
     * Sets the shadow y offset of the <b>FloatingActionButton</b> and invalidates its layout.
     * <p>
     * Must be specified in density-independent (dp) pixels, which are then converted into actual
     * pixels (px).
     *
     * @param shadowYOffsetDp shadow radius specified in density-independent (dp) pixels
     */
    public void setShadowYOffset(float shadowYOffsetDp) {
        mShadowYOffset = Util.vpToPx(getContext(), shadowYOffsetDp);
        postLayout();
        updateBackground();
    }

    public int getShadowYOffset() {
        return mShadowYOffset;
    }

    public void setShadowColorResource(int colorResId) {
        int shadowColor = ResUtil.getColor(getContext(), colorResId);
        if (mShadowColor != shadowColor) {
            mShadowColor = shadowColor;
            updateBackground();
        }
    }

    public void setShadowColor(int color) {
        if (mShadowColor != color) {
            mShadowColor = color;
            updateBackground();
        }
    }

    public int getShadowColor() {
        return mShadowColor;
    }

    /**
     * Checks whether <b>FloatingActionButton</b> is hidden
     *
     * @return true if <b>FloatingActionButton</b> is hidden, false otherwise
     */
    public boolean isHidden() {
        return getVisibility() == INVISIBLE;
    }

    /**
     * Makes the <b>FloatingActionButton</b> to appear and sets its visibility to {@link #VISIBLE}
     *
     * @param animate if true - plays "show animation"
     */
    public void show(boolean animate) {
        if (isHidden()) {
            if (animate) {
                playShowAnimation();
            }
            super.setVisibility(VISIBLE);
        }
    }

    /**
     * Makes the <b>FloatingActionButton</b> to disappear and sets its visibility to {@link #INVISIBLE}
     *
     * @param animate if true - plays "hide animation"
     */
    public void hide(boolean animate) {
        if (!isHidden()) {
            if (animate) {
                playHideAnimation();
            }
            super.setVisibility(INVISIBLE);
        }
    }

    public void toggle(boolean animate) {
        if (isHidden()) {
            show(animate);
        } else {
            hide(animate);
        }
    }

    public void setLabelText(String text) {
        mLabelText = text;
        Text labelView = getLabelView();
        if (labelView != null) {
            labelView.setText(text);
        }
    }

    public String getLabelText() {
        return mLabelText;
    }

    public void setShowAnimation(AnimatorGroup showAnimation) {
        mShowAnimation = showAnimation;
    }

    public void setHideAnimation(AnimatorGroup hideAnimation) {
        mHideAnimation = hideAnimation;
    }

    public void setLabelVisibility(int visibility) {
        Label labelView = getLabelView();
        if (labelView != null) {
            labelView.setVisibility(visibility);
            labelView.setHandleVisibilityChanges(visibility == VISIBLE);
        }
    }

    public int getLabelVisibility() {
        Text labelView = getLabelView();
        if (labelView != null) {
            return labelView.getVisibility();
        }

        return -1;
    }

    public void setElevationCompat(float elevation) {
        mShadowColor = 0x26000000;
        mShadowRadius = Math.round(elevation / 2);
        mShadowXOffset = 0;
        mShadowYOffset = Math.round(mFabSize == SIZE_NORMAL ? elevation : elevation / 2);
        mUsingElevationCompat = true;
        mShowShadow = false;
        updateBackground();
        ComponentContainer.LayoutConfig layoutParams = getLayoutConfig();
        if (layoutParams != null) {
            setLayoutConfig(layoutParams);
        }

    }

    /**
     * <p>Change the indeterminate mode for the progress bar. In indeterminate
     * mode, the progress is ignored and the progress bar shows an infinite
     * animation instead.</p>
     *
     * @param indeterminate true to enable the indeterminate mode
     */
    public synchronized void setIndeterminate(boolean indeterminate) {
        if (!indeterminate) {
            mCurrentProgress = 0.0f;
        }

        mProgressBarEnabled = indeterminate;
        mShouldUpdateButtonPosition = true;
        mProgressIndeterminate = indeterminate;
        mLastTimeAnimated = Time.getRealActiveTime();
        setupProgressBounds();
        updateBackground();
        invalidate();
    }

    public synchronized void setMax(int max) {
        mProgressMax = max;
    }

    public synchronized int getMax() {
        return mProgressMax;
    }

    public synchronized void setProgress(int progress, boolean animate) {
        if (mProgressIndeterminate) return;

        mProgress = progress;
        mAnimateProgress = animate;

        if (!mButtonPositionSaved) {
            mShouldSetProgress = true;
            return;
        }

        mProgressBarEnabled = true;
        mShouldUpdateButtonPosition = true;
        setupProgressBounds();
        saveButtonOriginalPosition();
        updateBackground();

        if (progress < 0) {
            progress = 0;
        } else if (progress > mProgressMax) {
            progress = mProgressMax;
        }

        if (progress == mTargetProgress) {
            return;
        }

        mTargetProgress = mProgressMax > 0 ? (progress / (float) mProgressMax) * 360 : 0;
        mLastTimeAnimated = Time.getRealActiveTime();

        if (!animate) {
            mCurrentProgress = mTargetProgress;
        }

        invalidate();
    }

    public synchronized int getProgress() {
        return mProgressIndeterminate ? 0 : mProgress;
    }

    public synchronized void hideProgress() {
        mProgressBarEnabled = false;
        mShouldUpdateButtonPosition = true;
        updateBackground();
    }

    public synchronized void setShowProgressBackground(boolean show) {
        mShowProgressBackground = show;
    }

    public synchronized boolean isProgressBackgroundShown() {
        return mShowProgressBackground;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        Label label = (Label) getTag();
        if (label != null) {
            label.setEnabled(enabled);
        }
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        Label label = (Label) getTag();
        if (label != null) {
            label.setVisibility(visibility);
        }
    }
}
