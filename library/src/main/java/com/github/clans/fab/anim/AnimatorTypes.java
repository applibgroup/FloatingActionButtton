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

public class AnimatorTypes {

    public static final int MOVE_BY_X = 1;
    public static final int MOVE_TO_X = 2;
    public static final int MOVE_FROM_X = 3;
    public static final int MOVE_BY_Y = 4;
    public static final int MOVE_TO_Y = 5;
    public static final int MOVE_FROM_Y = 6;
    public static final int ALPHA = 7;
    public static final int ROTATE = 8;
    public static final int SCALE_X = 9;
    public static final int SCALE_X_BY = 10;
    public static final int SCALE_Y = 11;
    public static final int SCALE_Y_BY = 12;

    /**
     * Supported Animation check
     *
     * @param animatorType check for suppoted animation type
     * @return true when animation type supported
     */
    public static boolean checkAnimatorType(int animatorType) {
        switch (animatorType) {
            case MOVE_BY_X:
            case MOVE_TO_X:
            case MOVE_FROM_X:
            case MOVE_BY_Y:
            case MOVE_TO_Y:
            case MOVE_FROM_Y:
            case ALPHA:
            case ROTATE:
            case SCALE_X:
            case SCALE_X_BY:
            case SCALE_Y:
            case SCALE_Y_BY:
                return true;
            default:
                return false;
        }
    }
}
