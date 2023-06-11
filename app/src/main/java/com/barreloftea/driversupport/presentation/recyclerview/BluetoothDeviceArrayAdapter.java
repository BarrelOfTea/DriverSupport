package com.barreloftea.driversupport.presentation.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import androidx.annotation.NonNull;

import com.barreloftea.driversupport.R;
import com.barreloftea.driversupport.domain.models.BluetoothDeviceM;

import java.util.List;


public class BluetoothDeviceArrayAdapter extends ArrayAdapter<BluetoothDeviceM> {


    public BluetoothDeviceArrayAdapter(@NonNull Context context, int resource, @NonNull List<BluetoothDeviceM> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        BluetoothDeviceM data = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_blue_device, parent, false);
        }

        // Lookup view for data population
        TextView textView1 = convertView.findViewById(android.R.id.text1);
        TextView textView2 = convertView.findViewById(android.R.id.text2);

        // Populate the data into the template view using the data object
        textView1.setText(data.getName());
        textView2.setText(data.getAddress());

        // Return the completed view to render on screen
        return convertView;
    }

}



/*

private BluetoothDeviceM[] devices;
    private BlueViewHolderClickListener parentFragment;

    public BluetoothDeviceArrayAdapter(BlueViewHolderClickListener listener){
        this.parentFragment = listener;
    }

    public void setData(BluetoothDeviceM[] data) {
        devices = data;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blue_device, parent, false);
        return new BluetoothDeviceArrayAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BluetoothDeviceArrayAdapter.ViewHolder holder, int position) {
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

 */