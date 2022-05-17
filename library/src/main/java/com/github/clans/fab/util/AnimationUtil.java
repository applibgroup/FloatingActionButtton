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

package com.github.clans.fab.util;

import com.github.clans.fab.anim.AnimatorTypes;
import com.github.clans.fab.anim.XAnimatorValue;
import ohos.agp.animation.AnimatorGroup;
import ohos.agp.components.Component;

public class AnimationUtil {

    public static AnimatorGroup loadAnimation(String animationType, Component target, long duration) {
        AnimatorGroup animatorGroup = new AnimatorGroup();
        switch (AnimationType.valueOf(animationType.toUpperCase())) {
            case JUMP_FROM_DOWN:
                animatorGroup.runParallel(target.createAnimatorProperty().
                        moveFromY(target.getContentPositionY()).moveToY(target.getContentPositionY() + 1000).setDuration(duration));

                break;
            case JUMP_TO_DOWN:
                animatorGroup.runParallel(target.createAnimatorProperty().
                        moveFromY(target.getContentPositionY()).moveToY(target.getContentPositionY() - 1000).setDuration(duration));
                break;
            case FAB_DEFAULT_SHOW:
                animatorGroup.runParallel(
                        XAnimatorValue.ofFloat(target, AnimatorTypes.SCALE_X, duration, 0.0f, 1.0f),
                        XAnimatorValue.ofFloat(target, AnimatorTypes.SCALE_Y, duration, 0.0f, 1.0f)
                );
                break;
            case FAB_DEFAULT_HIDE:
                animatorGroup.runParallel(
                        XAnimatorValue.ofFloat(target, AnimatorTypes.SCALE_X, duration, 1.0f, 0.0f),
                        XAnimatorValue.ofFloat(target, AnimatorTypes.SCALE_Y, duration, 1.0f, 0.0f)
                );
                break;
            case MENU_BUTTON_SHOW:
                animatorGroup.runParallel(
                        XAnimatorValue.ofFloat(target, AnimatorTypes.ALPHA, duration, 0, 1)
                );
                break;
            case MENU_BUTTON_HIDE:
                animatorGroup.runParallel(
                        XAnimatorValue.ofFloat(target, AnimatorTypes.ALPHA, duration, 1, 0)
                );
                break;
        }

        return animatorGroup;
    }

    enum AnimationType {
        JUMP_FROM_DOWN,
        JUMP_TO_DOWN,
        MENU_BUTTON_SHOW,
        MENU_BUTTON_HIDE,
        FAB_DEFAULT_SHOW,
        FAB_DEFAULT_HIDE
    }
}
