package com.kika.testxposed;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

public interface IHook {
    public boolean isThisPackageName(XC_LoadPackage.LoadPackageParam lpparam);
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable;
}
