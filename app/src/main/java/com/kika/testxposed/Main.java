package com.kika.testxposed;

import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import java.util.HashMap;

public class Main implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        Log.i("Main", "handleLoadPackage: Hello world!");
        logPrint(lpparam);
        XposedBridge.log("Hello World!"); // 可以在日志模块中查看
        IHook iHook = new HookZhuCeJi();
        if (iHook.isThisPackageName(lpparam)) {
            XposedBridge.log("is this packages=" + lpparam.packageName);
            iHook.handleLoadPackage(lpparam);
        } else {
            XposedBridge.log("is not this packages=" + lpparam.packageName);
        }
    }

    private void test() {
    }

    /**
     * 动态加载apk等文件
     * @param lpparam
     */
    private void dynamicLoadClass(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedHelpers.findAndHookMethod(
            ClassLoader.class,
            "loadClass",
            String.class,
            new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                }
            }
        );
    }
    /**
     * public String packageName;
     * public String processName;
     * public ClassLoader classLoader;
     * public ApplicationInfo appInfo;
     * public boolean isFirstApplication;
     */
    private void logPrint(XC_LoadPackage.LoadPackageParam lpparam) {
        final HashMap<String, Object> map = new HashMap<>();
        map.put("packageName:", lpparam.packageName);
        map.put("processName", lpparam.processName);
        map.put("classLoader", lpparam.classLoader);
        map.put("appInfo", lpparam.appInfo);
        map.put("isFirstApplication", lpparam.isFirstApplication);
        MyLog.i(this, map);
    }
}
