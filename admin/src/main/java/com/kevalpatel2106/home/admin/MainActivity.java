package com.kevalpatel2106.home.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.btn_register_activity)
    AppCompatButton mRegisterBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @OnClick(R.id.btn_register_activity)
    void registerBtn() {
        startActivity(new Intent(this, AddDeviceActivity.class));
    }
}
