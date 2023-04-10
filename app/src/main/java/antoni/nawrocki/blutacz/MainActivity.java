package antoni.nawrocki.blutacz;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.ArrayList;
import java.util.Set;

import antoni.nawrocki.blutacz.adapter.BluetoothDeviceAdapter;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "bt-dev";
    private static final int REQUEST_ENABLE_BT = 421;
    private static final int REQUEST_ENABLE_DISCOVERABLE = 420;
//    ImageView availibilityIcon;
    RecyclerView pairedDevicesRecycler;
    RecyclerView newDevicesRecycler;
    TextView macAdress;
    LinearProgressIndicator progressIndicator;
    LinearProgressIndicator progressIndicator2;
    MaterialButton scanButton;
    MaterialButton enableDiscoverability;
    ArrayList<BluetoothDevice> newDevices = new ArrayList<>();
    BluetoothDeviceAdapter newDevicesAdapter = new BluetoothDeviceAdapter(newDevices, this);

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getName() == null) {
                    return;
                }
                if(newDevices.contains(device)){
                    return;
                }
//                String deviceName = device.getName();
//                String deviceHardwareAddress = device.getAddress(); // MAC address
//                Log.i("bt-dev", deviceName + " " + deviceHardwareAddress);
                newDevices.add(device);
                newDevicesAdapter.notifyDataSetChanged();
            }
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                progressIndicator.show();
                newDevices.clear();
                Log.i("bt-dev", "discovery started");
            }
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                progressIndicator.hide();
                scanButton.setIcon(getDrawable(R.drawable.ic_baseline_refresh_24));
                Log.i("bt-dev", "discovery finished");
            }
        }
    };

    BroadcastReceiver receiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)){
                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);

                switch (mode) {
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d(TAG, "onReceive: discoverable");
                        progressIndicator2.show();
                        break;
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d(TAG, "onReceive: discoverable");
                        progressIndicator2.hide();
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d(TAG, "onReceive: discoverable");
                        progressIndicator2.hide();
                        break;
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();

//        availibilityIcon = findViewById(R.id.bluetooth_available);
        pairedDevicesRecycler = findViewById(R.id.pairedDevicesRecyclerView);
        newDevicesRecycler = findViewById(R.id.newDevicesRecyclerView);
        macAdress = findViewById(R.id.macadressBody);
        progressIndicator = findViewById(R.id.progressIndicator);
        progressIndicator2 = findViewById(R.id.progressIndicator2);
        scanButton = findViewById(R.id.scanDevices);
        enableDiscoverability = findViewById(R.id.enableDiscoverability);

        progressIndicator.hide();
        progressIndicator2.hide();

//      Check for bluetooth availability
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)) {
            Toast.makeText(this, "Bluetooth nie jest wspierany", Toast.LENGTH_SHORT).show();
        }

        BluetoothManager bluetoothManager = getSystemService(BluetoothManager.class);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
//        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!bluetoothAdapter.isEnabled()) {
//            availibilityIcon.setBackgroundColor(Color.RED);
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
//            availibilityIcon.setBackgroundColor(Color.BLUE);
        }

        ArrayList<BluetoothDevice> pairedDevices = new ArrayList<>(bluetoothAdapter.getBondedDevices());
//
//        if (pairedDevices.size() > 0) {
//            // There are paired devices. Get the name and address of each paired device.
//            for (BluetoothDevice device : pairedDevices) {
//                String deviceName = device.getName();
//                String deviceHardwareAddress = device.getAddress(); // MAC address
//                int id = device.getBluetoothClass().getDeviceClass();
//                Log.i("bt-dev:", deviceName + " " + deviceHardwareAddress + " " + id);
//            }
//        }

        BluetoothDeviceAdapter bluetoothDeviceAdapter = new BluetoothDeviceAdapter(pairedDevices, this);
        pairedDevicesRecycler.setAdapter(bluetoothDeviceAdapter);

        newDevicesRecycler.setAdapter(newDevicesAdapter);

        macAdress.setText(bluetoothAdapter.getAddress());

//        Log.v("bt-dev", bluetoothAdapter.getState() + " " + bluetoothAdapter.getScanMode());

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(receiver, filter);

        IntentFilter filter2 = new IntentFilter();
        filter2.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter2.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter2.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(receiver2, filter2);

        scanButton.setOnClickListener(l -> {
//            Log.v("bt-dev", "whatever");
            checkBluetoothPermission();
            if (bluetoothAdapter.isDiscovering()) {
                Log.v("bt-dev", "canceling discovering");
                bluetoothAdapter.cancelDiscovery();
                scanButton.setIcon(getDrawable(R.drawable.ic_baseline_refresh_24));

            } else {
                bluetoothAdapter.cancelDiscovery();
                bluetoothAdapter.startDiscovery();
                scanButton.setIcon(getDrawable(R.drawable.ic_baseline_close_24));
            }
        });

        scanButton.performClick();

        enableDiscoverability.setOnClickListener(l -> {
            if (bluetoothAdapter.isDiscovering()) {
                return;
            }
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 20);
            startActivityForResult(discoverableIntent, REQUEST_ENABLE_DISCOVERABLE);
        });
    }

    public void checkPermission() {
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        }else {
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
//        if (checkSelfPermission(android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED) {
//        }else {
//            ActivityCompat.requestPermissions(this, new String[]{
//                    android.Manifest.permission.BLUETOOTH_CONNECT,
//                    android.Manifest.permission.BLUETOOTH_SCAN}, 2);
//        }
    }

    public void checkBluetoothPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (checkSelfPermission(android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED) {
            }else {
                ActivityCompat.requestPermissions(this, new String[]{
                        android.Manifest.permission.BLUETOOTH_CONNECT,
                        android.Manifest.permission.BLUETOOTH_SCAN}, 2);
            }
        } else {
            if (checkSelfPermission(android.Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED) {
            }else {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_ADMIN}, 2);
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(receiver);
        unregisterReceiver(receiver2);
    }
}