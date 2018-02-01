package com.longwuxu.apitest;

import android.content.Context;
import android.util.Log;

import com.xwl.annotation.BindTest;

/**
 * Created by xuwulong on 2018/2/1.
 */

public class BModule {
    private static BModule aMoudle;
    private Context mContext;

    public static BModule getInstance(Context context) {
        if (null == aMoudle) {
            aMoudle = new BModule(context);
        }
        return aMoudle;
    }
    private BModule(Context context) {
        mContext = context;
    }

    @BindTest(index = 103)
    public void testB(int a, String b) {
        Log.d("xwl", "user call testB");
    }

    @BindTest(index = 105)
    public void testBSecond() {
        Log.d("xwl", "user call testBSecond");
    }
}
