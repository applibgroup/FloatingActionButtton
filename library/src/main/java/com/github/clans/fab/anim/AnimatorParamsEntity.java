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

public class AnimatorParamsEntity {
    private Number mainValue;
    private Number mateValue;
    private boolean isFromAction;

    public AnimatorParamsEntity(Number mainValue) {
        this(mainValue, null, false);
    }

    public AnimatorParamsEntity(Number mainValue, Number mateValue, boolean isFromAction) {
        this.mainValue = mainValue;
        this.mateValue = mateValue;
        this.isFromAction = isFromAction;
    }

    public Number getMainValue() {
        return mainValue;
    }

    public void setMainValue(Number mainValue) {
        this.mainValue = mainValue;
    }

    public Number getMateValue() {
        return mateValue;
    }

    public void setMateValue(Number mateValue) {
        this.mateValue = mateValue;
    }

    public boolean isFromAction() {
        return isFromAction;
    }

    public void setFromAction(boolean moreParams) {
        isFromAction = moreParams;
    }
}
