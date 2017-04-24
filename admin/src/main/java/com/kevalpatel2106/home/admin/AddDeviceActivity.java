package com.kevalpatel2106.home.admin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.widget.Toast;

import com.kevalpatel2106.home.admin.base.BaseActivity;
import com.kevalpatel2106.network.APIObserver;
import com.kevalpatel2106.network.RetrofitUtils;
import com.kevalpatel2106.network.Validator;
import com.kevalpatel2106.network.requestPojo.DeviceRegisterRequest;
import com.kevalpatel2106.network.responsePojo.DeviceRegisterData;

import butterknife.BindView;
import butterknife.OnClick;

public class AddDeviceActivity extends BaseActivity {

    @BindView(R.id.btn_register_device)
    AppCompatButton mRegisterBtn;

    @BindView(R.id.et_device_id)
    AppCompatEditText mDeviceIdEt;

    @BindView(R.id.et_device_name)
    AppCompatEditText mDeviceNameEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_register_device);
    }

    @OnClick(R.id.btn_register_device)
    void registerDevice() {

        //Validate device name
        final String deviceName = mDeviceNameEt.getText().toString().trim();
        if (Validator.isValidDeviceName(deviceName)) {

            //Valid device id
            final String deviceId = mDeviceIdEt.getText().toString().trim();
            if (Validator.isValidDeviceId(deviceId)) {

                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Registering device");
                progressDialog.setCancelable(false);
                progressDialog.show();

                //Make an api call
                DeviceRegisterRequest request = new DeviceRegisterRequest();
                request.setDeviceName(deviceName);
                request.setDeviceId(deviceId);
                addSubscription(RetrofitUtils.subscribe(RetrofitUtils.getAdminApiService().registerDevice(request),
                        new APIObserver<DeviceRegisterData>() {
                            @Override
                            public void onError(String errorMessage, int statusCode) {
                                progressDialog.dismiss();
                                Toast.makeText(AddDeviceActivity.this,
                                        errorMessage,
                                        Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onSuccess(DeviceRegisterData deviceRegisterData) {
                                progressDialog.dismiss();
                                Toast.makeText(AddDeviceActivity.this,
                                        "DeviceData registered successfully.",
                                        Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }));
            } else {
                mDeviceIdEt.setError("DeviceData id must be more than " + Validator.DEVICE_ID_MIN_LENGTH + " characters.");
            }
        } else {
            mDeviceNameEt.setError("DeviceData name must be more than " + Validator.DEVICE_NAME_MIN_LENGTH + " characters.");
        }
    }
}
