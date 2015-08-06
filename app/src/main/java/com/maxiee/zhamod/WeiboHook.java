package com.maxiee.zhamod;

import android.content.res.XModuleResources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by maxiee on 15-8-4.
 */
public class WeiboHook implements IXposedHookLoadPackage, IXposedHookInitPackageResources, IXposedHookZygoteInit{
    private static String MODULE_PATH = null;
    private static final String PACKAGE_NAME = "com.sina.weibolite";
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

        final Class<?> homeListActivity = XposedHelpers.findClass("com.sina.weibo.HomeListActivity", loadPackageParam.classLoader);

        XposedHelpers.findAndHookMethod(
                homeListActivity,
                "onCreate",
                Bundle.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        LinearLayout titleBarLeft = (LinearLayout) XposedHelpers.getObjectField(param.thisObject, "h");
                        RelativeLayout titleBar = (RelativeLayout) titleBarLeft.getParent();
                        titleBar.setBackgroundResource(0);
                        titleBar.setBackgroundColor(Color.parseColor("#3F51B5"));
                    }
                }
        );

        final Class<?> statusModel = XposedHelpers.findClass("com.sina.weibo.models.Status", loadPackageParam.classLoader);

        XposedHelpers.findAndHookMethod(
                statusModel,
                "getText",
                new XC_MethodReplacement() {
                    @Override
                    protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                        return "天好热啊。。";
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

    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam initPackageResourcesParam) throws Throwable {
        if (!initPackageResourcesParam.packageName.equals(PACKAGE_NAME)) {
            return;
        }
        mModRes= XModuleResources.createInstance(MODULE_PATH, initPackageResourcesParam.res);
    }
}
