package com.maxiee.zhamod.hook.service;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by maxiee on 15-8-25.
 */
public class PullDownHook {
    private static final String TAG = "[PullDown]";
    private static final String CLASS = "com.sina.weibo.view.PullDownView";

    public static void init(final XC_LoadPackage.LoadPackageParam lpp) {
        Class<?> pullDown = XposedHelpers.findClass(CLASS, lpp.classLoader);
        setUpdateHandleHook(pullDown);
    }

    private static void setUpdateHandleHook(Class<?> pullDown) {
        XposedHelpers.findAndHookMethod(
                pullDown,
                "setUpdateHandle",
                "com.sina.weibo.view.PullDownView$b",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        Object handle = param.args[0];
                        XposedBridge.log(TAG + "setUpdateHandle:" + handle.getClass().toString());
                    }
                }
        );
    }
}
