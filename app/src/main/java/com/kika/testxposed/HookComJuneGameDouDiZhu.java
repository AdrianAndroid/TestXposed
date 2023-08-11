package com.kika.testxposed;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookComJuneGameDouDiZhu extends HookImpl{
    @Override
    public String packageName() {
        return "com.june.game.doudizhu";
    }

    @Override
    public String appName() {
        return "单机斗地主";
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedHelpers.findAndHookMethod(
                "com.june.game.a.e",
                lpparam.classLoader,
                "a",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        log("result=" + param.getResult());
                        param.setResult("9000");
                    }
                }
        );
    }
}
