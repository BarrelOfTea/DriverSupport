package com.barreloftea.driversupport.presentation.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.barreloftea.driversupport.R;
import com.barreloftea.driversupport.domain.models.BluetoothDeviceM;
import com.barreloftea.driversupport.domain.usecases.interfaces.BlueViewHolderClickListener;

public class BluetoothDeviceAdapter extends RecyclerView.Adapter<BluetoothDeviceAdapter.ViewHolder>{


    private BluetoothDeviceM[] devices;
    private BlueViewHolderClickListener parentFragment;

    public BluetoothDeviceAdapter(BlueViewHolderClickListener listener){
        this.parentFragment = listener;
    }

    public void setData(BluetoothDeviceM[] data) {
        devices = data;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blue_device, parent, false);
        return new BluetoothDeviceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BluetoothDeviceAdapter.ViewHolder holder, int position) {
        holder.getDeviceNameTextView().setText(devices[position].getName());
        holder.getDeviceAddressTextView().setText(devices[position].getAddress());
        holder.setPosition(position);
    }

    @Override
    public int getItemCount() {
        return devices.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView device_name;
        private final TextView device_address;
        private int position;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            device_name = itemView.findViewById(R.id.tv_device_blue_name);
            device_address = itemView.findViewById(R.id.tv_device_blue_address);
            itemView.setOnClickListener(v -> {
                //parentFragment.onViewHolderClick(devices[position]);
            });


        }

        public void setPosition(int pos){
            this.position = pos;
        }

        public TextView getDeviceNameTextView() {
            return device_name;
        }

        public TextView getDeviceAddressTextView() {
            return device_address;
        }
    }

}
