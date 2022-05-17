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

package com.github.clans.fab.sample;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.Label;
import com.github.clans.fab.util.ResUtil;
import com.github.clans.fab.util.Util;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import ohos.aafwk.ability.delegation.AbilityDelegatorRegistry;
import ohos.agp.components.Component;
import ohos.agp.components.element.Element;
import ohos.agp.utils.Color;
import ohos.app.Context;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(JUnitParamsRunner.class)
public class FloatingActionButtonTest {
    private FloatingActionButton mFloatingActionButton;
    private Context mContext;

    private static final Object[] getButtonSizes() {
        return new Object[]{
                new Object[]{FloatingActionButton.SIZE_MINI},
                new Object[]{FloatingActionButton.SIZE_NORMAL}
        };
    }

    private static final Object[] getFABStateColors() {
        return new Object[]{
                new Object[]{Color.RED, Color.GREEN, Color.BLUE},
                new Object[]{Color.BLUE, Color.RED, Color.GREEN},
                new Object[]{Color.GREEN, Color.BLUE, Color.RED}
        };
    }

    private static final Object[] getOffsetData() {
        return new Float[][]{{1.0f}, {2.0f}, {3.0f}};
    }

    private static final Object[] getRandomLabelText() {
        return new String[][]{{"Label : 1"}, {"Label : 2"}, {"Label : 3"}};
    }

    @Before
    public void setUp() {
        mContext = AbilityDelegatorRegistry.getAbilityDelegator().getAppContext();
        mFloatingActionButton = new FloatingActionButton(mContext);
    }

    @Test
    public void shouldSetFABForegroundImageSource() {
        Element expectedElement = ResUtil.getPixelMapDrawable(mContext, ResourceTable.Media_ic_edit);
        mFloatingActionButton.setImageElement(expectedElement);
        assertEquals(expectedElement, mFloatingActionButton.getIconDrawable());
    }

    @Test
    @Parameters(method = "getButtonSizes")
    public void shouldSetFABButtonSize(int size) {
        mFloatingActionButton.setButtonSize(size);
        assertEquals(size, mFloatingActionButton.getButtonSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setSizeShouldThrowIAEForInvalidSize() {
        mFloatingActionButton.setButtonSize(3);
    }

    @Test
    public void measuredFABWidthAndHeightShouldNotBeZero() {
        assertNotEquals(0, mFloatingActionButton.calculateMeasuredWidth());
        assertNotEquals(0, mFloatingActionButton.calculateMeasuredHeight());
    }

    @Test
    public void shouldCreateCircleShapedDrawable() {
        assertNotNull(mFloatingActionButton.createCircleDrawable(Color.RED.getValue()));
    }

    @Test
    public void shouldSetFABClickedListener() {
        Component.ClickedListener fabClickedListener = new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                ResUtil.showToast(mContext, "FAB Clicked", 500);
            }
        };
        mFloatingActionButton.setClickedListener(fabClickedListener);
        assertEquals(fabClickedListener, mFloatingActionButton.getClickedListener());
    }

    @Test
    @Parameters(method = "getFABStateColors")
    public void shouldSetFABStateColors(Color colorNormal, Color colorPressed, Color colorRipple) {
        mFloatingActionButton.setColorNormal(colorNormal.getValue());
        assertEquals(colorNormal.getValue(), mFloatingActionButton.getColorNormal());
        mFloatingActionButton.setColorPressed(colorPressed.getValue());
        assertEquals(colorPressed.getValue(), mFloatingActionButton.getColorPressed());
        mFloatingActionButton.setColorRipple(colorRipple.getValue());
        assertEquals(colorRipple.getValue(), mFloatingActionButton.getColorRipple());
    }

    @Test
    public void shouldSetFABDisabledColor() {
        mFloatingActionButton.setColorDisabled(Color.RED.getValue());
        assertEquals(Color.RED.getValue(), mFloatingActionButton.getColorDisabled());
    }

    @Test
    public void shouldSetFABShadowColor() {
        mFloatingActionButton.setShadowColor(Color.RED.getValue());
        assertEquals(Color.RED.getValue(), mFloatingActionButton.getShadowColor());
    }

    @Test
    public void shouldSetFABShadowRadius() {
        mFloatingActionButton.setShadowRadius(1.0f);
        assertEquals(Util.vpToPx(mContext, 1.0f), mFloatingActionButton.getShadowRadius(), 0.0f);
    }

    @Test
    @Parameters(method = "getOffsetData")
    public void shouldSetFABShadowXAndYOffset(float offset) {
        mFloatingActionButton.setShadowXOffset(offset);
        assertEquals(Util.vpToPx(mContext, offset), mFloatingActionButton.getShadowXOffset(), 0.0f);
        mFloatingActionButton.setShadowYOffset(offset);
        assertEquals(Util.vpToPx(mContext, offset), mFloatingActionButton.getShadowYOffset(), 0.0f);
    }

    @Test
    public void shouldShowFAB() {
        mFloatingActionButton.show(true);
        assertTrue(mFloatingActionButton.getVisibility() == Component.VISIBLE);
    }

    @Test
    public void shouldHideFAB() {
        mFloatingActionButton.hide(true);
        assertTrue(mFloatingActionButton.getVisibility() == Component.INVISIBLE);
    }

    @Test
    @Parameters(method = "getRandomLabelText")
    public void shouldSetFABLabelText(String labelTxt) {
        mFloatingActionButton.setLabelText(labelTxt);
        assertEquals(labelTxt, mFloatingActionButton.getLabelText());
    }

    @Test
    public void shouldShowFABLabel() {
        mFloatingActionButton.setTag(new Label(mContext));
        mFloatingActionButton.setLabelVisibility(Component.VISIBLE);
        assertTrue(mFloatingActionButton.getLabelVisibility() == Component.VISIBLE);
    }

    @Test
    public void shouldHideFABLabel() {
        mFloatingActionButton.setTag(new Label(mContext));
        mFloatingActionButton.setLabelVisibility(Component.INVISIBLE);
        assertTrue(mFloatingActionButton.getLabelVisibility() == Component.INVISIBLE);
    }

    @Test
    public void shouldSetFABMaxProgress() {
        mFloatingActionButton.setMax(50);
        assertEquals(50, mFloatingActionButton.getMax());
    }

    @Test
    public void shouldShowBackgroundForFABProgress() {
        mFloatingActionButton.setShowProgressBackground(true);
        assertTrue(mFloatingActionButton.isProgressBackgroundShown());
    }

}