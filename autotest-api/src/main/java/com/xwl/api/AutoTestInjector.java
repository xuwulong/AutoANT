package com.xwl.api;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;

/**
 * Created by xuwulong on 2018/1/11.
 */

public class AutoTestInjector {
    public static String PACKAGE_NAME = "com.xwl.autotest";
    public static String CLASS_NAME = "FakeDevelopReceiver";
    public static String ACTION = "com.xwl.autotest.action";

    public static boolean injectAutoTest(Context context) {
        if (null == context) {
            throw new IllegalArgumentException("Param context can not be null");
        }
        try {
            Class injectorClazz = Class.forName(PACKAGE_NAME + "." + CLASS_NAME);
            BroadcastReceiver testReceiver = (BroadcastReceiver) injectorClazz.newInstance();
            context.registerReceiver(testReceiver, new IntentFilter(ACTION));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("can not find %s , something when compiler.", PACKAGE_NAME + "." + CLASS_NAME));
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return true;
    }

}