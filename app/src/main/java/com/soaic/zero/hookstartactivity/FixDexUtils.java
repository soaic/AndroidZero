package com.soaic.zero.hookstartactivity;

import android.content.Context;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexClassLoader;

/**
 * 热修复-代码修复
 * 把dex放到 odex 文件夹下
 */
public class FixDexUtils {

    public static void fixDex(Context context) {

        try {
            // 1. 获取 application ClassLoader
            ClassLoader applicationClassLoader = context.getClassLoader();
            // 2. 从 classLoader中获取 pathList 下的 dexElements
            Object dexElements = getDexElementsByClassLoader(applicationClassLoader);

            // 3. dex 文件存放的目录
            File dexFile = context.getExternalFilesDir("odex");
            if (!dexFile.exists()) {
                dexFile.mkdirs();
            }

            // 3. 1 dex解压路径
            File dexCache = context.getExternalFilesDir("odexCache");
            if (!dexCache.exists()) {
                dexCache.mkdirs();
            }
            File[] files = dexFile.listFiles();

            if (files != null) {
                ClassLoader dexClassLoader;
                Object fileDexElements;
                for (File file : files) {
                    // 只处理 .dex 后缀结尾
                    if (file.getAbsolutePath().lastIndexOf(".dex") > 0) {
                        // 4. 通过 Dex File 生成 DexClassLoader
                        dexClassLoader = new DexClassLoader(
                                file.getAbsolutePath(), // dex 文件路径
                                dexCache.getAbsolutePath(), // dex 解压路径
                                null,  // so 库
                                applicationClassLoader); // 父 ClassLoader
                        // 5. 获取生成的 DexClassLoader 中的 dexElements
                        fileDexElements = getDexElementsByClassLoader(dexClassLoader);
                        // 6. 把 fileDexElements 与 dexElements 合并，并放在最前面
                        dexElements = combineArray(fileDexElements, dexElements);
                    }
                }

                // 7. 修复 dex, 把合并后的dex注入到 applicationClassLoader 中
                injectDexElements(applicationClassLoader, dexElements);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 把dexElement 注入到 ClassLoader
     * @param classLoader
     * @param dexElements
     */
    private static void injectDexElements(ClassLoader classLoader, Object dexElements) throws Exception {
        // 1. 先获取 pathList 属性
        Field pathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
        // 2. 设置私有可用
        pathListField.setAccessible(true);
        // 3. 从 classLoader 中拿到 pathList 值
        Object pathList = pathListField.get(classLoader);
        // 4. 获取 dexElements 属性
        Field dexElementsField = pathList.getClass().getDeclaredField("dexElements");
        dexElementsField.setAccessible(true);
        // 5. 把 dexElements 注入到 pathList 中
        dexElementsField.set(pathList, dexElements);
    }

    /**
     * 通过 ClassLoader 获取 dexElements
     * @param classLoader
     * @return
     * @throws Exception
     */
    private static Object getDexElementsByClassLoader(ClassLoader classLoader) throws Exception {
        // 1. 先获取 pathList 属性
        Field pathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
        // 2. 设置私有可用
        pathListField.setAccessible(true);
        // 3. 从 classLoader 中拿到 pathList 值
        Object pathList = pathListField.get(classLoader);
        // 4. 获取 dexElements 属性
        Field dexElementsField = pathList.getClass().getDeclaredField("dexElements");
        dexElementsField.setAccessible(true);
        // 5. 从 pathList 中获取 dexElements 数据
        return dexElementsField.get(pathList);
    }


    /**
     * 合并数组
     * @param arrayLhs 合并靠左的数据
     * @param arrayRhs 合并靠右的数据
     * @return 合并后的数据
     */
    private static Object combineArray(Object arrayLhs, Object arrayRhs) {
        // 获取数据类型
        Class<?> localClass = arrayLhs.getClass().getComponentType();
        int i = Array.getLength(arrayLhs);
        int j = i + Array.getLength(arrayRhs);

        Object result = Array.newInstance(localClass, j);
        for (int k = 0; k < j; ++k) {
            if (k < i) {
                Array.set(result, k, Array.get(arrayLhs, k));
            } else {
                Array.set(result, k, Array.get(arrayRhs, k - i));
            }
        }
        return result;
    }

}
