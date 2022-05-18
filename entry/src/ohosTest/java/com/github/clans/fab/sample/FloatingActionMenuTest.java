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
import com.github.clans.fab.FloatingActionMenu;
import com.github.clans.fab.anim.AnimatorTypes;
import com.github.clans.fab.anim.XAnimatorValue;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import ohos.aafwk.ability.delegation.AbilityDelegatorRegistry;
import ohos.agp.animation.Animator;
import ohos.agp.animation.AnimatorGroup;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.utils.Color;
import ohos.app.Context;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(JUnitParamsRunner.class)
public class FloatingActionMenuTest {
    private FloatingActionMenu mFloatingActionMenu;
    private FloatingActionButton mFloatingActionButton;
    private Context mContext;

    private static final Object[] getAnimationDelayData() {
        return new Integer[][]{{100}, {200}, {500}};
    }

    private static final Object[] provideIconToggleAnimator() {
        Image mImageToggle = new Image(AbilityDelegatorRegistry.getAbilityDelegator().getAppContext());
        XAnimatorValue collapseAnimator = XAnimatorValue.ofFloat(mImageToggle, AnimatorTypes.ROTATE,
                500, 0, 360);

        XAnimatorValue expandAnimator = XAnimatorValue.ofFloat(mImageToggle, AnimatorTypes.ROTATE,
                500, 360, 0);

        AnimatorGroup mOpenAnimatorSet = new AnimatorGroup();
        AnimatorGroup mCloseAnimatorSet = new AnimatorGroup();
        AnimatorGroup mIconToggleSet = new AnimatorGroup();
        ;

        mOpenAnimatorSet.runSerially(expandAnimator);
        mCloseAnimatorSet.runSerially(collapseAnimator);

        mOpenAnimatorSet.setCurveType(Animator.CurveType.OVERSHOOT);
        mCloseAnimatorSet.setCurveType(Animator.CurveType.ANTICIPATE);

        mIconToggleSet.build().addAnimators(mOpenAnimatorSet, mCloseAnimatorSet);
        return new Object[]{
                new Object[]{mIconToggleSet}
        };
    }

    private static final Object[] getStateColors() {
        return new Object[]{
                new Object[]{Color.RED, Color.GREEN, Color.BLUE},
                new Object[]{Color.BLUE, Color.RED, Color.GREEN},
                new Object[]{Color.GREEN, Color.BLUE, Color.RED}
        };
    }

    @Before
    public void setUp() {
        mContext = AbilityDelegatorRegistry.getAbilityDelegator().getAppContext();
        mFloatingActionMenu = new FloatingActionMenu(mContext);
        mFloatingActionButton = new FloatingActionButton(mContext);
        mFloatingActionMenu.addComponent(mFloatingActionButton);
    }

    @Test
    public void shouldSetAnimationForFloatingActionMenu() {
        mFloatingActionMenu.setAnimated(true);
        assertTrue(mFloatingActionMenu.isAnimated());
    }

    @Test
    public void shouldCancelAnimationForFloatingActionMenu() {
        mFloatingActionMenu.setAnimated(false);
        assertFalse(mFloatingActionMenu.isAnimated());
    }

    @Test
    public void shouldSetAnimationForMenuIcon() {
        mFloatingActionMenu.setIconAnimated(true);
        assertTrue(mFloatingActionMenu.isIconAnimated());
    }

    @Test
    public void shouldCancelAnimationForMenuIcon() {
        mFloatingActionMenu.setIconAnimated(false);
        assertFalse(mFloatingActionMenu.isIconAnimated());
    }

    @Test
    @Parameters(method = "getAnimationDelayData")
    public void shouldSetAnimationDelayPerChildItem(int delay) {
        mFloatingActionMenu.setAnimationDelayPerItem(delay);
        assertEquals(delay, mFloatingActionMenu.getAnimationDelayPerItem());
    }


    @Test
    @Parameters(method = "provideIconToggleAnimator")
    public void shouldSetIconToggleAnimation(AnimatorGroup mIconToggleSet) {
        mFloatingActionMenu.setIconToggleAnimatorSet(mIconToggleSet);
        assertEquals(mIconToggleSet, mFloatingActionMenu.getIconToggleAnimatorSet());
    }

    @Test
    public void shouldShowFloatingActionMenu() {
        mFloatingActionMenu.showMenu(true);
        assertFalse(mFloatingActionMenu.isMenuHidden());
    }

    @Test
    public void shouldHideFloatingActionMenu() {
        mFloatingActionMenu.hideMenu(true);
        assertTrue(mFloatingActionMenu.isMenuHidden());
    }

    @Test
    @Parameters(method = "getStateColors")
    public void shouldSetFloatingActionMenuStateColors(Color colorNormal, Color colorPressed, Color colorRipple) {
        mFloatingActionMenu.setMenuButtonColorNormal(colorNormal.getValue());
        assertEquals(colorNormal.getValue(), mFloatingActionMenu.getMenuButtonColorNormal());
        mFloatingActionMenu.setMenuButtonColorPressed(colorPressed.getValue());
        assertEquals(colorPressed.getValue(), mFloatingActionMenu.getMenuButtonColorPressed());
        mFloatingActionMenu.setMenuButtonColorRipple(colorRipple.getValue());
        assertEquals(colorRipple.getValue(), mFloatingActionMenu.getMenuButtonColorRipple());
    }

}
