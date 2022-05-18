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
import ohos.agp.animation.AnimatorValue;
import ohos.agp.components.Component;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class XAnimatorValue extends AnimatorValue implements Animator.StateChangedListener {

    private WeakReference<Component> targetComponent;
    private ArrayList<AnimatorParamsEntity> valueSet;
    private int mAnimatorType = -1;
    // Animation unit time
    private int unitTime = 0;
    private AnimatorProperty runningAnimator;
    private int runningIndex = 0;

    private XAnimatorValue(Component component, int animatorType, long duration, List<AnimatorParamsEntity> values) throws Exception {
        if (!AnimatorTypes.checkAnimatorType(animatorType)) {
            throw new Exception("Enter a valid animatorType.");
        }
        if (duration <= 0) {
            throw new Exception("The duration must be greater than 0.");
        }
        unitTime = Math.round(duration / values.size());
        System.out.println(unitTime);
        targetComponent = new WeakReference<>(component);
        mAnimatorType = animatorType;
        valueSet = new ArrayList<>(values);
        setDuration(duration);
        setStateChangedListener(this);
    }

    /**
     * Initialize animation collection
     */
    private void configAnimator() {
        if (runningIndex < valueSet.size()) {
            runningAnimator = AnimationFactory.genAnimatorByType(targetComponent.get(), mAnimatorType, valueSet.get(runningIndex), unitTime, 0, this);
            if (runningAnimator != null) {
                runningAnimator.start();
            }
        }
    }

    public Component getTargetView() {
        return this.targetComponent != null ? this.targetComponent.get() : null;
    }

    public static XAnimatorValue ofFloat(Component targetComponent, int animatorType, long duration, Number... values) {
        try {
            List<Number> tempList = Arrays.asList(values);
            ArrayList<AnimatorParamsEntity> targetList = new ArrayList<>();
            for (int i = 0; i < tempList.size(); i++) {
                targetList.add(new AnimatorParamsEntity(tempList.get(i)));
            }
            return new XAnimatorValue(targetComponent, animatorType, duration, targetList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static XAnimatorValue ofFloat(Component targetComponent, int animatorType, long duration, AnimatorParamsEntity... values) {
        try {
            return new XAnimatorValue(targetComponent, animatorType, duration, Arrays.asList(values));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void start() {
        configAnimator();
    }

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
        runningIndex++;
        configAnimator();
    }

    @Override
    public void onPause(Animator animator) {
        //TODO
    }

    @Override
    public void onResume(Animator animator) {
        //TODO
    }
}