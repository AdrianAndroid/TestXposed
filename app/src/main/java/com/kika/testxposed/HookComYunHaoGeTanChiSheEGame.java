package com.kika.testxposed;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import java.util.HashMap;
import java.util.Map;

/*

    public static void DX_Pay(HashMap<String, String> payParams) {
        new AlertDialog.Builder(activity);
        EgamePay.pay(activity, payParams, new EgamePayListener() { // from class: com.qy.zombie.zombie.3
            @Override // cn.egame.terminal.paysdk.EgamePayListener
            public void paySuccess(Map<String, String> params) {
                Toast.makeText(zombie.activity, "支付成功", 0).show();
                zombie.BuySccess();
            }

            @Override // cn.egame.terminal.paysdk.EgamePayListener
            public void payFailed(Map<String, String> params, int errorInt) {
                Toast.makeText(zombie.activity, "支付失败" + errorInt, 0).show();
                zombie.BuyFailed();
            }

            @Override // cn.egame.terminal.paysdk.EgamePayListener
            public void payCancel(Map<String, String> params) {
                Toast.makeText(zombie.activity, "支付取消", 0).show();
                zombie.BuyFailed();
            }
        });
    }

    public static void DX_Pay(java.util.HashMap<java.lang.String, java.lang.String> r3) {
        android.app.AlertDialog$Builder r0 = new android.app.AlertDialog$Builder
        android.app.Activity r1 = com.qy.zombie.zombie.activity
        r0.<init>(r1)
        android.app.Activity r1 = com.qy.zombie.zombie.activity
        com.qy.zombie.zombie$3 r2 = new com.qy.zombie.zombie$3
        r2.<init>()
        cn.egame.terminal.paysdk.EgamePay.pay(r1, r3, r2)
        return
    }
 */
public class HookComYunHaoGeTanChiSheEGame extends HookImpl{
    @Override
    public String packageName() {
        return "com.yunhaoge.tanchishe.egame";
    }

    @Override
    public String appName() {
        return "经典贪吃蛇大作战_1.00";
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        // Hook com.qy.zombie.zombie
        hookDxPay(lpparam);
        hookMethod(lpparam);
    }

    private void hookMethod(XC_LoadPackage.LoadPackageParam lpparam) {
        //com.qy.zombie.zombie$3
        final String className = "com.qy.zombie.zombie$3";
        final ClassLoader classLoader = lpparam.classLoader;
        final String methodName_payFailed = "payFailed";
        final String methodName_payCancel = "payCancel";
        final XC_MethodReplacement replacement = new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                Object thisObject = methodHookParam.thisObject; // com.qy.zombie.zombie$3
                Object param = methodHookParam.args[0]; // Map
                log("replaceHookedMethod", "thisObject" + thisObject, "param=" + param);
                callMethod(thisObject, "paySuccess", param);
                return null;
            }
        };
        XposedHelpers.findAndHookMethod(className, classLoader, methodName_payFailed, Map.class, int.class,
            replacement);
        XposedHelpers.findAndHookMethod(className, classLoader, methodName_payCancel, Map.class, replacement);
    }

    private void hookDxPay(XC_LoadPackage.LoadPackageParam lpparam) {
        final String className = "com.qy.zombie.zombie";
        final String methodName = "DX_Pay";
        final ClassLoader classLoader = lpparam.classLoader;
        XposedHelpers.findAndHookMethod(
            className,
            classLoader,
            methodName,
            HashMap.class,
            new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Object object = getStaticObject(className, classLoader, "activity");
                toastOnMain(object, "beforeHooked!");
            }
        });
    }
}

// frida 实现
/*
# -*- coding: utf-8 -*-

import frida
import sys

# 经典贪吃蛇大作战
jscode = """
Java.perform(function(){
    var pay = Java.use('com.qy.zombie.zombie$3');
    var zombie = Java.use('com.qy.zombie.zombie');
    pay.payCancel.implementation = function () {
        zombie.BuySccess();
    }
    pay.payFailed.implementation = function () {
        zombie.BuySccess();
    }
});
"""

def message(message, data):
    if message["type"] == 'send':
        print("[*] {0}".format(message['payload']))
    else:
        print(message)

process = frida.get_remote_device().attach('com.yunhaoge.tanchishe.egame')
script = process.create_script(jscode)
script.on("message", message)
script.load()
sys.stdin.read()

 */
