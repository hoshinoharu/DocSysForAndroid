package com.rehoshi.docsys;

import android.content.Intent;
import android.os.Bundle;

import com.rehoshi.docsys.base.BaseActivity;
import com.rehoshi.docsys.ui.LoginActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
