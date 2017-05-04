package com.kevalpatel2106.home.activity;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.kevalpatel2106.home.R;
import com.kevalpatel2106.home.base.BaseActivity;
import com.kevalpatel2106.home.utils.Utils;
import com.kevalpatel2106.home.utils.managers.DeviceSessionManager;
import com.kevalpatel2106.network.APIObserver;
import com.kevalpatel2106.network.RetrofitUtils;
import com.kevalpatel2106.network.requestPojo.LoginRequest;
import com.kevalpatel2106.network.responsePojo.LoginResponseData;

import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_login_activity);
    }

    @OnClick(R.id.tv_what_is_my_device_id)
    void displayDeviceId() {
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage("You device id is " + Utils.getDeviceId(this) + ".")
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Copy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("DeviceId", Utils.getDeviceId(LoginActivity.this));
                        clipboard.setPrimaryClip(clip);
                    }
                })
                .show();

    }

    @OnClick(R.id.btn_authenticate_device)
    void registerDevice() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Authenticating device");
        progressDialog.setCancelable(false);
        progressDialog.show();

        LoginRequest request = new LoginRequest(this);
        request.setGcmKey(FirebaseInstanceId.getInstance().getToken());
        addSubscription(RetrofitUtils.subscribe(RetrofitUtils.getApiService().updateGcmId(request),
                new APIObserver<LoginResponseData>() {
                    @Override
                    public void onError(String errorMessage, int statusCode) {
                        Toast.makeText(LoginActivity.this,
                                errorMessage,
                                Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onSuccess(LoginResponseData loginResponseData) {
                        progressDialog.dismiss();

                        new DeviceSessionManager(LoginActivity.this)
                                .setNewSession(loginResponseData.getDeviceId(), loginResponseData.getToken());

                        Toast.makeText(LoginActivity.this,
                                "DeviceData logged in successfully.",
                                Toast.LENGTH_LONG).show();

                        startActivity(new Intent(LoginActivity.this, CommandActivity.class));
                        finish();
                    }
                }));
    }
}
