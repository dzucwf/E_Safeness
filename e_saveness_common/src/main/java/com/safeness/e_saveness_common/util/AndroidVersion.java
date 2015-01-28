package com.safeness.e_saveness_common.util;

/**
 * Created by EISAVISA on 2015/1/26.
 *
 * 安卓运行版本工具类
 */
public class AndroidVersion {

    public static final int SDK_VERSION_ECLAIR = 5;
    public static final int SDK_VERSION_DONUT = 4;
    public static final int SDK_VERSION_CUPCAKE = 3;
    public static boolean PRE_CUPCAKE =
            getSDKVersionNumber() < SDK_VERSION_CUPCAKE ? true : false;
    public static int getSDKVersionNumber() {
        int sdkVersion;
        try {
            sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {
            sdkVersion = 0;
        }
        return sdkVersion;
    }
}
