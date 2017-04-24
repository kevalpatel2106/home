package com.kevalpatel2106.home.admin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kevalpatel2106.home.utils.cons.DeviceType;
import com.kevalpatel2106.network.responsePojo.DeviceListData;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Keval on 24-Apr-17.
 */

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.DeviceListViewHolder> {

    private final Context mContext;
    private final ArrayList<DeviceListData.Device> mDevices;

    public DeviceListAdapter(Context context, ArrayList<DeviceListData.Device> devices) {
        mContext = context;
        mDevices = devices;
    }

    @Override
    public DeviceListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DeviceListViewHolder(LayoutInflater.from(mContext).inflate(R.layout.row_device, parent, false));
    }

    @Override
    public void onBindViewHolder(DeviceListViewHolder holder, int position) {
        DeviceListData.Device device = mDevices.get(position);
        holder.mDeviceIdTv.setText(device.getDeviceId());
        holder.mDeviceNameTv.setText(device.getDeviceName());

        switch (device.getDeviceType()){
            case DeviceType.MOBILE:
                holder.mDeviceTypeIv.setImageResource(R.drawable.ic_phone);
                break;
            case DeviceType.PIE:
                holder.mDeviceTypeIv.setImageResource(R.drawable.ic_pie);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mDevices.size();
    }

    class DeviceListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_device_type)
        AppCompatImageView mDeviceTypeIv;

        @BindView(R.id.tv_device_id)
        AppCompatTextView mDeviceIdTv;

        @BindView(R.id.tv_device_name)
        AppCompatTextView mDeviceNameTv;

        public DeviceListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
