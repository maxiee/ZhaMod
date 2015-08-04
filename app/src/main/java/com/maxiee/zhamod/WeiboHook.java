package com.maxiee.zhamod;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
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
    }
}
