package com.kika.testxposed;

import android.app.Application;
import android.content.Context;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookComYoDo1Ctr2A4399_1 extends HookImpl {
    @Override
    public String packageName() {
        return "com.yodo1.ctr2.A4399_1";
    }

    @Override
    public String appName() {
        return "割绳子";
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        // 先hookApplication
        XposedHelpers.findAndHookMethod(
            Application.class,
            "attach",
            Context.class,
            new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    // 获取上下文
                    Context context = (Context) param.args[0];
                    // 获取类加载器
                    ClassLoader classLoader = context.getClassLoader();
                    // 获取自定义的类
                    final Class<?> productData = XposedHelpers.findClass(
                        "com.yodo1.android.sdk.helper.ProductData",
                        classLoader
                    );
                    // 获取自定义的类
                    final Class<?> payType = XposedHelpers.findClass(
                        "com.yodo1.android.sdk.constants.PayType",
                        classLoader
                    );
                    //        @Override // com.yodo1.android.sdk.callback.Yodo1PurchaseListener
                    //        public void purchased(int code, String orderId, ProductData productData, PayType payType) {
                    //            String productId = productData.getProductId();
                    //            if (code == 1) {
                    //                Yodo1GameSDK.paySuccess(productId);
                    //                YLog.d("sdk通知支付成功，开始和ops校验订单... ");
                    //            } else if (code == 2) {
                    //                YLog.d("sdk通知取消支付 ");
                    //                Yodo1GameSDK.payCancel(productId);
                    //            } else {
                    //                YLog.d("sdk通知支付失败 ");
                    //                Yodo1GameSDK.payFail(productId);
                    //            }
                    //        }
                    XposedHelpers.findAndHookMethod(
                        "com.zeptolab.sdk.ui.Yodo1Pay$8",
                        classLoader,
                        "purchased",
                        int.class,
                        String.class,
                        productData,
                        payType,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                super.beforeHookedMethod(param);
                                param.args[0] = 1;
                            }
                        }
                    );
                }
            }
        );
    }

    private static final class TestApplication extends Application {
        /*
        final void attach(Context context) {
            attachBaseContext(context);
            mLoadedApk = ContextImpl.getImpl(context).mPackageInfo;
        }
         */
        @Override
        protected void attachBaseContext(Context base) {
            super.attachBaseContext(base);
        }
    }
}
