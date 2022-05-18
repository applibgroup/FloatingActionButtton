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

import ohos.agp.colors.RgbColor;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Text;
import ohos.agp.components.element.Element;
import ohos.agp.components.element.PixelMapElement;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.text.Font;
import ohos.agp.utils.Color;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.utils.TextAlignment;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;
import ohos.global.resource.NotExistException;
import ohos.global.resource.RawFileEntry;
import ohos.global.resource.Resource;
import ohos.global.resource.ResourceManager;
import ohos.global.resource.WrongTypeException;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;

import java.io.*;
import java.util.Optional;

public class ResUtil {
    private static final String TAG = ResUtil.class.getSimpleName();
    private static final int BUFFER_LENGTH = 8192;
    public static final int DEFAULT_ERROR = -1;
    private static final String RAW_FILE_PATH = "entry/resources/rawfile/";

    private ResUtil() {
    }

    /**
     * get the path from id
     *
     * @param context the context
     * @param id      the id
     * @return the path from id
     */
    public static String getPathById(Context context, int id) {

        String path = "";
        if (context == null) {
            return path;
        }
        ResourceManager manager = context.getResourceManager();
        if (manager == null) {
            return path;
        }
        try {
            path = manager.getMediaPath(id);
        } catch (IOException e) {
            LogUtil.error(TAG, "getPathById -> IOException");
        } catch (NotExistException e) {
            LogUtil.error(TAG, "getPathById -> NotExistException");
        } catch (WrongTypeException e) {
            LogUtil.error(TAG, "getPathById -> WrongTypeException");
        }
        return path;
    }

    /**
     * get the color
     *
     * @param context the context
     * @param id      the id
     * @return the color
     */
    public static int getColor(Context context, int id) {
        int result = 0;
        if (context == null) {
            return result;
        }
        ResourceManager manager = context.getResourceManager();
        if (manager == null) {
            return result;
        }
        try {
            result = manager.getElement(id).getColor();
        } catch (IOException e) {
            LogUtil.error(TAG, "getColor -> IOException");
        } catch (NotExistException e) {
            LogUtil.error(TAG, "getColor -> NotExistException");
        } catch (WrongTypeException e) {
            LogUtil.error(TAG, "getColor -> WrongTypeException");
        }
        return result;
    }

    /**
     * get the dimen value
     *
     * @param context the context
     * @param id      the id
     * @return get the float dimen value
     */
    public static float getDimen(Context context, int id) {
        float result = 0;
        if (context == null) {
            return result;
        }
        ResourceManager manager = context.getResourceManager();
        if (manager == null) {
            return result;
        }
        try {
            result = manager.getElement(id).getFloat();
        } catch (IOException e) {
            LogUtil.error(TAG, "getDimen -> IOException");
        } catch (NotExistException e) {
            LogUtil.error(TAG, "getDimen -> NotExistException");
        } catch (WrongTypeException e) {
            LogUtil.error(TAG, "getDimen -> WrongTypeException");
        }
        return result;
    }

    /**
     * get the dimen value
     *
     * @param context the context
     * @param id      the id
     * @return get the int dimen value
     */
    public static int getIntDimen(Context context, int id) {
        float value = getDimen(context, id);
        return (int) (value >= 0 ? value + Const.FLOAT_HALF : value - Const.FLOAT_HALF);
    }

    public static final ShapeElement getShapeElement(Context context, int shape, int color, float radius) {
        ShapeElement shapeElement = new ShapeElement();
        shapeElement.setShape(shape);
        shapeElement.setRgbColor(RgbColor.fromArgbInt(getColor(context, color)));
        shapeElement.setCornerRadius(radius);
        return shapeElement;
    }

    /**
     * get the pixel map
     *
     * @param context the context
     * @param id      the id
     * @return the pixel map
     */
    public static Optional<PixelMap> getPixelMap(Context context, int id) {
        String path = getPathById(context, id);
        if (path.isEmpty()) {
            return Optional.empty();
        }
        RawFileEntry assetManager = context.getResourceManager().getRawFileEntry(path);
        ImageSource.SourceOptions options = new ImageSource.SourceOptions();
        options.formatHint = "image/png";
        ImageSource.DecodingOptions decodingOptions = new ImageSource.DecodingOptions();
        try {
            Resource asset = assetManager.openRawFile();
            ImageSource source = ImageSource.create(asset, options);
            return Optional.ofNullable(source.createPixelmap(decodingOptions));
        } catch (IOException e) {
            LogUtil.error(TAG, "getPixelMap -> IOException");
        }
        return Optional.empty();
    }

    /**
     * get the Pixel Map Element
     *
     * @param context the context
     * @param resId   the res id
     * @return the Pixel Map Element
     */
    public static PixelMapElement getPixelMapDrawable(Context context, int resId) {
        Optional<PixelMap> optional = getPixelMap(context, resId);
        return optional.map(PixelMapElement::new).orElse(null);
    }

    /**
     * get the Element
     *
     * @param color the color
     * @return the Element
     */
    public static Element buildDrawableByColor(int color) {
        ShapeElement drawable = new ShapeElement();
        drawable.setShape(ShapeElement.RECTANGLE);
        drawable.setRgbColor(RgbColor.fromArgbInt(color));
        return drawable;
    }

    public static Font createFont(Context context, String fontFamily) {
        //String fontFamily = getString(context, fontId);
        String path = RAW_FILE_PATH + fontFamily;
        File file = new File(context.getDataDir(), fontFamily);
        try (OutputStream outputStream = new FileOutputStream(file);
             InputStream inputStream = context.getResourceManager().getRawFileEntry(path).openRawFile()) {
            byte[] buffer = new byte[BUFFER_LENGTH];
            int bytesRead = inputStream.read(buffer, 0, BUFFER_LENGTH);
            while (bytesRead != DEFAULT_ERROR) {
                outputStream.write(buffer, 0, bytesRead);
                bytesRead = inputStream.read(buffer, 0, BUFFER_LENGTH);
            }
        } catch (FileNotFoundException exception) {
            LogUtil.error(TAG, "loadFontFromFile -> FileNotFoundException : " + exception.getLocalizedMessage());
        } catch (IOException exception) {
            LogUtil.error(TAG, "loadFontFromFile -> IOException : " + exception.getLocalizedMessage());
        }
        return Optional.of(new Font.Builder(file).setWeight(Font.REGULAR).build()).get();
    }

    public static void showToast(Context abilityContext, String str, int duration) {
        Text text = new Text(abilityContext);
        text.setWidth(ComponentContainer.LayoutConfig.MATCH_PARENT);
        text.setHeight(ComponentContainer.LayoutConfig.MATCH_CONTENT);
        text.setTextSize(48);
        text.setText(str);
        text.setMultipleLine(true);
        text.setTextAlignment(TextAlignment.CENTER);
        ShapeElement shapeElement = (ShapeElement) buildDrawableByColor(Color.WHITE.getValue());
        text.setBackground(shapeElement);
        DirectionalLayout directionalLayout = new DirectionalLayout(abilityContext);
        directionalLayout.setBackground(shapeElement);
        DirectionalLayout.LayoutConfig layoutConfig = new DirectionalLayout.LayoutConfig
                (DirectionalLayout.LayoutConfig.MATCH_PARENT, DirectionalLayout.LayoutConfig.MATCH_CONTENT);
        layoutConfig.setMarginBottom(100);
        directionalLayout.setLayoutConfig(layoutConfig);
        directionalLayout.setPadding(20, 30, 20, 30);
        directionalLayout.addComponent(text);
        ToastDialog toastDialog = new ToastDialog(abilityContext);
        toastDialog.setComponent(directionalLayout).setDuration(duration).setAutoClosable(true)
                .setAlignment(LayoutAlignment.BOTTOM).setTransparent(true).show();
    }
}

