package com.kika.testxposed;

import android.content.Context;
import android.widget.Toast;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public abstract class HookImpl implements IHook {
//    final Handler handler = new Handler(Looper.getMainLooper());

    public void toastOnMain(Object context, String msg) {
        if (context instanceof Context) {
            XposedBridge.log("HookImpl msg=" + msg);
            postOnMain(() -> Toast.makeText((Context) context, "" + msg, Toast.LENGTH_SHORT).show());
        } else {
            XposedBridge.log("HookImpl not Context object context-> " + context + " , msg=" + msg);
        }
    }

    public void log(String msg) {
        XposedBridge.log(msg);
    }

    public void log(String... msg) {
        StringBuilder sb = new StringBuilder();
        for (String s : msg) {
            sb.append(s).append(" ,");
        }
        XposedBridge.log(sb.toString());
    }

    public void postOnMain(Runnable runnable) {

        //handler.post(runnable);
    }

    /**
     * 获取静态属性的值
     *
     * @param className   className
     * @param classLoader classLoader
     * @return t
     */
    public Object getStaticObject(String className, ClassLoader classLoader, String fieldName) {
        try {
            Class<?> clazz = XposedHelpers.findClass(className, classLoader);
            return XposedHelpers.getStaticObjectField(clazz, fieldName);
        } catch (Exception e) {
            XposedBridge.log(e);
        }
        return null;
    }

    /**
     * 设置静态属性的值
     */
    public void setStaticObject(String className, ClassLoader classLoader, String fieldName, Object value) {
        try {
            Class<?> clazz = XposedHelpers.findClass(className, classLoader);
            XposedHelpers.setStaticObjectField(clazz, fieldName, value);
        } catch (Exception e) {
            XposedBridge.log(e);
        }
    }

    /**
     * 获取非静态字段的值
     */
    public Object getObjectField(Object obj, String fieldName) {
        //1. Object obj：这是你想要获取字段值的对象。
        //2. String fieldName：这是你想要获取的字段的名称。
        try {
            return XposedHelpers.getObjectField(obj, fieldName);
        } catch (Exception e) {
            XposedBridge.log(e);
        }
        return null;
    }

    /**
     * 设置非静态字段的值
     */
    public void setObjectField(Object obj, String fieldName, Object value) {
        //1. Object obj：这是你想要获取字段值的对象。
        //2. String fieldName：这是你想要获取的字段的名称。
        try {
            XposedHelpers.setObjectField(obj, fieldName, value);
        } catch (Exception e) {
            XposedBridge.log(e);
        }
    }

    /**
     * 主动调用静态方法
     */
    public void callStaticMethod(String className, ClassLoader classLoader, String methodName, Object... args) {
        try {
            Class<?> clazz = XposedHelpers.findClass(className, classLoader);
            XposedHelpers.callStaticMethod(clazz, methodName, args);
        } catch (Exception e) {
            XposedBridge.log(e);
        }
    }

    public void callMethod(Object obj, String methodName, Object... args) {
        try {
            XposedHelpers.callMethod(obj, methodName, args);
        } catch (Exception e) {
            XposedBridge.log(e);
        }
    }

    public void test() {
        //XposedHelpers.newInstance(Class<?> clazz, Object... args)
        //XposedHelpers.callMethod(Object obj, String methodName, Object... args)
        //XposedHelpers.callStaticMethod(Class<?> clazz, String methodName, Object... args)
    }

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
