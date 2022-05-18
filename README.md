## FloatingActionButton

## Introduction
Yet another implementation of Floating Action Button for HMOS with lots of features.
>  

### Screenshots

<img src="/screenshots/main_screen.png" width=350 ></img> <img src="/screenshots/menu_closed.png" width=350 ></img> <img src="/screenshots/menu_default_opened.png" width=350 ></img>
<img src="/screenshots/menu_custom_opened.png" width=350 ></img> <img src="/screenshots/menu_mini_opened.png" width=350 ></img> <img src="/screenshots/menu_right_opened.png" width=350 ></img>
<img src="/screenshots/menu_down_opened.png" width=350 ></img> <img src="/screenshots/progress_background.png" width=350 ></img>

### Features

- Option to set custom **normal**/**pressed** colors
- Option to set custom animations
- Option to set custom icon drawable
- Support for **normal** `56dp` and **mini** `40dp` button sizes
- Custom FloatingActionMenu icon animations
- Option to expand menu up and down
- Option to show labels to the left and to the right of the menu
- Option to show circle progress on `FloactinActionButton`
- Option to add button to the `FloatingActionMenu` programmatically
- Option to dim the `FloatinActionMenu`'s background
- *Option to remove all buttons from the `FloatingActionMenu`*
- *Option to set a label for the `FloatingActionMenu`'s button*

### Below features are not supported now

- Ripple effect
- Option to set custom **ripple** color
- Option to set custom shadow color and offsets
- Option to disable shadow for buttons and (or) labels

## Usage Instructions

### Floating action Button

Add the `com.github.clans.fab.FloatingActionButton` to your layout XML file.
```XML
<StackLayout
    xmlns:ohos="http://schemas.huawei.com/res/ohos"
    ohos:id="$+id:parent"
    ohos:height="match_parent"
    ohos:width="match_parent">

    <ListContainer
        ohos:id="$+id:list"
        ohos:height="match_parent"
        ohos:width="match_parent"/>

    <com.github.clans.fab.FloatingActionButton
        ohos:id="$+id:fabBtn"
        ohos:height="match_content"
        ohos:width="match_content"
        ohos:bottom_margin="12vp"
        ohos:fab_elevation="4vp"
        ohos:image_src="$media:ic_menu"
        ohos:layout_alignment="bottom|right"
        ohos:right_margin="16vp"/>

</StackLayout>
```

### Floating action menu

```XML
 <com.github.clans.fab.FloatingActionMenu
        ohos:id="$+id:menu_green"
        ohos:height="match_content"
        ohos:width="match_content"
        ohos:align_parent_bottom="true"
        ohos:align_parent_right="true"
        ohos:bottom_margin="10vp"
        ohos:left_margin="10vp"
        ohos:menu_animationDelayPerItem="500"
        ohos:menu_colorNormal="#43A047"
        ohos:menu_colorPressed="#2E7D32"
        ohos:menu_icon="$media:ic_star"
        ohos:right_margin="150vp"
        ohos:top_margin="10vp"
        >

        <com.github.clans.fab.FloatingActionButton
            ohos:height="match_content"
            ohos:width="match_content"
            ohos:fab_colorNormal="#43A047"
            ohos:fab_colorPressed="#2E7D32"
            ohos:fab_label="Menu item 1"
            ohos:fab_size="1"
            ohos:image_src="$media:ic_edit"/>


        <com.github.clans.fab.FloatingActionButton
            ohos:height="match_content"
            ohos:width="match_content"
            ohos:fab_colorNormal="#43A047"
            ohos:fab_colorPressed="#2E7D32"
            ohos:fab_label="Menu item 2"
            ohos:fab_size="1"
            ohos:image_src="$media:ic_edit"/>

        <com.github.clans.fab.FloatingActionButton
            ohos:height="match_content"
            ohos:width="match_content"
            ohos:fab_colorNormal="#43A047"
            ohos:fab_colorPressed="#2E7D32"
            ohos:fab_label="lorem_ipsum"
            ohos:fab_size="1"
            ohos:image_src="$media:ic_edit"/>
    </com.github.clans.fab.FloatingActionMenu>
```
## Installation Instructions

```
Method 1: Generate har package from library and add it to lib folder.
       add following code to gradle of entry
       implementation fileTree(dir: 'libs', include: ['*.jar', '*.har'])
Method 2:
    allprojects{
        repositories{
            mavenCentral()
        }
    }
    implementation 'io.openharmony.tpc.thirdlib:floatingactionbutton:1.0.0'

```

## License

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
