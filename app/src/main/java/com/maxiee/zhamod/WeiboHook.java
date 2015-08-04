package com.maxiee.zhamod;

import android.view.View;
import android.widget.TextView;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by maxiee on 15-8-4.
 */
public class WeiboHook implements IXposedHookLoadPackage{
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (!loadPackageParam.packageName.equals("com.sina.weibo")) {
            return;
        }
        XposedBridge.log("主人,发现渣浪客户端!");

        final Class<?> homeActivity = XposedHelpers.findClass("com.sina.weibo.view.TabView", loadPackageParam.classLoader);

        XposedHelpers.findAndHookMethod(
                homeActivity,
                "setText",
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        String text = (String) XposedHelpers.getObjectField(param.thisObject, "f");
                        XposedBridge.log(text);
                        XposedHelpers.setObjectField(param.thisObject, "f", "渣");
                        ((View) param.thisObject).invalidate();
                    }
                });
    }
}
