package com.kika.testxposed;

import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import java.util.HashMap;

public class Main implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        Log.i("Main", "handleLoadPackage: Hello world!");
        logPrint(lpparam);

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
