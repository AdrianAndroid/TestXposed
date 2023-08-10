package com.kika.testxposed;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

public class HookProxyPlugin extends HookImpl{
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
        XposedHelpers.findAndHookMethod(
            System.class,
            "getProperty",
            String.class,
            new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    XposedBridge.log("绕过代理检测");
                    String str = (String) param.args[0];
                    if (str.equals("https.proxyHost") || str.equals("http.proxyHost")
                        || str.equals("https.proxyPort") || str.equals("http.proxyPort")) {
                        param.setResult(null);
                    }
                }
            }
        );

        XposedHelpers.findAndHookMethod(
            URL.class,
            "openConnection",
            new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    XposedBridge.log("强制代理");
                    param.args[0] = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.168.99.13", 8888));
                }
            });
    }
}
