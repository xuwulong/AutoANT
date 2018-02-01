package com.longwuxu.apitest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.xwl.api.AutoTestInjector;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        AutoTestInjector.injectAutoTest(this);
    }
}
