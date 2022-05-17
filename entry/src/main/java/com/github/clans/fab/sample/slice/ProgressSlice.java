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
import com.github.clans.fab.sample.ResourceTable;
import com.github.clans.fab.util.ResUtil;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.BaseItemProvider;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.ListContainer;
import ohos.agp.components.StackLayout;
import ohos.agp.utils.Color;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class ProgressSlice extends AbilitySlice {
    private StackLayout mRootId;
    private ListContainer list_component;
    private FloatingActionButton mFab;
    private LinkedList<ProgressType> mProgressTypes;
    private int mMaxProgress = 100;
    private int mPreviousVisibleItem;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        ComponentContainer root_layout = (ComponentContainer)
                LayoutScatter.getInstance(getContext()).parse(com.github.clans.fab.sample.ResourceTable.Layout_slice_progress, null, false);
        mRootId = (StackLayout) root_layout.findComponentById(ResourceTable.Id_parent);
        mRootId.setBackground(ResUtil.buildDrawableByColor((Color.DKGRAY).getValue()));
        list_component = (ListContainer) mRootId.findComponentById(ResourceTable.Id_list);
        mFab = (FloatingActionButton) mRootId.findComponentById(ResourceTable.Id_fabBtn);
        populateListContainer();
        super.setUIContent(root_layout);
    }

    @Override
    protected void onForeground(Intent intent) {
        super.onForeground(intent);
        populateListContainer();
    }

    private void populateListContainer() {
        Locale[] availableLocales = Locale.getAvailableLocales();
        mProgressTypes = new LinkedList<>();
        for (ProgressType type : ProgressType.values()) {
            mProgressTypes.offer(type);
        }
        List<String> locales = new ArrayList<>();
        for (Locale locale : availableLocales) {
            locales.add(locale.getDisplayName());
        }
        list_component.setItemProvider(new MainListProvider(locales.toArray(new String[0])));
        list_component.setScrolledListener((component, i, i1, i2, i3) -> {
            int firstVisibleItem = list_component.getItemPosByVisibleIndex(0);
            if (firstVisibleItem > mPreviousVisibleItem) {
                mFab.hide(true);
            } else if (firstVisibleItem < mPreviousVisibleItem) {
                mFab.show(true);
            }
            mPreviousVisibleItem = firstVisibleItem;
        });
        getUITaskDispatcher().delayDispatch(() -> {
            mFab.show(true);
            mFab.setMax(mMaxProgress);
        }, 300);
        mFab.setClickedListener(component -> {
            ProgressType type = mProgressTypes.poll();
            switch (type) {
                case INDETERMINATE:
                    mFab.setShowProgressBackground(true);
                    mFab.setIndeterminate(true);
                    mProgressTypes.offer(ProgressType.INDETERMINATE);
                    break;
                case PROGRESS_POSITIVE:
                    mFab.setIndeterminate(false);
                    mFab.setProgress(70, true);
                    mProgressTypes.offer(ProgressType.PROGRESS_POSITIVE);
                    break;
                case PROGRESS_NEGATIVE:
                    mFab.setProgress(30, true);
                    mProgressTypes.offer(ProgressType.PROGRESS_NEGATIVE);
                    break;
               /* case HIDDEN:
                    mFab.hideProgress();
                    mProgressTypes.offer(ProgressType.HIDDEN);
                    break;*/
                case PROGRESS_NO_ANIMATION:
                    increaseProgress(mFab, 0);
                    break;
                case PROGRESS_NO_BACKGROUND:
                    mFab.setShowProgressBackground(false);
                    mFab.setIndeterminate(true);
                    mProgressTypes.offer(ProgressType.PROGRESS_NO_BACKGROUND);
                    break;
            }
        });
    }

    private void increaseProgress(final FloatingActionButton fab, int i) {
        if (i <= mMaxProgress) {
            fab.setProgress(i, false);
            final int progress = ++i;
            getUITaskDispatcher().delayDispatch(() -> increaseProgress(fab, progress), 30);
        } else {
            getUITaskDispatcher().delayDispatch(new Runnable() {
                @Override
                public void run() {
                    fab.hideProgress();
                }
            }, 200);
            mProgressTypes.offer(ProgressType.PROGRESS_NO_ANIMATION);
        }
    }

    public class MainListProvider extends BaseItemProvider {
        String[] list_items;

        MainListProvider(String[] lst) {
            list_items = lst;
        }

        @Override
        public int getCount() {
            return list_items.length;
        }

        @Override
        public Object getItem(int i) {
            return list_items[i];
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public Component getComponent(int i, Component component, ComponentContainer componentContainer) {
            if (component == null) {
                component = LayoutScatter.getInstance(getContext()).
                        parse(ResourceTable.Layout_list_item, componentContainer, false);
            }
            ((Button) (component.findComponentById(ResourceTable.Id_list_component))).setText((String) getItem(i));
            component.setClickable(false);
            return component;
        }
    }

    private enum ProgressType {
        INDETERMINATE, PROGRESS_POSITIVE, PROGRESS_NEGATIVE, /*HIDDEN,*/ PROGRESS_NO_ANIMATION, PROGRESS_NO_BACKGROUND
    }
}
