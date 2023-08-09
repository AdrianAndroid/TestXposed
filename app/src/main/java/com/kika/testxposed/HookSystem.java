package com.kika.testxposed;

import android.net.wifi.WifiInfo;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import java.net.InetAddress;

/**
 * Hook 系统的一些方法
 */
public class HookSystem implements IHook {
    public static long isToLong(String strIp) {
        long[] ip = new long[4];
        int position1 = strIp.indexOf(".");
        int position2 = strIp.indexOf(".", position1 + 1);
        int position3 = strIp.indexOf(".", position2 + 1);
        // 将每个.之间的字符串转换成整形
        ip[0] = Long.parseLong(strIp.substring(0, position1));
        ip[1] = Long.parseLong(strIp.substring(position1 + 1, position2));
        ip[2] = Long.parseLong(strIp.substring(position2 + 1, position3));
        ip[3] = Long.parseLong(strIp.substring(position3 + 1));
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] >> 8) + ip[3];
    }

    @Override
    public boolean isThisPackageName(XC_LoadPackage.LoadPackageParam lpparam) {
        return true;
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        // 拦截系统方法,篡改IMEI设备号
        XposedHelpers.findAndHookMethod(
            "android.telephony.TelephonyManager",
            lpparam.classLoader,
            "getDeviceId",
            new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    //param.setResult(HookSystem.isToLong(param.getResult().toString()));
                    XposedBridge.log("imei:" + param.getResult());
                }
            });
        // HOOK构造方法,拦截IP地址
        XposedHelpers.findAndHookConstructor(
            "java.net.InetSocketAddress",
            lpparam.classLoader,
            String.class,
            int.class,
            new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    XposedBridge.log("IP地址:" + param.args[0]);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                }
            }
        );
        // 拦截流量上网IP地址
        XposedHelpers.findAndHookMethod(InetAddress.class,
            "getHostAddress",
            new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    XposedBridge.log("IP地址:" + param.getResult());
                }
            });
        // 拦截WIFI上网IP地址
        XposedHelpers.findAndHookMethod(
            WifiInfo.class,
            "getIpAddress",
            new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    XposedBridge.log("IP地址:" + param.getResult());
                    // 分割字符串
                    String[] str = "192.168.1.99".split("\\.");
                    // 定义一个字符串,用来存储反转后的IP地址
                    String ipAddress = "";
                    // for循环控制IP地址反转
                    for (int i = 3; i >= 0; i--) {
                        ipAddress = ipAddress + str[i] + ".";
                    }
                    // 去除最后一位的"."
                    ipAddress = ipAddress.substring(0, ipAddress.length() - 1);
                    // 返回新的整形IP地址
                    param.setResult((int) isToLong(ipAddress));
                }
            }
        );
    }
}
