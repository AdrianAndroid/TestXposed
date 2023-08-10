package com.kika.testxposed;

import android.util.Log;
import android.webkit.WebView;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookH5Plugin extends HookImpl{
    @Override
    public boolean isThisPackageName(XC_LoadPackage.LoadPackageParam lpparam) {
        return true;
    }

    @Override
    public String packageName() {
        return null;
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        // 强制H5调试插件开发
        XposedBridge.hookAllConstructors(
            WebView.class,
            new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    // 主动调用静态方法call 第一参数传的是类
                    final Object[] params = {true};
                    XposedHelpers.callStaticMethod(WebView.class, "setWebContentsDebuggingEnabled", params);
                    Log.d(HookH5Plugin.class.getSimpleName(), "package:" + lpparam.packageName);
                }
            }
        );
        // 方法重载
        XposedBridge.hookAllMethods(
            WebView.class,
            "setWebContentsDebuggingEnabled",
            new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    param.args[0] = true;
                    Log.d(HookH5Plugin.class.getSimpleName(), "package:" + lpparam.packageName);
                }
            }
        );
        // 拦截替换直接方法逻辑
        XposedHelpers.findAndHookMethod(
            "io.dcloud.common.adapter.ui.WebViewImpl",
            lpparam.classLoader,
            "setWebViewData",
            new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                    Log.d(HookH5Plugin.class.getSimpleName(), "setWebViewData is replace");
                    return null;
                }
            });
    }
}
