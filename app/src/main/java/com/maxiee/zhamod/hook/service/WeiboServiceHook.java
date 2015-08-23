package com.maxiee.zhamod.hook.service;

import android.content.Intent;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by maxiee on 15-8-24.
 */
public class WeiboServiceHook {
    private static final String TAG = "[WeiboService]";

    public static void init(final XC_LoadPackage.LoadPackageParam lpp) {
        Class<?> weiboService = XposedHelpers.findClass(
                "com.sina.weibo.business.WeiboService",
                lpp.classLoader);
        onBindHook(weiboService);
        onStartHook(weiboService);
        staticaHook(weiboService);
    }

    private static void onBindHook(Class<?> weiboService) {
        XposedHelpers.findAndHookMethod(
                weiboService,
                "onBind",
                Intent.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        Intent i = (Intent) param.args[0];
                        XposedBridge.log(TAG + "onBind:" + i.getAction());
                    }
                });
    }

    private static void onStartHook(Class<?> weiboService) {
        XposedHelpers.findAndHookMethod(
                weiboService,
                "onStart",
                Intent.class,
                "int",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        Intent i = (Intent) param.args[0];
                        XposedBridge.log(TAG + "onStart:" + i.getAction());
                    }
                }
        );
    }

    private static void staticaHook(Class<?> weiboService) {
        XposedHelpers.findAndHookMethod(
                weiboService,
                "a",
                String.class,
                "com.sina.weibo.business.al",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        String action = (String) param.args[0];
                        Object al = param.args[1];
                        XposedBridge.log(TAG + "a:" + action);
                        XposedBridge.log(TAG + "a:" + al.toString());
                    }
                }
        );
    }
}
