package com.kika.testxposed;

import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main implements IXposedHookLoadPackage {
    static final List<IHook> listHooks = new ArrayList<>();
    static {
        listHooks.add(new HookZhuCeJi());
        listHooks.add(new HookComAstPlane());
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        Log.i("Main", "handleLoadPackage: Hello world!");
        logPrint(lpparam);
        XposedBridge.log("Hello World!"); // 可以在日志模块中查看
        for (IHook iHook : listHooks) {
            final String appName = iHook.appName();
            XposedBridge.log("==================");
            XposedBridge.log("==================");
            XposedBridge.log("====start=========");
            XposedBridge.log("====start=========" + appName + "========");
            if (iHook.isThisPackageName(lpparam)) {
                XposedBridge.log("is this packages=" + lpparam.packageName);
                iHook.handleLoadPackage(lpparam);
            } else {
                XposedBridge.log("is not this packages=" + lpparam.packageName);
            }
            XposedBridge.log("====end=========");
            XposedBridge.log("==================");
            XposedBridge.log("==================");
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
