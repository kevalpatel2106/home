package com.kevalpatel2106.home.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.kevalpatel2106.home.admin.base.BaseActivity;
import com.kevalpatel2106.network.APIObserver;
import com.kevalpatel2106.network.RetrofitUtils;
import com.kevalpatel2106.network.responsePojo.DeviceListData;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * This activity displays the list of all the devices registered.
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.btn_register_activity)
    AppCompatButton mRegisterBtn;

    @BindView(R.id.device_list)
    RecyclerView mDeviceListRv;

    @BindView(R.id.swipe_to_refresh_device_list)
    SwipeRefreshLayout mDeviceSwipeToRefresh;

    private ArrayList<DeviceListData.Device> mDevices;
    private DeviceListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle(R.string.title_main);

        mDevices = new ArrayList<>();
        mAdapter = new DeviceListAdapter(this, mDevices);
        mDeviceListRv.setLayoutManager(new LinearLayoutManager(this));
        mDeviceListRv.setAdapter(mAdapter);

        mDeviceSwipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllDevice();
            }
        });

        mDeviceSwipeToRefresh.setRefreshing(true);
        getAllDevice();
    }

    /**
     * Get the list of all the devices registered.
     */
    private void getAllDevice() {
        addSubscription(RetrofitUtils.subscribe(
                RetrofitUtils.getAdminApiService().getAllDevices(RetrofitUtils.getAuthString(this)),
                new APIObserver<DeviceListData>() {
                    @Override
                    public void onError(String errorMessage, int statusCode) {
                        mDeviceSwipeToRefresh.setRefreshing(false);

                        Toast.makeText(MainActivity.this,
                                errorMessage,
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onSuccess(DeviceListData deviceListData) {
                        mDeviceSwipeToRefresh.setRefreshing(false);

                        //List of all the devices.
                        mDevices.clear();
                        mDevices.addAll(deviceListData.getDevices());
                        mAdapter.notifyDataSetChanged();
                    }
                }));
    }

    @OnClick(R.id.btn_register_activity)
    void registerBtn() {
        startActivity(new Intent(this, AddDeviceActivity.class));
    }
}
