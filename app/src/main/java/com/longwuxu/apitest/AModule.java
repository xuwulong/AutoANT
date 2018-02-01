package com.longwuxu.apitest;

import android.content.Context;
import android.util.Log;

import com.xwl.annotation.BindTest;

/**
 * Created by xuwulong on 2018/2/1.
 */

public class AModule {
    private static AModule aMoudle;
    private Context mContext;

    public static AModule getInstance(Context context) {
        if (null == aMoudle) {
            aMoudle = new AModule(context);
        }
        return aMoudle;
    }


    private AModule(Context context) {
        mContext = context;
    }

    @BindTest(index = 101)
    public void testA(int a, int b) {
        Log.d("xwl", "user call testA");
    }

    @BindTest(index = 102)
    public void testASecond() {
        Log.d("xwl", "user call testASecond");
    }
}
