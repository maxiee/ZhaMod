package com.maxiee.zhamod;

import android.content.Context;
import android.content.res.XModuleResources;
import android.view.View;

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

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        MODULE_PATH = startupParam.modulePath;
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
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

//        final Class<?> tabView = XposedHelpers.findClass("com.sina.weibo.view.TabView", loadPackageParam.classLoader);
//
//        XposedHelpers.findAndHookMethod(
//                tabView,
//                "setText",
//                String.class,
//                new XC_MethodHook() {
//                    @Override
//                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                    }
//
//                    @Override
//                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        String text = (String) XposedHelpers.getObjectField(param.thisObject, "f");
//                        XposedBridge.log(text);
//                        XposedHelpers.setObjectField(param.thisObject, "f", "渣");
//                        ((View) param.thisObject).invalidate();
//                    }
//                });

//        final Class<?> mainTabActivity = XposedHelpers.findClass("com.sina.weibo.MainTabActivity", loadPackageParam.classLoader);
//
//        XposedHelpers.findAndHookMethod(
//                mainTabActivity,
//                "q",
//                new XC_MethodHook() {
//                    @Override
//                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        RelativeLayout relativeLayout = (RelativeLayout) XposedHelpers.getObjectField(param.thisObject, "U");
//                        relativeLayout.setVisibility(View.VISIBLE);
//                        ImageView oldComposeImage = (ImageView) relativeLayout.getChildAt(0);
//                        oldComposeImage.setVisibility(View.GONE);
//                        ImageView composeImage = new ImageView(relativeLayout.getContext());
//                        composeImage.setImageDrawable(mModRes.getDrawable(R.drawable.bei));
//                        composeImage.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//                        composeImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//                        relativeLayout.addView(composeImage);
//                    }
//                }
//        );
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
