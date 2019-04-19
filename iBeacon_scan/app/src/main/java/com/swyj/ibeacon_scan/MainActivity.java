package com.swyj.ibeacon_scan;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.swyj.ibeacon_scan.iBeacon.iBeacon;

import java.util.ArrayList;
import java.util.List;

import static com.swyj.ibeacon_scan.iBeacon.iBeaconDevice.getInstalled_iBeacons;
import static com.swyj.ibeacon_scan.utils.utils.Equal_iBeaconDevice;
import static com.swyj.ibeacon_scan.utils.utils.find_iBeacon;

public class MainActivity extends AppCompatActivity {

    //安装的iBeacon放入该List里
    List<iBeacon> installed_iBeacons = new ArrayList<>();
    //扫描到的iBeacon放入该List里
    List<iBeacon> scan_iBeacons = new ArrayList<>();

    //蓝牙适配器
    BluetoothAdapter mBluetoothAdapter;

    //蓝牙扫描时间2s/轮
    private static final long SCAN_PERIOD = 2000;

    //请求码，随意设置
    final int REQUEST_ENABLE_BT = 1234;

    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        installed_iBeacons = getInstalled_iBeacons();

        //蓝牙请求许可
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_ENABLE_BT);//获得权限

        //初始化蓝牙
        init_ble();
        mHandler = new Handler();

        scanLeDevice(true);
    }


    //初始化蓝牙
    private void init_ble() {

        //手机硬件支持蓝牙
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {

            Toast.makeText(this, "不支持BLE", Toast.LENGTH_SHORT).show();
            finish();
        }
        // Initializes Bluetooth adapter.
        //获取手机本地的蓝牙适配器
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        //打开蓝牙权限
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {

            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

    }



    //扫描蓝牙设备
    //enable(扫描使能，true:扫描开始,false:扫描停止)
    private void scanLeDevice(final boolean enable) {

        if (enable) {

            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    Log.i("SCAN", "stop.....................");
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);

                    //扫描结束后
                    //将扫描到的存入scan_iBeacon中
                    for (int i = 0;i < installed_iBeacons.size();i++) {
                        if (installed_iBeacons.get(i).isDiscover()) {
                            scan_iBeacons.add(installed_iBeacons.get(i));
                        }
                    }


                    for (int i = 0;i < installed_iBeacons.size();i++) {
                        for (int j = 0;j < installed_iBeacons.get(i).getRSSI_Value().size();j++){
                            System.out.println(installed_iBeacons.get(i).getBluetoothAddress() + "   " + installed_iBeacons.get(i).getRSSI_Value().get(j));
                        }
                    }

                    scan_iBeacons.clear();//清空上一次定位残留下的设备

                    scanLeDevice(true);//递归调用实现自动扫描
                }
            }, SCAN_PERIOD);

            /*开始扫描蓝牙设备，带mLeScanCallback回调函数 */
            Log.i("SCAN", "begin.....................");
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            Log.i("Stop", "stoping................");
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }

    }

    //蓝牙扫描回调函数 实现扫描蓝牙设备，回调蓝牙BluetoothDevice，可以获取name MAC等信息
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {

            for (int i = 0; i <installed_iBeacons.size(); i++) {
                if(installed_iBeacons.get(i).getBluetoothAddress().equals(device.getAddress())) {

                    installed_iBeacons.get(i).setRSSI_Value(rssi);//将扫描到的RSSI值存入数组
                    installed_iBeacons.get(i).setDiscover(true);//标明已扫描到
                }
            }

        }

    };


}
