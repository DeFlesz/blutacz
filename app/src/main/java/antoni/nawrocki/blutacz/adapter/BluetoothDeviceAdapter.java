package antoni.nawrocki.blutacz.adapter;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

import antoni.nawrocki.blutacz.BluetoothDevicesMap;
import antoni.nawrocki.blutacz.R;

public class BluetoothDeviceAdapter extends RecyclerView.Adapter<BluetoothDeviceAdapter.ViewHolder> {
    ArrayList<BluetoothDevice> bluetoothDevices = new ArrayList<>();
    Context context;

    public BluetoothDeviceAdapter(ArrayList<BluetoothDevice> bluetoothDevices, Context context) {
        this.bluetoothDevices = bluetoothDevices;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bluetooth_device_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BluetoothDevice bluetoothDevice = bluetoothDevices.get(position);
//
//        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }

        holder.deviceName.setText(bluetoothDevice.getName());
        holder.deviceMacadress.setText(bluetoothDevice.getAddress());

        int drawable = new BluetoothDevicesMap().getDrawable(bluetoothDevice.getBluetoothClass().getDeviceClass());

        holder.deviceIcon.setImageResource(drawable);
    }

    @Override
    public int getItemCount() {
        return bluetoothDevices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView deviceIcon;
        TextView deviceName;
        TextView deviceMacadress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            deviceIcon = itemView.findViewById(R.id.deviceIcon);
            deviceName = itemView.findViewById(R.id.deviceName);
            deviceMacadress = itemView.findViewById(R.id.deviceMacadress);
        }
    }
}
