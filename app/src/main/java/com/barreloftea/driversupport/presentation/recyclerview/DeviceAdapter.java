package com.barreloftea.driversupport.presentation.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.barreloftea.driversupport.R;
import com.barreloftea.driversupport.domain.models.Device;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder>{


    private Device[] devices;
    private DeviceViewHolderClickListener parentFragment;

    public DeviceAdapter(DeviceViewHolderClickListener listener){
        this.parentFragment = listener;
    }

    public void setData(Device[] data) {
        devices = data;
    }
    public Device[] getData(){return devices;}


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device, parent, false);
        return new DeviceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceAdapter.ViewHolder holder, int position) {
        holder.getDeviceNameTextView().setText(devices[position].getName());
        holder.setPosition(position);
        if (devices[position].isSaved())
            holder.getDevicePlusIcon().setImageResource(R.drawable.baseline_remove_24);
        else
            holder.getDevicePlusIcon().setImageResource(R.drawable.baseline_add_24);
    }

    @Override
    public int getItemCount() {
        return devices.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView device_name;
        private final ImageView plus_view;
        private int position;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            device_name = itemView.findViewById(R.id.tv_device_name);
            plus_view = itemView.findViewById(R.id.icon_device_add);
            itemView.setOnClickListener(v -> {
                parentFragment.onViewHolderClick(devices[position]);
            });


        }

        public void setPosition(int pos){
            this.position = pos;
        }

        public TextView getDeviceNameTextView() {
            return device_name;
        }
        public ImageView getDevicePlusIcon() {
            return plus_view;
        }

    }

}
