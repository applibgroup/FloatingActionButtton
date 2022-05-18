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

import com.github.clans.fab.sample.ResourceTable;
import com.github.clans.fab.util.ResUtil;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.animation.Animator;
import ohos.agp.animation.AnimatorProperty;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.agp.components.element.ShapeElement;

public class MainAbilitySlice extends AbilitySlice implements Component.ClickedListener {
    private Text btn_horizontal_ntb, btn_vertical_ntb, btn_samples_ntb;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);

        ComponentContainer rootLayout = (ComponentContainer)
                LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_ability_main, null, false);
        rootLayout.setBackground(ResUtil.getShapeElement
                (this, ShapeElement.RECTANGLE, ResourceTable.Color_material_light_brown, 5.0f));
        btn_horizontal_ntb = (Text) rootLayout.findComponentById(ResourceTable.Id_btn_home);
        btn_vertical_ntb = (Text) rootLayout.findComponentById(ResourceTable.Id_btn_menus);
        btn_samples_ntb = (Text) rootLayout.findComponentById(ResourceTable.Id_btn_progress);

        btn_horizontal_ntb.setClickedListener(this);
        btn_vertical_ntb.setClickedListener(this);
        btn_samples_ntb.setClickedListener(this);

        setShapeElement();
        super.setUIContent(rootLayout);
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    @Override
    public void onClick(Component component) {
        AnimatorProperty animation = component.createAnimatorProperty();
        animation.scaleX(0.9f).scaleY(0.9f).setDuration(200).setCurveType(Animator.CurveType.CYCLE)
                .setStateChangedListener(new Animator.StateChangedListener() {
                    @Override
                    public void onStart(Animator animator) {
                        //TODO
                    }

                    @Override
                    public void onStop(Animator animator) {
                        //TODO
                    }

                    @Override
                    public void onCancel(Animator animator) {
                        //TODO
                    }

                    @Override
                    public void onEnd(Animator animator) {
                        Intent intent = new Intent();
                        intent.addFlags(Intent.FLAG_ABILITY_NEW_MISSION);
                        switch (component.getId()) {
                            case ResourceTable.Id_btn_home:
                                present(new HomeSlice(), intent);
                                break;
                            case ResourceTable.Id_btn_menus:
                                present(new MenuSlice(), intent);
                                break;
                            case ResourceTable.Id_btn_progress:
                                present(new ProgressSlice(), intent);
                                break;
                            default:
                                break;
                        }
                    }

                    @Override
                    public void onPause(Animator animator) {
                        //TODO
                    }

                    @Override
                    public void onResume(Animator animator) {
                        //TODO
                    }
                });

        animation.start();
    }

    private void setShapeElement() {
        btn_horizontal_ntb.setBackground(ResUtil.getShapeElement
                (this, ShapeElement.RECTANGLE, ResourceTable.Color_material_brown, 5.0f));
        btn_vertical_ntb.setBackground(ResUtil.getShapeElement
                (this, ShapeElement.RECTANGLE, ResourceTable.Color_material_brown, 5.0f));
        btn_samples_ntb.setBackground(ResUtil.getShapeElement
                (this, ShapeElement.RECTANGLE, ResourceTable.Color_material_brown, 5.0f));
    }
}
