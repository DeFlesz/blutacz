package antoni.nawrocki.blutacz;

import android.bluetooth.BluetoothClass;

import java.util.HashMap;

public class BluetoothDevicesMap {
    HashMap<Integer, Integer> map = new HashMap<Integer, Integer>() {{
        put(BluetoothClass.Device.PHONE_SMART, R.drawable.ic_baseline_smartphone_24);
        put(BluetoothClass.Device.TOY_GAME, R.drawable.ic_baseline_gamepad_24);
        put(BluetoothClass.Device.PHONE_UNCATEGORIZED, R.drawable.ic_baseline_smartphone_24);
        put(BluetoothClass.Device.COMPUTER_LAPTOP, R.drawable.ic_baseline_computer_24);
        put(BluetoothClass.Device.COMPUTER_DESKTOP, R.drawable.ic_baseline_computer_24);
        put(BluetoothClass.Device.COMPUTER_UNCATEGORIZED, R.drawable.ic_baseline_computer_24);
        put(BluetoothClass.Device.PERIPHERAL_KEYBOARD, R.drawable.ic_baseline_keyboard_24);
        put(BluetoothClass.Device.AUDIO_VIDEO_HEADPHONES, R.drawable.ic_baseline_headphones_24);
        put(BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET, R.drawable.ic_baseline_headset_mic_24);
        put(BluetoothClass.Device.AUDIO_VIDEO_HANDSFREE, R.drawable.ic_baseline_headset_mic_24);
//        put(BluetoothClass.Device.AUDIO_VIDEO_UNCATEGORIZED, R.drawable.ic_baseline_headphones_24);
        put(BluetoothClass.Device.AUDIO_VIDEO_LOUDSPEAKER, R.drawable.ic_baseline_speaker_24);
        put(BluetoothClass.Device.AUDIO_VIDEO_PORTABLE_AUDIO, R.drawable.ic_baseline_speaker_24);
//       ps4 controller
        put(1288, R.drawable.ic_baseline_gamepad_24);
//       sony headphones
        put(7936, R.drawable.ic_baseline_headphones_24);
    }};

    public int getDrawable(int device) {
        return map.getOrDefault(device, R.drawable.ic_baseline_bluetooth_24);
    }
}
