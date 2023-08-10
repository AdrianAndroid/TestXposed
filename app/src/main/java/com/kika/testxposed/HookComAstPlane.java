package com.kika.testxposed;

import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import java.util.Map;

public class HookComAstPlane extends HookImpl {
    @Override
    public String packageName() {
        return "com.ast.plane";
    }

    @Override
    public String appName() {
        return "雷电星海战歌";
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        String className = "com.ast.plane.PayUnity$1";
        ClassLoader classLoader = lpparam.classLoader;
        String methodNamePaySuccess = "paySuccess";
        String methodNamePayFailed = "payFailed";
        String methodNamePayCancel = "payCancel";
        final XC_MethodReplacement xc_replacementToPaySuccess = new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                // 获取商品的index参数
                Map<String, String> map = (Map<String, String>) methodHookParam.args[0];
                Object index = map.get("toolsAlias");
                XposedBridge.log("HookComAstPlane methodl index=" + index);
                // Hook 购买成功的方法
                //UnityPlayer.UnitySendMessage("Payback", "Callback", index);
                final String callClassName = "com.unity3d.player.UnityPlayer";
                final String callMethodName = "UnitySendMessage";
                final Object[] args = {"Payback", "Callback", index};
                Class<?> clazz = XposedHelpers.findClass(callClassName, lpparam.classLoader);
                Object result = XposedHelpers.callStaticMethod(clazz, callMethodName, args);
                XposedBridge.log("HookComAstPlane paySuccess index=" + index + ", result=" + result);
                return null;
            }
        };
        // 替换paySuccess, 因为新版本的Android不能在子线程Toast
        XposedHelpers.findAndHookMethod(
            className,
            lpparam.classLoader,
            methodNamePaySuccess,
            Map.class,
            xc_replacementToPaySuccess
        );
        // 替换payFailed
        XposedHelpers.findAndHookMethod(
            className,
            lpparam.classLoader,
            methodNamePayFailed,
            Map.class,
            int.class,
            xc_replacementToPaySuccess
        );
        // 替换payCancel
        XposedHelpers.findAndHookMethod(
            className,
            lpparam.classLoader,
            methodNamePayCancel,
            Map.class,
            xc_replacementToPaySuccess
        );
    }
}
