package com.kevalpatel2106.home.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.widget.Toast;

import com.kevalpatel2106.home.admin.base.BaseActivity;
import com.kevalpatel2106.network.APIObserver;
import com.kevalpatel2106.network.RetrofitUtils;
import com.kevalpatel2106.network.responsePojo.DeviceListData;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.btn_register_activity)
    AppCompatButton mRegisterBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle(R.string.title_main);

        getAllDevice();
    }

    private void getAllDevice() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting the list of registered devices");
        progressDialog.setCancelable(false);
        progressDialog.show();

        addSubscription(RetrofitUtils.subscribe(
                RetrofitUtils.getAdminApiService().getAllDevices(RetrofitUtils.getAuthString(this)),
                new APIObserver<DeviceListData>() {
                    @Override
                    public void onError(String errorMessage, int statusCode) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this,
                                errorMessage,
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onSuccess(DeviceListData deviceListData) {
                        //List of all the devices.
                        List<DeviceListData.Device> devices = deviceListData.getDevices();

                        //TODO display in recycler view.
                    }

                }));
    }

    @OnClick(R.id.btn_register_activity)
    void registerBtn() {
        startActivity(new Intent(this, AddDeviceActivity.class));
    }
}
