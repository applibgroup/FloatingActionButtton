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

package com.github.clans.fab.anim;

import ohos.agp.animation.Animator;
import ohos.agp.animation.AnimatorProperty;
import ohos.agp.components.Component;

public class AnimationFactory {

    public static AnimatorProperty genAnimatorByType(Component target, int animatorType, AnimatorParamsEntity animatorValue, int duration, int delay, Animator.StateChangedListener listener) {
        AnimatorProperty animatorProperty = new AnimatorProperty(target);
        animatorProperty.setCurveType(Animator.CurveType.LINEAR);
        switch (animatorType) {
            case AnimatorTypes.MOVE_BY_X:
                animatorProperty.moveByX(animatorValue.getMainValue().floatValue());
                break;
            case AnimatorTypes.MOVE_TO_X:
                if (animatorValue.isFromAction() && animatorValue.getMateValue() != null) {
                    animatorProperty.moveFromX(animatorValue.getMateValue().floatValue()).moveToX(animatorValue.getMainValue().floatValue());
                } else {
                    animatorProperty.moveToX(animatorValue.getMainValue().floatValue());
                }
                break;
            case AnimatorTypes.MOVE_FROM_X:
                animatorProperty.moveFromX(animatorValue.getMainValue().floatValue());
                break;
            case AnimatorTypes.MOVE_BY_Y:
                animatorProperty.moveByY(animatorValue.getMainValue().floatValue());
                break;
            case AnimatorTypes.MOVE_TO_Y:
                if (animatorValue.isFromAction() && animatorValue.getMateValue() != null) {
                    animatorProperty.moveFromY(animatorValue.getMateValue().floatValue()).moveToY(animatorValue.getMainValue().floatValue());
                } else {
                    animatorProperty.moveToY(animatorValue.getMainValue().floatValue());
                }
                break;
            case AnimatorTypes.MOVE_FROM_Y:
                animatorProperty.moveFromY(animatorValue.getMainValue().floatValue());
                break;
            case AnimatorTypes.ALPHA:
                animatorProperty.alpha(animatorValue.getMainValue().floatValue());
                break;
            case AnimatorTypes.ROTATE:
                animatorProperty.rotate(animatorValue.getMainValue().floatValue());
                break;
            case AnimatorTypes.SCALE_X:
                animatorProperty.scaleX(animatorValue.getMainValue().floatValue());
                break;
            case AnimatorTypes.SCALE_X_BY:
                animatorProperty.scaleXBy(animatorValue.getMainValue().floatValue());
                break;
            case AnimatorTypes.SCALE_Y:
                animatorProperty.scaleY(animatorValue.getMainValue().floatValue());
                break;
            case AnimatorTypes.SCALE_Y_BY:
                animatorProperty.scaleYBy(animatorValue.getMainValue().floatValue());
                break;
            default:
                break;
        }
        animatorProperty.setDuration(duration).setDelay(delay).setStateChangedListener(listener);
        return animatorProperty;
    }
}