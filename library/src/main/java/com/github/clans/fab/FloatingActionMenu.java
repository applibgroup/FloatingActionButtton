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

import com.github.clans.fab.anim.AnimatorTypes;
import com.github.clans.fab.anim.XAnimatorValue;
import com.github.clans.fab.util.AnimationUtil;
import com.github.clans.fab.util.ResUtil;
import com.github.clans.fab.util.Util;
import ohos.agp.animation.Animator;
import ohos.agp.animation.AnimatorGroup;
import ohos.agp.animation.AnimatorValue;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.*;
import ohos.agp.components.element.Element;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.render.Canvas;
import ohos.agp.text.Font;
import ohos.agp.utils.Color;
import ohos.agp.utils.Rect;
import ohos.app.Context;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.multimodalinput.event.TouchEvent;

import java.util.ArrayList;
import java.util.List;

public class FloatingActionMenu extends ComponentContainer implements Component.TouchEventListener,
        Component.DrawTask, Component.BindStateChangedListener, Component.LayoutRefreshedListener {

    private static final int ANIMATION_DURATION = 300;
    private static final float CLOSED_PLUS_ROTATION = 0f;
    private static final float OPENED_PLUS_ROTATION_LEFT = -90f - 45f;
    private static final float OPENED_PLUS_ROTATION_RIGHT = 90f + 45f;

    private static final int OPEN_UP = 0;
    private static final int OPEN_DOWN = 1;

    private static final int LABELS_POSITION_LEFT = 0;
    private static final int LABELS_POSITION_RIGHT = 1;

    private AnimatorGroup mOpenAnimatorSet = new AnimatorGroup();
    private AnimatorGroup mCloseAnimatorSet = new AnimatorGroup();
    private AnimatorGroup mIconToggleSet;

    private int mButtonSpacing = Util.vpToPx(getContext(), 5f);
    private FloatingActionButton mMenuButton;
    private int mMaxButtonWidth;
    private int mLabelsMargin = Util.vpToPx(getContext(), 5f);
    private int mLabelsVerticalOffset = Util.vpToPx(getContext(), 5f);
    private int mButtonsCount;
    private boolean mMenuOpened;
    private boolean mIsMenuOpening;
    private EventHandler mUiHandler = new EventHandler(EventRunner.getMainEventRunner());
    private int mLabelsPaddingTop = Util.vpToPx(getContext(), 4f);
    private int mLabelsPaddingRight = Util.vpToPx(getContext(), 8f);
    private int mLabelsPaddingBottom = Util.vpToPx(getContext(), 4f);
    private int mLabelsPaddingLeft = Util.vpToPx(getContext(), 8f);
    private Color mLabelsTextColor;
    private float mLabelsTextSize;
    private int mLabelsCornerRadius = Util.vpToPx(getContext(), 3f);
    private boolean mLabelsShowShadow;
    private Color mLabelsColorNormal;
    private Color mLabelsColorPressed;
    private Color mLabelsColorRipple;
    private boolean mMenuShowShadow;
    private Color mMenuShadowColor;
    private float mMenuShadowRadius = 4f;
    private float mMenuShadowXOffset = 1f;
    private float mMenuShadowYOffset = 3f;
    private Color mMenuColorNormal;
    private Color mMenuColorPressed;
    private Color mMenuColorRipple;
    private Element mIcon;
    private int mAnimationDelayPerItem;
    private boolean mIsAnimated = true;
    private boolean mLabelsSingleLine;
    private int mLabelsEllipsize;
    private int mLabelsMaxLines;
    private int mMenuFabSize;
    private int mLabelsStyle;
    private Font mCustomTypefaceFromFont;
    private boolean mIconAnimated = true;
    private Image mImageToggle;
    private AnimatorGroup mMenuButtonShowAnimation;
    private AnimatorGroup mMenuButtonHideAnimation;
    private AnimatorGroup mImageToggleShowAnimation;
    private AnimatorGroup mImageToggleHideAnimation;
    private boolean mIsMenuButtonAnimationRunning;
    private boolean mIsSetClosedOnTouchOutside;
    private int mOpenDirection;
    private OnMenuToggleListener mToggleListener;

    private AnimatorValue mShowBackgroundAnimator;
    private AnimatorValue mHideBackgroundAnimator;
    private Color mBackgroundColor;

    private int mLabelsPosition;
    private Context mLabelsContext;
    private String mMenuLabelText;
    private boolean mUsingMenuLabel;

    private static final String MENU_BUTTON_SPACING = "menu_buttonSpacing";
    private static final String MENU_LABELS_MARGIN = "menu_labels_margin";
    private static final String MENU_LABELS_POSITION = "menu_labels_position";
    private static final String MENU_LABELS_PADDING_TOP = "menu_labels_paddingTop";
    private static final String MENU_LABELS_PADDING_RIGHT = "menu_labels_paddingRight";
    private static final String MENU_LABELS_PADDING_BOTTOM = "menu_labels_paddingBottom";
    private static final String MENU_LABELS_PADDING_LEFT = "menu_labels_paddingLeft";
    private static final String MENU_LABELS_TEXT_COLOR = "menu_labels_textColor";
    private static final String MENU_LABELS_TEXT_SIZE = "menu_labels_textSize";
    private static final String MENU_LABELS_CORNER_RADIUS = "menu_labels_cornerRadius";
    private static final String MENU_LABELS_SHOW_SHADOW = "menu_labels_showShadow";
    private static final String MENU_LABELS_COLOR_NORMAL = "menu_labels_colorNormal";
    private static final String MENU_LABELS_COLOR_PRESSED = "menu_labels_colorPressed";
    private static final String MENU_LABELS_COLOR_RIPPLE = "menu_labels_colorRipple";
    private static final String MENU_SHOW_SHADOW = "menu_showShadow";
    private static final String MENU_SHADOW_COLOR = "menu_shadowColor";
    private static final String MENU_SHADOW_RADIUS = "menu_shadowRadius";
    private static final String MENU_SHADOW_XOFFSET = "menu_shadowXOffset";
    private static final String MENU_SHADOW_YOFFSET = "menu_shadowYOffset";
    private static final String MENU_COLOR_NORMAL = "menu_colorNormal";
    private static final String MENU_COLOR_PRESSED = "menu_colorPressed";
    private static final String MENU_COLOR_RIPPLE = "menu_colorRipple";
    private static final String MENU_ANIMATION_DELAY_PER_ITEM = "menu_animationDelayPerItem";
    private static final String MENU_ICON = "menu_icon";
    private static final String MENU_LABELS_SINGLE_LINE = "menu_labels_singleLine";
    private static final String MENU_LABELS_ELLIPSIZE = "menu_labels_ellipsize";
    private static final String MENU_LABELS_MAX_LINES = "menu_labels_maxLines";
    private static final String MENU_FAB_SIZE = "menu_fab_size";
    private static final String MENU_LABELS_STYLE = "menu_labels_style";
    private static final String MENU_OPEN_DIRECTION = "menu_openDirection";
    private static final String MENU_BACKGROUND_COLOR = "menu_backgroundColor";
    private static final String MENU_FAB_LABEL = "menu_fab_label";
    private static final String MENU_LABELS_PADDING = "menu_labels_padding";
    private static final String MENU_LABELS_CUSTOM_FONT = "menu_labels_customFont";

    public interface OnMenuToggleListener {
        void onMenuToggle(boolean opened);
    }

    public FloatingActionMenu(Context context) {
        this(context, null);
    }

    public FloatingActionMenu(Context context, AttrSet attrSet) {
        this(context, attrSet, null);
    }

    public FloatingActionMenu(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        init(context, attrSet);
    }

    private void init(Context context, AttrSet attrs) {
        if (attrs != null) {
            boolean isPresentMenuButtonSpacing = attrs.getAttr(MENU_BUTTON_SPACING).isPresent();
            if (isPresentMenuButtonSpacing) {
                mButtonSpacing = attrs.getAttr(MENU_BUTTON_SPACING).get().getDimensionValue();
            }
            boolean isPresentMenuLabelsMargin = attrs.getAttr(MENU_LABELS_MARGIN).isPresent();
            if (isPresentMenuLabelsMargin) {
                mLabelsMargin = attrs.getAttr(MENU_LABELS_MARGIN).get().getDimensionValue();
            }
            boolean isPresentMenuLabelsPosition = attrs.getAttr(MENU_LABELS_POSITION).isPresent();
            if (isPresentMenuLabelsPosition) {
                mLabelsPosition = attrs.getAttr(MENU_LABELS_POSITION).get().getIntegerValue();
            } else {
                mLabelsPosition = LABELS_POSITION_LEFT;
            }
            boolean isPresentMenuLabelsPaddingTop = attrs.getAttr(MENU_LABELS_PADDING_TOP).isPresent();
            if (isPresentMenuLabelsPaddingTop) {
                mLabelsPaddingTop = attrs.getAttr(MENU_LABELS_PADDING_TOP).get().getDimensionValue();
            }
            boolean isPresentMenuLabelsPaddingRight = attrs.getAttr(MENU_LABELS_PADDING_RIGHT).isPresent();
            if (isPresentMenuLabelsPaddingRight) {
                mLabelsPaddingRight = attrs.getAttr(MENU_LABELS_PADDING_RIGHT).get().getDimensionValue();
            }
            boolean isPresentMenuLabelsPaddingBottom = attrs.getAttr(MENU_LABELS_PADDING_BOTTOM).isPresent();
            if (isPresentMenuLabelsPaddingBottom) {
                mLabelsPaddingBottom = attrs.getAttr(MENU_LABELS_PADDING_BOTTOM).get().getDimensionValue();
            }
            boolean isPresentMenuLabelsPaddingLeft = attrs.getAttr(MENU_LABELS_PADDING_LEFT).isPresent();
            if (isPresentMenuLabelsPaddingLeft) {
                mLabelsPaddingLeft = attrs.getAttr(MENU_LABELS_PADDING_LEFT).get().getDimensionValue();
            }
            boolean isPresentMenuLabelsTextColor = attrs.getAttr(MENU_LABELS_TEXT_COLOR).isPresent();
            if (isPresentMenuLabelsTextColor) {
                mLabelsTextColor = attrs.getAttr(MENU_LABELS_TEXT_COLOR).get().getColorValue();
            } else {
                mLabelsTextColor = Color.WHITE;
            }
            boolean isPresentMenuLabelsTextSize = attrs.getAttr(MENU_LABELS_TEXT_SIZE).isPresent();
            if (isPresentMenuLabelsTextSize) {
                mLabelsTextSize = attrs.getAttr(MENU_LABELS_TEXT_SIZE).get().getDimensionValue();
            } else {
                mLabelsTextSize = ResUtil.getIntDimen(context, ResourceTable.Float_labels_text_size);
            }
            boolean isPresentMenuLabelsCornerRadius = attrs.getAttr(MENU_LABELS_CORNER_RADIUS).isPresent();
            if (isPresentMenuLabelsCornerRadius) {
                mLabelsCornerRadius = attrs.getAttr(MENU_LABELS_CORNER_RADIUS).get().getDimensionValue();
            }
            boolean isPresentMenuLabelsShowShadow = attrs.getAttr(MENU_LABELS_SHOW_SHADOW).isPresent();
            if (isPresentMenuLabelsShowShadow) {
                mLabelsShowShadow = attrs.getAttr(MENU_LABELS_SHOW_SHADOW).get().getBoolValue();
            } else {
                mLabelsShowShadow = false;
            }

            boolean isPresentMenuLabelsColorNormal = attrs.getAttr(MENU_LABELS_COLOR_NORMAL).isPresent();
            if (isPresentMenuLabelsColorNormal) {
                mLabelsColorNormal = attrs.getAttr(MENU_LABELS_COLOR_NORMAL).get().getColorValue();
            } else {
                mLabelsColorNormal = new Color(0xFF333333);
            }
            boolean isPresentMenuLabelsColorPressed = attrs.getAttr(MENU_LABELS_COLOR_PRESSED).isPresent();
            if (isPresentMenuLabelsColorPressed) {
                mLabelsColorPressed = attrs.getAttr(MENU_LABELS_COLOR_PRESSED).get().getColorValue();
            } else {
                mLabelsColorPressed = new Color(0xFF444444);
            }
            boolean isPresentMenuLabelsColorRipple = attrs.getAttr(MENU_LABELS_COLOR_RIPPLE).isPresent();
            if (isPresentMenuLabelsColorRipple) {
                mLabelsColorRipple = attrs.getAttr(MENU_LABELS_COLOR_RIPPLE).get().getColorValue();
            } else {
                mLabelsColorRipple = new Color(0x66FFFFFF);
            }
            boolean isPresentMenuShowShadow = attrs.getAttr(MENU_SHOW_SHADOW).isPresent();
            if (isPresentMenuShowShadow) {
                mMenuShowShadow = attrs.getAttr(MENU_SHOW_SHADOW).get().getBoolValue();
            } else {
                mMenuShowShadow = false;
            }
            boolean isPresentMenuShadowColor = attrs.getAttr(MENU_SHADOW_COLOR).isPresent();
            if (isPresentMenuShadowColor) {
                mMenuShadowColor = attrs.getAttr(MENU_SHADOW_COLOR).get().getColorValue();
            } else {
                mMenuShadowColor = new Color(0x66000000);
            }
            boolean isPresentMenuShadowRadius = attrs.getAttr(MENU_SHADOW_RADIUS).isPresent();
            if (isPresentMenuShadowRadius) {
                mMenuShadowRadius = attrs.getAttr(MENU_SHADOW_RADIUS).get().getDimensionValue();
            }
            boolean isPresentMenuShadowXOffset = attrs.getAttr(MENU_SHADOW_XOFFSET).isPresent();
            if (isPresentMenuShadowXOffset) {
                mMenuShadowXOffset = attrs.getAttr(MENU_SHADOW_XOFFSET).get().getDimensionValue();
            }
            boolean isPresentMenuShadowYOffset = attrs.getAttr(MENU_SHADOW_YOFFSET).isPresent();
            if (isPresentMenuShadowYOffset) {
                mMenuShadowYOffset = attrs.getAttr(MENU_SHADOW_YOFFSET).get().getDimensionValue();
            }
            boolean isPresentMenuColorNormal = attrs.getAttr(MENU_COLOR_NORMAL).isPresent();
            if (isPresentMenuColorNormal) {
                mMenuColorNormal = attrs.getAttr(MENU_COLOR_NORMAL).get().getColorValue();
            } else {
                mMenuColorNormal = new Color(0xFFDA4336);
            }
            boolean isPresentMenuColorPressed = attrs.getAttr(MENU_COLOR_PRESSED).isPresent();
            if (isPresentMenuColorPressed) {
                mMenuColorPressed = attrs.getAttr(MENU_COLOR_PRESSED).get().getColorValue();
            } else {
                mMenuColorPressed = new Color(0xFFE75043);
            }
            boolean isPresentMenuColorRipple = attrs.getAttr(MENU_COLOR_RIPPLE).isPresent();
            if (isPresentMenuColorRipple) {
                mMenuColorRipple = attrs.getAttr(MENU_COLOR_RIPPLE).get().getColorValue();
            } else {
                mMenuColorRipple = new Color(0x99FFFFFF);
            }
            boolean isPresentMenuAnimationDelayPerItem = attrs.getAttr(MENU_ANIMATION_DELAY_PER_ITEM).isPresent();
            if (isPresentMenuAnimationDelayPerItem) {
                mAnimationDelayPerItem = attrs.getAttr(MENU_ANIMATION_DELAY_PER_ITEM).get().getIntegerValue();
            } else {
                mAnimationDelayPerItem = 500;
            }
            boolean isPresentMenuIcon = attrs.getAttr(MENU_ICON).isPresent();
            if (isPresentMenuIcon) {
                mIcon = attrs.getAttr(MENU_ICON).get().getElement();
            } else {
                mIcon = ResUtil.getPixelMapDrawable(getContext(), ResourceTable.Graphic_fab_add);
            }
            boolean isPresentMenuLabelsSingleLine = attrs.getAttr(MENU_LABELS_SINGLE_LINE).isPresent();
            if (isPresentMenuLabelsSingleLine) {
                mLabelsSingleLine = attrs.getAttr(MENU_LABELS_SINGLE_LINE).get().getBoolValue();
            } else {
                mLabelsSingleLine = false;
            }
            boolean isPresentMenuLabelsEllipsize = attrs.getAttr(MENU_LABELS_ELLIPSIZE).isPresent();
            if (isPresentMenuLabelsEllipsize) {
                mLabelsEllipsize = attrs.getAttr(MENU_LABELS_ELLIPSIZE).get().getIntegerValue();
            } else {
                mLabelsEllipsize = 0;
            }
            boolean isPresentMenuLabelsMaxLines = attrs.getAttr(MENU_LABELS_MAX_LINES).isPresent();
            if (isPresentMenuLabelsMaxLines) {
                mLabelsMaxLines = attrs.getAttr(MENU_LABELS_MAX_LINES).get().getIntegerValue();
            } else {
                mLabelsMaxLines = 1;
            }
            boolean isPresentMenuFabSize = attrs.getAttr(MENU_FAB_SIZE).isPresent();
            if (isPresentMenuFabSize) {
                mMenuFabSize = attrs.getAttr(MENU_FAB_SIZE).get().getIntegerValue();
            } else {
                mMenuFabSize = FloatingActionButton.SIZE_NORMAL;
            }
            boolean isPresentMenuLabelsStyle = attrs.getAttr(MENU_LABELS_STYLE).isPresent();
            if (isPresentMenuLabelsStyle) {
                mLabelsStyle = attrs.getAttr(MENU_LABELS_STYLE).get().getIntegerValue();
            } else {
                mLabelsStyle = 0;
            }
            String customFont = attrs.getAttr(MENU_LABELS_CUSTOM_FONT).isPresent() ?
                    attrs.getAttr(MENU_LABELS_CUSTOM_FONT).get().getStringValue() : null;
            try {
                if (customFont != null && !customFont.isEmpty()) {
                    mCustomTypefaceFromFont = ResUtil.createFont(context, customFont);
                }
            } catch (RuntimeException ex) {
                throw new IllegalArgumentException("Unable to load specified custom font: " + customFont, ex);
            }
            boolean isPresentMenuOpenDirection = attrs.getAttr(MENU_OPEN_DIRECTION).isPresent();
            if (isPresentMenuOpenDirection) {
                mOpenDirection = attrs.getAttr(MENU_OPEN_DIRECTION).get().getIntegerValue();
            } else {
                mOpenDirection = OPEN_UP;
            }
            boolean isPresentMenuBackgroundColor = attrs.getAttr(MENU_BACKGROUND_COLOR).isPresent();
            if (isPresentMenuBackgroundColor) {
                mBackgroundColor = attrs.getAttr(MENU_BACKGROUND_COLOR).get().getColorValue();
            } else {
                mBackgroundColor = Color.TRANSPARENT;
            }
            boolean isPresentMenuFabLabel = attrs.getAttr(MENU_FAB_LABEL).isPresent();
            if (isPresentMenuFabLabel) {
                mUsingMenuLabel = true;
                mMenuLabelText = attrs.getAttr(MENU_FAB_LABEL).get().getStringValue();
            }
            boolean isPresentMenuLabelsPadding = attrs.getAttr(MENU_LABELS_PADDING).isPresent();
            int padding = 0;
            if (isPresentMenuLabelsPadding) {
                padding = attrs.getAttr(MENU_LABELS_PADDING).get().getDimensionValue();
                initPadding(padding);
            }
        } else {
            mLabelsPosition = LABELS_POSITION_LEFT;
            mLabelsTextColor = Color.WHITE;
            mLabelsTextSize = ResUtil.getIntDimen(context, ResourceTable.Float_labels_text_size);
            mLabelsColorNormal = new Color(0xFF333333);
            mLabelsColorPressed = new Color(0xFF444444);
            mLabelsColorRipple = new Color(0x66FFFFFF);
            mMenuShadowColor = new Color(0x66000000);
            mMenuColorNormal = new Color(0xFFDA4336);
            mMenuColorPressed = new Color(0xFFE75043);
            mMenuColorRipple = new Color(0x99FFFFFF);
            mAnimationDelayPerItem = 500;
            mIcon = ResUtil.getPixelMapDrawable(getContext(), ResourceTable.Graphic_fab_add);
            mLabelsMaxLines = 1;
            mMenuFabSize = FloatingActionButton.SIZE_NORMAL;
            mOpenDirection = OPEN_UP;
            mBackgroundColor = Color.TRANSPARENT;
        }

        mLabelsContext = context;

        initBackgroundDimAnimation();
        createMenuButton(context);
        initMenuButtonAnimations();

        setTouchEventListener(this::onTouchEvent);
        addDrawTask(this::onDraw);
        setBindStateChangedListener(this);

    }

    @Override
    public void onComponentBoundToWindow(Component component) {
        onFinishInflate();
    }

    @Override
    public void onComponentUnboundFromWindow(Component component) {
        //TODO
    }

    @Override
    public void onRefreshed(Component component) {
        invalidate();
    }

    @Override
    public void onDraw(Component component, Canvas canvas) {
        onMeasure();
    }


    @Override
    public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
        if (mIsSetClosedOnTouchOutside) {
            boolean handled = false;
            switch (touchEvent.getAction()) {
                case TouchEvent.PRIMARY_POINT_DOWN:
                    handled = isOpened();
                    break;
                case TouchEvent.PRIMARY_POINT_UP:
                    close(mIsAnimated);
                    handled = true;
            }
            return handled;
        }

        return false;
    }

    private void initMenuButtonAnimations() {
        mImageToggleShowAnimation = AnimationUtil.loadAnimation("menu_button_show", mImageToggle, 400);
        setMenuButtonShowAnimation(mImageToggleShowAnimation);

        mImageToggleHideAnimation = AnimationUtil.loadAnimation("menu_button_hide", mImageToggle, 400);
        setMenuButtonHideAnimation(mImageToggleHideAnimation);
    }

    private void initBackgroundDimAnimation() {
        RgbColor rgbColor = RgbColor.fromArgbInt(mBackgroundColor.getValue());
        final int maxAlpha = Color.alpha(mBackgroundColor.getValue());
        final int red = rgbColor.getRed();
        final int green = rgbColor.getGreen();
        final int blue = rgbColor.getBlue();

        mShowBackgroundAnimator = new AnimatorValue();
        mShowBackgroundAnimator.setDuration(ANIMATION_DURATION);
        mShowBackgroundAnimator.setValueUpdateListener(new AnimatorValue.ValueUpdateListener() {
            @Override
            public void onUpdate(AnimatorValue animation, float v) {
                ShapeElement bgElement = new ShapeElement();
                bgElement.setRgbColor(new RgbColor(red, green, blue, (int) (maxAlpha * v)));
                setBackground(bgElement);
            }
        });

        mHideBackgroundAnimator = new AnimatorValue();
        mHideBackgroundAnimator.setDuration(ANIMATION_DURATION);
        mHideBackgroundAnimator.setValueUpdateListener(new AnimatorValue.ValueUpdateListener() {
            @Override
            public void onUpdate(AnimatorValue animation, float v) {
                ShapeElement bgElement = new ShapeElement();
                bgElement.setRgbColor(new RgbColor(red, green, blue, 1 - (int) (maxAlpha * v)));
                setBackground(bgElement);
            }
        });
    }

    private boolean isBackgroundEnabled() {
        return mBackgroundColor != Color.TRANSPARENT;
    }

    private void initPadding(int padding) {
        mLabelsPaddingTop = padding;
        mLabelsPaddingRight = padding;
        mLabelsPaddingBottom = padding;
        mLabelsPaddingLeft = padding;
    }

    private void createMenuButton(Context context) {
        mMenuButton = new FloatingActionButton(context);

        mMenuButton.mShowShadow = mMenuShowShadow;
        if (mMenuShowShadow) {
            mMenuButton.mShadowRadius = Util.vpToPx(context, mMenuShadowRadius);
            mMenuButton.mShadowXOffset = Util.vpToPx(context, mMenuShadowXOffset);
            mMenuButton.mShadowYOffset = Util.vpToPx(context, mMenuShadowYOffset);
        }
        mMenuButton.setColors(mMenuColorNormal.getValue(), mMenuColorPressed.getValue(), mMenuColorRipple.getValue());
        mMenuButton.mShadowColor = mMenuShadowColor.getValue();
        mMenuButton.mFabSize = mMenuFabSize;
        mMenuButton.updateBackground();
        //mMenuButton.setLabelText(mMenuLabelText);

        mImageToggle = new Image(getContext());
        mImageToggle.setImageElement(mIcon);

        addComponent(mMenuButton);
        addComponent(mImageToggle);

        createDefaultIconAnimation();
    }

    private void createDefaultIconAnimation() {
        float collapseAngle;
        float expandAngle;
        if (mOpenDirection == OPEN_UP) {
            collapseAngle = mLabelsPosition == LABELS_POSITION_LEFT ? OPENED_PLUS_ROTATION_LEFT : OPENED_PLUS_ROTATION_RIGHT;
            expandAngle = mLabelsPosition == LABELS_POSITION_LEFT ? OPENED_PLUS_ROTATION_LEFT : OPENED_PLUS_ROTATION_RIGHT;
        } else {
            collapseAngle = mLabelsPosition == LABELS_POSITION_LEFT ? OPENED_PLUS_ROTATION_RIGHT : OPENED_PLUS_ROTATION_LEFT;
            expandAngle = mLabelsPosition == LABELS_POSITION_LEFT ? OPENED_PLUS_ROTATION_RIGHT : OPENED_PLUS_ROTATION_LEFT;
        }

        XAnimatorValue collapseAnimator = XAnimatorValue.ofFloat(mImageToggle, AnimatorTypes.ROTATE,
                ANIMATION_DURATION, collapseAngle, CLOSED_PLUS_ROTATION);

        XAnimatorValue expandAnimator = XAnimatorValue.ofFloat(mImageToggle, AnimatorTypes.ROTATE,
                ANIMATION_DURATION, CLOSED_PLUS_ROTATION, expandAngle);

        mOpenAnimatorSet.runSerially(expandAnimator);
        mCloseAnimatorSet.runSerially(collapseAnimator);

        mOpenAnimatorSet.setCurveType(Animator.CurveType.OVERSHOOT);
        mCloseAnimatorSet.setCurveType(Animator.CurveType.ANTICIPATE);

    }

    protected void onMeasure() {
        int width = 0;
        int height = 0;
        mMaxButtonWidth = 0;
        int maxLabelWidth = 0;

        for (int i = 0; i < mButtonsCount; i++) {
            Component child = getComponentAt(i);

            if (child.getVisibility() == HIDE || child == mImageToggle) continue;

            mMaxButtonWidth = Math.max(mMaxButtonWidth, ((FloatingActionButton) child).calculateMeasuredWidth());
        }

        for (int i = 0; i < mButtonsCount; i++) {
            int usedWidth = 0;
            Component child = getComponentAt(i);

            if (child.getVisibility() == HIDE || child == mImageToggle) continue;

            usedWidth += ((FloatingActionButton) child).calculateMeasuredWidth();
            height += ((FloatingActionButton) child).calculateMeasuredHeight();

            Label label = (Label) child.getTag();
            if (label != null) {
                int labelOffset = (mMaxButtonWidth - ((FloatingActionButton) child).
                        calculateMeasuredWidth()) / (mUsingMenuLabel ? 1 : 2);
                int labelUsedWidth = ((FloatingActionButton) child).calculateMeasuredWidth()
                        + label.calculateShadowWidth() + mLabelsMargin + labelOffset;
                usedWidth += label.calculateMeasuredWidth();
                maxLabelWidth = Math.max(maxLabelWidth, usedWidth + labelOffset);
            }
        }

        width = Math.max(mMaxButtonWidth, maxLabelWidth + mLabelsMargin) + getPaddingLeft() + getPaddingRight();

        height += mButtonSpacing * (mButtonsCount - 1) + getPaddingTop() + getPaddingBottom();
        height = adjustForOvershoot(height);

        if (getLayoutConfig().width == LayoutConfig.MATCH_PARENT) {
            width = getWidth();
        }

        if (getLayoutConfig().height == LayoutConfig.MATCH_PARENT) {
            height = getHeight();
        }

        ComponentContainer.LayoutConfig layoutConfig = getLayoutConfig();
        layoutConfig.width = width;
        layoutConfig.height = height;
        setLayoutConfig(layoutConfig);

        Rect currentPosition = getComponentPosition();
        int newLeft = currentPosition.right - width;
        int newTop = currentPosition.bottom - height;
        onLayout(newLeft, newTop, currentPosition.right, currentPosition.bottom);
    }

    protected void onLayout(int l, int t, int r, int b) {
        int buttonsHorizontalCenter = mLabelsPosition == LABELS_POSITION_LEFT
                ? r - l - mMaxButtonWidth / 2 - getPaddingRight()
                : mMaxButtonWidth / 2 + getPaddingLeft();
        boolean openUp = mOpenDirection == OPEN_UP;

        int menuButtonTop = openUp
                ? b - t - mMenuButton.calculateMeasuredHeight() - getPaddingBottom()
                : getPaddingTop();
        int menuButtonLeft = buttonsHorizontalCenter - mMenuButton.calculateMeasuredWidth() / 2;

        mMenuButton.setComponentPosition(menuButtonLeft, menuButtonTop, menuButtonLeft + mMenuButton.calculateMeasuredWidth(),
                menuButtonTop + mMenuButton.calculateMeasuredHeight());

        int imageLeft = buttonsHorizontalCenter - mImageToggle.getWidth() / 2;
        int imageTop = menuButtonTop + mMenuButton.calculateMeasuredHeight() / 2 - mImageToggle.getHeight() / 2;

        mImageToggle.setComponentPosition(imageLeft, imageTop, imageLeft + mImageToggle.getWidth(),
                imageTop + mImageToggle.getHeight());

        int nextY = openUp
                ? menuButtonTop + mMenuButton.calculateMeasuredHeight() + mButtonSpacing
                : menuButtonTop;

        for (int i = mButtonsCount - 1; i >= 0; i--) {
            Component child = getComponentAt(i);

            if (child == mImageToggle) continue;

            FloatingActionButton fab = (FloatingActionButton) child;

            if (fab.getVisibility() == HIDE) continue;

            int childX = buttonsHorizontalCenter - fab.calculateMeasuredWidth() / 2;
            int childY = openUp ? nextY - fab.calculateMeasuredHeight() - mButtonSpacing : nextY;

            if (fab != mMenuButton) {
                fab.setComponentPosition(childX, childY, childX + fab.calculateMeasuredWidth(),
                        childY + fab.calculateMeasuredHeight());

                if (!mIsMenuOpening) {
                    fab.hide(false);
                }
            }

            Label label = (Label) fab.getTag();
            if (label != null) {
                int labelsOffset = (mUsingMenuLabel ? mMaxButtonWidth / 2 : fab.calculateMeasuredWidth() / 2) + mLabelsMargin;
                int labelXNearButton = mLabelsPosition == LABELS_POSITION_LEFT
                        ? buttonsHorizontalCenter - labelsOffset
                        : buttonsHorizontalCenter + labelsOffset;

                int labelXAwayFromButton = mLabelsPosition == LABELS_POSITION_LEFT
                        ? labelXNearButton - label.calculateMeasuredWidth()
                        : labelXNearButton + label.calculateMeasuredWidth();

                int labelLeft = mLabelsPosition == LABELS_POSITION_LEFT
                        ? labelXAwayFromButton
                        : labelXNearButton;

                int labelRight = mLabelsPosition == LABELS_POSITION_LEFT
                        ? labelXNearButton
                        : labelXAwayFromButton;

                int labelTop = childY - mLabelsVerticalOffset + (fab.calculateMeasuredHeight()
                        - label.calculateMeasuredHeight()) / 2;

                label.setComponentPosition(labelLeft, labelTop, labelRight, labelTop + label.calculateMeasuredHeight());

                if (!mIsMenuOpening) {
                    label.setVisibility(INVISIBLE);
                }
            }

            nextY = openUp
                    ? childY - mButtonSpacing
                    : childY + fab.calculateMeasuredHeight() + mButtonSpacing;
        }
    }

    private int adjustForOvershoot(int dimension) {
        return (int) (dimension * 0.1 + dimension);
    }

    protected void onFinishInflate() {
        moveChildToFront(mMenuButton);
        moveChildToFront(mImageToggle);
        mButtonsCount = getChildCount();
        createLabels();
    }

    private void createLabels() {
        for (int i = 0; i < mButtonsCount; i++) {

            if (getComponentAt(i) == mImageToggle) continue;

            final FloatingActionButton fab = (FloatingActionButton) getComponentAt(i);

            if (fab.getTag() != null) continue;

            addLabel(fab);

            if (fab == mMenuButton) {
                mMenuButton.setClickedListener(new ClickedListener() {
                    @Override
                    public void onClick(Component component) {
                        toggle(mIsAnimated);
                    }
                });
            }
        }
    }

    private void addLabel(FloatingActionButton fab) {
        String text = fab.getLabelText();

        if (text == null || text.isEmpty()) return;

        final Label label = new Label(mLabelsContext);
        label.setClickable(true);
        label.setFab(fab);
        label.setShowAnimation(label.createAnimatorProperty().scaleXFrom(0.0f).
                scaleX(1.0f).scaleYFrom(0.0f).scaleY(1.0f).setDuration(500));
        label.setHideAnimation(label.createAnimatorProperty().scaleXFrom(0.0f).
                scaleX(1.0f).scaleYFrom(0.0f).scaleY(1.0f).setDuration(500));

        if (mLabelsStyle > 0) {
            label.setShowShadow(false);
            label.setUsingStyle(true);
        } else {
            label.setColors(mLabelsColorNormal.getValue(), mLabelsColorPressed.getValue(), mLabelsColorRipple.getValue());
            label.setShowShadow(mLabelsShowShadow);
            label.setCornerRadius(mLabelsCornerRadius);
            if (mLabelsEllipsize > 0) {
                setLabelEllipsize(label);
            }
            label.setMaxTextLines(mLabelsMaxLines);
            label.setMultipleLine(false);
            label.setMaxTextWidth(Util.vpToPx(getContext(), 200));
            label.updateBackground();

            label.setTextSize((int) mLabelsTextSize);
            label.setTextColor(mLabelsTextColor);

            int left = mLabelsPaddingLeft;
            int top = mLabelsPaddingTop;
            if (mLabelsShowShadow) {
                left += fab.getShadowRadius() + Math.abs(fab.getShadowXOffset());
                top += fab.getShadowRadius() + Math.abs(fab.getShadowYOffset());
            }

            label.setPadding(
                    left,
                    top,
                    mLabelsPaddingLeft,
                    mLabelsPaddingTop
            );

            if (mLabelsMaxLines < 0 || mLabelsSingleLine) {
                label.setMaxTextLines(mLabelsSingleLine ? 1 : label.getMaxTextLines());
            }
        }

        if (mCustomTypefaceFromFont != null) {
            label.setFont(mCustomTypefaceFromFont);
        }
        label.setText(text);
        label.setClickedListener(fab.getOnClickListener());

        addComponent(label);
        fab.setTag(label);
    }

    private void setLabelEllipsize(Label label) {
        switch (mLabelsEllipsize) {
            case 1:
                label.setTruncationMode(Text.TruncationMode.ELLIPSIS_AT_START);
                break;
            case 2:
                label.setTruncationMode(Text.TruncationMode.ELLIPSIS_AT_MIDDLE);
                break;
            case 3:
                label.setTruncationMode(Text.TruncationMode.ELLIPSIS_AT_END);
                break;
            case 4:
                label.setTruncationMode(Text.TruncationMode.AUTO_SCROLLING);
                break;
            default:
                break;
        }
    }

    private void hideMenuButtonWithImage(boolean animate) {
        if (!isMenuButtonHidden()) {
            mMenuButton.hide(animate);
            if (animate) {
                mImageToggleHideAnimation.start();
            }
            mImageToggle.setVisibility(INVISIBLE);
            mIsMenuButtonAnimationRunning = false;
        }
    }

    private void showMenuButtonWithImage(boolean animate) {
        if (isMenuButtonHidden()) {
            mMenuButton.show(animate);
            if (animate) {
                mImageToggleShowAnimation.start();
            }
            mImageToggle.setVisibility(VISIBLE);
        }
    }

    /* ===== API methods ===== */

    public boolean isOpened() {
        return mMenuOpened;
    }

    public void toggle(boolean animate) {
        if (isOpened()) {
            close(animate);
        } else {
            open(animate);
        }
    }

    public void open(final boolean animate) {
        if (!isOpened()) {
            if (isBackgroundEnabled()) {
                mShowBackgroundAnimator.start();
            }

            createDefaultIconAnimation();
            if (mIconAnimated) {
                if (mIconToggleSet != null) {
                    mIconToggleSet.start();
                } else {
                    mCloseAnimatorSet.cancel();
                    mOpenAnimatorSet.start();
                }
            }

            int delay = 0;
            int counter = 0;
            mIsMenuOpening = true;
            for (int i = getChildCount() - 1; i >= 0; i--) {
                Component child = getComponentAt(i);
                if (child instanceof FloatingActionButton && child.getVisibility() != HIDE) {
                    counter++;

                    final FloatingActionButton fab = (FloatingActionButton) child;
                    mUiHandler.postTask(new Runnable() {
                        @Override
                        public void run() {
                            if (isOpened()) return;

                            if (fab != mMenuButton) {
                                fab.show(animate);
                            }

                            Label label = (Label) fab.getTag();
                            if (label != null && label.isHandleVisibilityChanges()) {
                                label.show(animate);
                            }
                        }
                    }, delay);
                    delay += mAnimationDelayPerItem;
                }
            }

            mUiHandler.postTask(new Runnable() {
                @Override
                public void run() {
                    mMenuOpened = true;

                    if (mToggleListener != null) {
                        mToggleListener.onMenuToggle(true);
                    }
                }
            }, ++counter * mAnimationDelayPerItem);
        }
    }

    public void close(final boolean animate) {
        if (isOpened()) {
            if (isBackgroundEnabled()) {
                mHideBackgroundAnimator.start();
            }

            createDefaultIconAnimation();
            if (mIconAnimated) {
                if (mIconToggleSet != null) {
                    mIconToggleSet.start();
                } else {
                    mCloseAnimatorSet.start();
                    mOpenAnimatorSet.cancel();
                }
            }

            int delay = 0;
            int counter = 0;
            mIsMenuOpening = false;
            for (int i = 0; i < getChildCount(); i++) {
                Component child = getComponentAt(i);
                if (child instanceof FloatingActionButton && child.getVisibility() != HIDE) {
                    counter++;

                    final FloatingActionButton fab = (FloatingActionButton) child;
                    mUiHandler.postTask(new Runnable() {
                        @Override
                        public void run() {
                            if (!isOpened()) return;

                            if (fab != mMenuButton) {
                                fab.hide(animate);
                            }

                            Label label = (Label) fab.getTag();
                            if (label != null && label.isHandleVisibilityChanges()) {
                                label.hide(animate);
                            }
                        }
                    }, delay);
                    delay += mAnimationDelayPerItem;
                }
            }

            mUiHandler.postTask(new Runnable() {
                @Override
                public void run() {
                    mMenuOpened = false;

                    if (mToggleListener != null) {
                        mToggleListener.onMenuToggle(false);
                    }
                }
            }, ++counter * mAnimationDelayPerItem);
        }
    }


    public void setIconAnimationInterpolator(int interpolator) {
        mOpenAnimatorSet.setCurveType(interpolator);
        mCloseAnimatorSet.setCurveType(interpolator);
    }

    public void setIconAnimationOpenInterpolator(int openInterpolator) {
        mOpenAnimatorSet.setCurveType(openInterpolator);
    }

    public void setIconAnimationCloseInterpolator(int closeInterpolator) {
        mCloseAnimatorSet.setCurveType(closeInterpolator);
    }

    /**
     * Sets whether open and close actions should be animated
     *
     * @param animated if <b>false</b> - menu items will appear/disappear instantly without any animation
     */
    public void setAnimated(boolean animated) {
        mIsAnimated = animated;
        mOpenAnimatorSet.setDuration(animated ? ANIMATION_DURATION : 0);
        mCloseAnimatorSet.setDuration(animated ? ANIMATION_DURATION : 0);
    }

    public boolean isAnimated() {
        return mIsAnimated;
    }

    public void setAnimationDelayPerItem(int animationDelayPerItem) {
        mAnimationDelayPerItem = animationDelayPerItem;
    }

    public int getAnimationDelayPerItem() {
        return mAnimationDelayPerItem;
    }

    public void setOnMenuToggleListener(OnMenuToggleListener listener) {
        mToggleListener = listener;
    }

    public void setIconAnimated(boolean animated) {
        mIconAnimated = animated;
    }

    public boolean isIconAnimated() {
        return mIconAnimated;
    }

    public Image getMenuIconView() {
        return mImageToggle;
    }

    public void setIconToggleAnimatorSet(AnimatorGroup toggleAnimatorSet) {
        mIconToggleSet = toggleAnimatorSet;
    }

    public AnimatorGroup getIconToggleAnimatorSet() {
        return mIconToggleSet;
    }

    public void setMenuButtonShowAnimation(AnimatorGroup showAnimation) {
        mMenuButtonShowAnimation = showAnimation;
        mMenuButton.setShowAnimation(showAnimation);
    }

    public void setMenuButtonHideAnimation(AnimatorGroup hideAnimation) {
        mMenuButtonHideAnimation = hideAnimation;
        mMenuButton.setHideAnimation(hideAnimation);
    }

    public boolean isMenuHidden() {
        return getVisibility() == INVISIBLE;
    }

    public boolean isMenuButtonHidden() {
        return mMenuButton.isHidden();
    }

    /**
     * Makes the whole {@link #FloatingActionMenu} to appear and sets its visibility to {@link #VISIBLE}
     *
     * @param animate if true - plays "show animation"
     */
    public void showMenu(boolean animate) {
        if (isMenuHidden()) {
            if (animate) {
                mMenuButtonShowAnimation.start();
            }
            setVisibility(VISIBLE);
        }
    }

    /**
     * Makes the {@link #FloatingActionMenu} to disappear and sets its visibility to {@link #INVISIBLE}
     *
     * @param animate if true - plays "hide animation"
     */
    public void hideMenu(final boolean animate) {
        if (!isMenuHidden() && !mIsMenuButtonAnimationRunning) {
            mIsMenuButtonAnimationRunning = true;
            if (isOpened()) {
                close(animate);
                mUiHandler.postTask(new Runnable() {
                    @Override
                    public void run() {
                        if (animate) {
                            mMenuButtonHideAnimation.start();
                        }
                        setVisibility(INVISIBLE);
                        mIsMenuButtonAnimationRunning = false;
                    }
                }, mAnimationDelayPerItem * mButtonsCount);
            } else {
                if (animate) {
                    mMenuButtonHideAnimation.start();
                }
                setVisibility(INVISIBLE);
                mIsMenuButtonAnimationRunning = false;
            }
        }
    }

    public void toggleMenu(boolean animate) {
        if (isMenuHidden()) {
            showMenu(animate);
        } else {
            hideMenu(animate);
        }
    }

    /**
     * Makes the {@link FloatingActionButton} to appear inside the {@link #FloatingActionMenu} and
     * sets its visibility to {@link #VISIBLE}
     *
     * @param animate if true - plays "show animation"
     */
    public void showMenuButton(boolean animate) {
        if (isMenuButtonHidden()) {
            showMenuButtonWithImage(animate);
        }
    }

    /**
     * Makes the {@link FloatingActionButton} to disappear inside the {@link #FloatingActionMenu} and
     * sets its visibility to {@link #INVISIBLE}
     *
     * @param animate if true - plays "hide animation"
     */
    public void hideMenuButton(final boolean animate) {
        if (!isMenuButtonHidden() && !mIsMenuButtonAnimationRunning) {
            mIsMenuButtonAnimationRunning = true;
            if (isOpened()) {
                close(animate);
                mUiHandler.postTask(new Runnable() {
                    @Override
                    public void run() {
                        hideMenuButtonWithImage(animate);
                    }
                }, mAnimationDelayPerItem * mButtonsCount);
            } else {
                hideMenuButtonWithImage(animate);
            }
        }
    }

    public void toggleMenuButton(boolean animate) {
        if (isMenuButtonHidden()) {
            showMenuButton(animate);
        } else {
            hideMenuButton(animate);
        }
    }

    public void setClosedOnTouchOutside(boolean close) {
        mIsSetClosedOnTouchOutside = close;
    }

    public void setMenuButtonColorNormal(int color) {
        mMenuColorNormal = new Color(color);
        mMenuButton.setColorNormal(color);
    }

    public void setMenuButtonColorNormalResId(int colorResId) {
        mMenuColorNormal = new Color(ResUtil.getColor(getContext(), colorResId));
        mMenuButton.setColorNormalResId(colorResId);
    }

    public int getMenuButtonColorNormal() {
        return mMenuColorNormal.getValue();
    }

    public void setMenuButtonColorPressed(int color) {
        mMenuColorPressed = new Color(color);
        mMenuButton.setColorPressed(color);
    }

    public void setMenuButtonColorPressedResId(int colorResId) {
        mMenuColorPressed = new Color(ResUtil.getColor(getContext(), colorResId));
        mMenuButton.setColorPressedResId(colorResId);
    }

    public int getMenuButtonColorPressed() {
        return mMenuColorPressed.getValue();
    }

    public void setMenuButtonColorRipple(int color) {
        mMenuColorRipple = new Color(color);
        mMenuButton.setColorRipple(color);
    }

    public void setMenuButtonColorRippleResId(int colorResId) {
        mMenuColorRipple = new Color(ResUtil.getColor(getContext(), colorResId));
        mMenuButton.setColorRippleResId(colorResId);
    }

    public int getMenuButtonColorRipple() {
        return mMenuColorRipple.getValue();
    }

    public void addMenuButton(FloatingActionButton fab) {
        addComponent(fab, mButtonsCount - 2);
        mButtonsCount++;
        //addLabel(fab);
    }

    public void removeMenuButton(FloatingActionButton fab) {
        removeComponent(fab.getLabelView());
        removeComponent(fab);
        mButtonsCount--;
    }

    public void addMenuButton(FloatingActionButton fab, int index) {
        int size = mButtonsCount - 2;
        if (index < 0) {
            index = 0;
        } else if (index > size) {
            index = size;
        }

        addComponent(fab, index);
        mButtonsCount++;
        addLabel(fab);
    }

    public void removeAllMenuButtons() {
        close(true);

        List<FloatingActionButton> viewsToRemove = new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) {
            Component v = getComponentAt(i);
            if (v != mMenuButton && v != mImageToggle && v instanceof FloatingActionButton) {
                viewsToRemove.add((FloatingActionButton) v);
            }
        }
        for (FloatingActionButton v : viewsToRemove) {
            removeMenuButton(v);
        }
    }

    public void setMenuButtonLabelText(String text) {
        mMenuButton.setLabelText(text);
    }

    public String getMenuButtonLabelText() {
        return mMenuLabelText;
    }

    public void setOnMenuButtonClickListener(ClickedListener clickListener) {
        mMenuButton.setClickedListener(clickListener);
    }

    public void setOnMenuButtonLongClickListener(LongClickedListener longClickListener) {
        mMenuButton.setLongClickedListener(longClickListener);
    }
}
