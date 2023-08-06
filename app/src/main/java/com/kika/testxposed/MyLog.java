package com.kika.testxposed;

import android.util.Log;

import java.util.Map;

public class MyLog {
    public static void i(Object tag, Map<String, Object> map) {
        final String strTag;
        if (tag == null) {
            strTag = "MyLog";
        } else {
            strTag = tag.getClass().getSimpleName();
        }
        for (String key : map.keySet()) {
            String value = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                value = map.getOrDefault(key, null) + "";
            } else {
                value = map.get(key) + "";
            }
            Log.i(strTag, String.format("key:%s, value=%s", key, value));
        }
    }
}
