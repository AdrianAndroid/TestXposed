package com.kika.testxposed;

import android.widget.EditText;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;

public class HookZhuCeJi implements IHook {
    final String packageName = "com.qianyu.zhuceji";

    @Override
    public boolean isThisPackageName(XC_LoadPackage.LoadPackageParam lpparam) {
        return packageName.equals(lpparam.packageName);
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.log("编写Xposed插件模板, 开启Hook之路!");
        XposedBridge.log("找到我们要hook的应用程序");
        XposedHelpers.findAndHookMethod(
            "com.qianyu.zhuceji.MainActivity",
            lpparam.classLoader, // 类加载器
            "checkSN", // 方法名
            String.class, String.class, // 参数列表
            new XC_MethodHook() {
                // HOOK之前,打印参数信息,修改参数
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    // 打印参数信息
                    XposedBridge.log("username=" + param.args[0]);
                    XposedBridge.log("sn=" + param.args[1]);

                    // 打印方法调用堆栈信息
                    StackTraceElement[] wodelogs = new Throwable("wodelog").getStackTrace();
                    for (StackTraceElement wodelog : wodelogs) {
                        XposedBridge.log("查看堆栈:" + wodelog.toString());
                    }
                }

                /**
                 *     private Button btn;
                 *     private EditText edit_sn;
                 *     private EditText edit_username;
                 * @param param param
                 * @throws Throwable Throwable
                 */
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    // 打印返回值信息
                    XposedBridge.log("返回值:" + param.getResult());
                    // 修改返回值
                    // param.setResult(true);
                    Class<?> clazz = param.thisObject.getClass();
                    XposedBridge.log("参数:" + clazz);
                    // 获取用户名字段
                    Field edit_username = clazz.getDeclaredField("edit_username");
                    // 设置可以访问
                    edit_username.setAccessible(true);
                    // 获取组件
                    EditText et_user = (EditText) edit_username.get(param.thisObject);
                    // 获取内容
                    String name = et_user.getText().toString().trim();

                    // 获取注册码字段
                    Field edit_sn = clazz.getDeclaredField("edit_sn");
                    edit_sn.setAccessible(true);
                    EditText et_sn = (EditText) edit_sn.get(param.thisObject);
                    String sn = et_sn.getText().toString().trim();
                    XposedBridge.log("用户名:" + name);
                    XposedBridge.log("注册码:" + name);

                    // 指定算法MD5
                    MessageDigest digest = MessageDigest.getInstance("MD5");
                    // 初始化
                    digest.reset();
                    // 更新
                    digest.update("3192850648@qq.com".getBytes());
                    // 获取方法
                    String methodName = "toHexString";
                    Class<?>[] parameterTypes = new Class[]{byte[].class, String.class};
                    Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
                    // 设置可见
                    method.setAccessible(true);
                    // 主动调用方法
                    String hexstr = (String)method.invoke(param.thisObject, new Object[]{digest.digest(), sn});
                    // 字符串拼接
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < hexstr.length(); i++) {
                        sb.append(hexstr.charAt(i));
                    }
                    // 设置编辑框的值
                    et_user.setText("3192850648@qq.com");
                    et_sn.setText(sb.toString());
                }
            });
    }
}
