package com.maxiee.zhamod;

import android.app.TabActivity;
import android.content.Context;
import android.content.res.XModuleResources;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TabHost;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by maxiee on 15-8-4.
 */
public class WeiboHook implements IXposedHookLoadPackage, IXposedHookInitPackageResources, IXposedHookZygoteInit{
    private static String MODULE_PATH = null;
    private static final String PACKAGE_NAME = "com.sina.weibo";
    private XModuleResources mModRes;
    private Class<?> mHomeListActivity;

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        MODULE_PATH = startupParam.modulePath;
    }

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (!loadPackageParam.packageName.equals(PACKAGE_NAME)) {
            return;
        }
        XposedBridge.log("主人,发现渣浪客户端!");

        final Class<?> baseLayout = XposedHelpers.findClass("com.sina.weibo.view.BaseLayout", loadPackageParam.classLoader);

        XposedHelpers.findAndHookMethod(
                baseLayout,
                "a",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        changeTitleBarBackground(param);
                    }
                }
        );

        XposedHelpers.findAndHookMethod(
                baseLayout,
                "a",
                Context.class,
                View.class,
                "boolean",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        changeTitleBarBackground(param);
                    }
                }
        );

        XposedHelpers.findAndHookMethod(
                "com.sina.weibo.MainTabActivity",
                loadPackageParam.classLoader,
                "onResume",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("抓到MainTabActivity的OnResume啦!");
                        TabActivity tabActivity = (TabActivity) param.thisObject;
                        TabHost tabHost = (TabHost) XposedHelpers.getObjectField(param.thisObject, "n");
                        tabHost.setCurrentTabByTag("mblog_tab");
                        // hook in hook 666
                        mHomeListActivity = XposedHelpers.findClass("com.sina.weibo.HomeListActivity", loadPackageParam.classLoader);
//                        mHomeListActivity = tabActivity.getCurrentActivity().getClass();
                        XposedHelpers.findAndHookMethod(
                                mHomeListActivity,
                                "onResume",
                                new XC_MethodHook() {
                                    @Override
                                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                        XposedBridge.log("钩住HomeListActivity的onResume啦!");
                                        // remove ad
                                        RelativeLayout WeiboBannerAd = (RelativeLayout) XposedHelpers.getObjectField(param.thisObject, "E");
                                        WeiboBannerAd.setVisibility(View.GONE);
                                    }
                                }
                        );
                    }
                }
        );
    }

    private void changeTitleBarBackground(XC_MethodHook.MethodHookParam param) {
        View titleBar = (View) XposedHelpers.getObjectField(param.thisObject, "g");
        titleBar.setBackgroundResource(0);
        XSharedPreferences sp = new XSharedPreferences(
                WeiboHook.class.getPackage().getName(),
                Constants.SP_FILE);
        sp.reload();
        int backgroundSelect = sp.getInt(Constants.SP_BACKGROUND, 0);
        titleBar.setBackgroundColor(mModRes.getColor(Constants.COLORS[backgroundSelect]));
    }

    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam initPackageResourcesParam) throws Throwable {
        if (!initPackageResourcesParam.packageName.equals(PACKAGE_NAME)) {
            return;
        }
        mModRes= XModuleResources.createInstance(MODULE_PATH, initPackageResourcesParam.res);
    }
}
