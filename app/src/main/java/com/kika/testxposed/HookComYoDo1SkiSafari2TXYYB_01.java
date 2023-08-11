package com.kika.testxposed;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookComYoDo1SkiSafari2TXYYB_01 extends HookImpl{
    @Override
    public String packageName() {
        return "com.yodo1.skisafari2.TXYYB_01";
    }

    @Override
    public String appName() {
        return "滑雪大冒险";
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        hookPaymentHelper(lpparam);
        hookPayAdapterYYB(lpparam);
    }

    private void hookPayAdapterYYB(XC_LoadPackage.LoadPackageParam lpparam) {
        //com.yodo1.sdk.pay.PayAdapterYYB.1
        final String className = "com.yodo1.sdk.pay.PayAdapterYYB$1";
        final ClassLoader classLoader = lpparam.classLoader;
        final String methodName = "OnPayNotify";
        final Class<?> param = XposedHelpers.findClass("com.tencent.ysdk.module.pay.PayRet", classLoader);
        XposedHelpers.findAndHookMethod(
            className, classLoader, methodName,
            param,
            new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    Object obj = param.args[0];
                    log("HookComYoDo1SkiSafari2TXYYB_01 "+ obj.getClass().getCanonicalName() + "\n" + obj);
                    XposedHelpers.setObjectField(obj, "ret", 0);
                    XposedHelpers.setObjectField(obj, "payState", 0);
                    log("HookComYoDo1SkiSafari2TXYYB_01 "+ obj.getClass().getCanonicalName() + "\n" + obj);
                    log("HookComYoDo1SkiSafari2TXYYB_01, " + className + ", " + methodName + ", ");
                }
            }
        );
    }

    private void hookPaymentHelper(XC_LoadPackage.LoadPackageParam lpparam) {
        final String className = "com.yodo1.android.ops.payment.PaymentHelper";
        final ClassLoader classLoader = lpparam.classLoader;
        final String methodName = "netCreateOrder";
        final Class<?> param1 = XposedHelpers.findClass("com.yodo1.android.ops.payment.RequestCreateOrder", classLoader);
        final Class<?> param2 = XposedHelpers.findClass("com.yodo1.android.ops.utils.Yodo1OpsCallback", classLoader);
        XposedHelpers.findAndHookMethod(
            className, classLoader, methodName,
            param1, param2,
            new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    log("HookComYoDo1SkiSafari2TXYYB_01 netCreateOrder beforeHookedMethod");
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    log("HookComYoDo1SkiSafari2TXYYB_01 netCreateOrder afterHookedMethod");
                }
            });
    }
}
