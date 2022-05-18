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

package com.github.clans.fab.sample.slice;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.github.clans.fab.anim.AnimatorTypes;
import com.github.clans.fab.anim.XAnimatorValue;
import com.github.clans.fab.sample.ResourceTable;
import com.github.clans.fab.util.ResUtil;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.animation.Animator;
import ohos.agp.animation.AnimatorGroup;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.DependentLayout;
import ohos.agp.components.LayoutScatter;

import java.util.ArrayList;
import java.util.List;

public class MenuSlice extends AbilitySlice {
    private FloatingActionMenu menuRed;
    private FloatingActionMenu menuYellow;
    private FloatingActionMenu menuGreen;
    private FloatingActionMenu menuBlue;
    private FloatingActionMenu menuDown;
    private FloatingActionMenu menuLabelsRight;
    private List<FloatingActionMenu> menus = new ArrayList<>();

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        ComponentContainer root_layout = (ComponentContainer)
                LayoutScatter.getInstance(getContext()).parse(com.github.clans.fab.sample.ResourceTable.Layout_slice_menu, null, false);
        DependentLayout mRootId = (DependentLayout) root_layout.findComponentById(ResourceTable.Id_parent);
        menuRed = (FloatingActionMenu) root_layout.findComponentById(ResourceTable.Id_menu_red);
        menuYellow = (FloatingActionMenu) root_layout.findComponentById(ResourceTable.Id_menu_yellow);
        menuGreen = (FloatingActionMenu) root_layout.findComponentById(ResourceTable.Id_menu_green);
        menuDown = (FloatingActionMenu) root_layout.findComponentById(ResourceTable.Id_menu_down);
        menuBlue = (FloatingActionMenu) root_layout.findComponentById(ResourceTable.Id_menu_blue);
        menuLabelsRight = (FloatingActionMenu) root_layout.findComponentById(ResourceTable.Id_menu_labels_right);
        super.setUIContent(root_layout);
    }

    @Override
    protected void onActive() {
        super.onActive();

        menuDown.hideMenuButton(false);
        menuRed.hideMenuButton(false);
        menuYellow.hideMenuButton(false);
        menuGreen.hideMenuButton(false);
        menuBlue.hideMenuButton(false);
        menuLabelsRight.hideMenuButton(false);

        menus.add(menuDown);
        menus.add(menuRed);
        menus.add(menuYellow);
        menus.add(menuGreen);
        menus.add(menuBlue);
        menus.add(menuLabelsRight);

        menuYellow.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                String text;
                if (opened) {
                    text = "Menu opened";
                } else {
                    text = "Menu closed";
                }
                ResUtil.showToast(getContext(), text, 200);
            }
        });

        int delay = 400;
        for (final FloatingActionMenu menu : menus) {
            getUITaskDispatcher().delayDispatch(new Runnable() {
                @Override
                public void run() {
                    menu.showMenuButton(true);
                }
            }, delay);
            delay += 150;
        }

        final FloatingActionButton programFab1 = new FloatingActionButton(this);
        programFab1.setButtonSize(FloatingActionButton.SIZE_MINI);
        programFab1.setLabelText("Programmatically added button");
        programFab1.setImageElement(ResUtil.getPixelMapDrawable(this, ResourceTable.Media_ic_edit));
        menuRed.addMenuButton(programFab1);

        createCustomAnimation();

    }


    private void createCustomAnimation() {
        AnimatorGroup firstSet = new AnimatorGroup();
        firstSet.runParallel(
                XAnimatorValue.ofFloat(menuGreen.getMenuIconView(), AnimatorTypes.SCALE_X, 50, 1.0f, 0.2f),
                XAnimatorValue.ofFloat(menuGreen.getMenuIconView(), AnimatorTypes.SCALE_Y, 50, 1.0f, 0.2f)
        );

        AnimatorGroup secondSet = new AnimatorGroup();
        secondSet.runParallel(
                XAnimatorValue.ofFloat(menuGreen.getMenuIconView(), AnimatorTypes.SCALE_X, 150, 0.2f, 1.0f),
                XAnimatorValue.ofFloat(menuGreen.getMenuIconView(), AnimatorTypes.SCALE_X, 150, 0.2f, 1.0f)
        );

        AnimatorGroup finalSet = new AnimatorGroup();
        finalSet.runSerially(firstSet, secondSet);
        finalSet.setCurveType(Animator.CurveType.OVERSHOOT);

    }
}
