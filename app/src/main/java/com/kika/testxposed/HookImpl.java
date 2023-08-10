package com.kika.testxposed;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

public abstract class HookImpl implements IHook {
    @Override
    public boolean isThisPackageName(XC_LoadPackage.LoadPackageParam lpparam) {
        final String packageName = packageName();
        if (packageName == null) {
            return false;
        } else {
            return packageName.equals(lpparam.packageName);
        }
    }

    @Override
    public String appName() {
        return null;
    }
}
