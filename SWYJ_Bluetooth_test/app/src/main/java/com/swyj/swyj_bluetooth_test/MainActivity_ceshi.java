/*
package com.swyj.swyj_bluetooth_test;

import java.util.ArrayList;
import java.util.List;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.swyj.swyj_bluetooth_test.R;
import com.swyj.swyj_bluetooth_test.iBeacon.iBeacon;


import static com.swyj.swyj_bluetooth_test.iBeacon.iBeaconDevice_ceshi.getInstalled_iBeacons;
import static com.swyj.swyj_bluetooth_test.positioning.Positioning.runPositioning;
import static com.swyj.swyj_bluetooth_test.utils.utils.Equal_iBeaconDevice;
import static com.swyj.swyj_bluetooth_test.utils.utils.ToTwoDecimals;
import static com.swyj.swyj_bluetooth_test.utils.utils.find_iBeacon;


//MainActivity类实现打开蓝牙、扫描蓝牙
public class MainActivity_ceshi extends Activity implements OnClickListener {

    //安装的iBeacon放入该List里
    List<iBeacon> installed_iBeacons = new ArrayList<>();

    List<iBeacon> scan_iBeacons = new ArrayList<>();

    double[] coord = new double[2];

    //扫描蓝牙按钮
    private Button scan_btn;
    //蓝牙适配器
    BluetoothAdapter mBluetoothAdapter;
    //蓝牙信号强度
    private ArrayList<Integer> rssis;
    //自定义Adapter
    LeDeviceListAdapter mLeDeviceListAdapter;
    //listview显示扫描到的蓝牙信息
    ListView lv;
    //显示定位坐标
    TextView textView;
    //描述扫描蓝牙的状态
    private boolean mScanning;
    private boolean scan_flag;
    private Handler mHandler;
    int REQUEST_ENABLE_BT = 1;
    //蓝牙扫描时间
    private static final long SCAN_PERIOD = 800;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_ENABLE_BT);//获得权限

        installed_iBeacons = getInstalled_iBeacons();

        //初始化控件
        init();
        //初始化蓝牙
        init_ble();
        scan_flag = true;
        //自定义适配器
        mLeDeviceListAdapter = new LeDeviceListAdapter();
        //为listview指定适配器
        lv.setAdapter(mLeDeviceListAdapter);

    }

    //初始化UI控件
    private void init() {
        scan_btn = this.findViewById(R.id.scan_dev_btn);
        scan_btn.setOnClickListener(this);
        lv = this.findViewById(R.id.lv);
        textView = this.findViewById(R.id.text);
        mHandler = new Handler();
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

    //按钮响应事件
    @Override
    public void onClick(View v) {

        if (scan_flag) {

            mLeDeviceListAdapter = new LeDeviceListAdapter();
            lv.setAdapter(mLeDeviceListAdapter);
            scanLeDevice(true);
        } else {

            scanLeDevice(false);
            scan_btn.setText("扫描设备");
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

                    mScanning = false;
                    scan_flag = true;
                    scan_btn.setText("扫描设备");
                    Log.i("SCAN", "stop.....................");
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    //扫描结束后进行操作
                    coord = runPositioning(scan_iBeacons);
                    */
/*for (int i = 0;i < coord.length;i++) {
                        System.out.println(coord[i]);
                    }*//*

                    */
/*for (int i = 0;i < scan_iBeacons.size();i++) {
                        System.out.println(scan_iBeacons.get(i).getRssi());
                    }*//*


                    textView.setText("x:" + ToTwoDecimals(coord[0]) + " " + "y:" + ToTwoDecimals(coord[1]));
                    //scan_btn.performClick();
                }
            }, SCAN_PERIOD);

            */
/*开始扫描蓝牙设备，带mLeScanCallback回调函数 *//*

            Log.i("SCAN", "begin.....................");
            mScanning = true;
            scan_flag = false;
            scan_btn.setText("停止扫描");
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {

            Log.i("Stop", "stoping................");
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            scan_flag = true;
        }

    }

    //蓝牙扫描回调函数 实现扫描蓝牙设备，回调蓝牙BluetoothDevice，可以获取name MAC等信息
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {

            // TODO Auto-generated method stub
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    */
/*讲扫描到设备的信息输出到listview的适配器*//*

                    mLeDeviceListAdapter.addDevice(device, rssi);
                    mLeDeviceListAdapter.notifyDataSetChanged();
                }
            });

            System.out.println("Address:" + device.getAddress());
            System.out.println("Name:" + device.getName());
            System.out.println("rssi:" + rssi);

        }

    };

    //自定义适配器Adapter,作为listview的适配器
    private class LeDeviceListAdapter extends BaseAdapter {

        private ArrayList<BluetoothDevice> mLeDevices;

        private LayoutInflater mInflator;

        public LeDeviceListAdapter() {

            super();
            rssis = new ArrayList<>();
            mLeDevices = new ArrayList<>();
            mInflator = getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device, int rssi) {

            if (!mLeDevices.contains(device) && Equal_iBeaconDevice(installed_iBeacons,device.getAddress())){
                mLeDevices.add(device);
                rssis.add(rssi);
                scan_iBeacons.add(find_iBeacon(installed_iBeacons,device.getAddress(),rssi));
                //System.out.println("成功添加设备：" + scan_iBeacons.get(0).getBluetoothAddress());
            }

        }

        public BluetoothDevice getDevice(int position)
        {
            return mLeDevices.get(position);
        }

        public void clear() {

            mLeDevices.clear();
            rssis.clear();
        }

        @Override
        public int getCount()
        {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int i)
        {
            return mLeDevices.get(i);
        }

        @Override
        public long getItemId(int i)
        {
            return i;
        }

        //重写getview
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            // General ListView optimization code.
            //加载listview每一项的视图
            view = mInflator.inflate(R.layout.listitem, null);
            //初始化三个textview显示蓝牙信息
            TextView deviceAddress = view.findViewById(R.id.tv_deviceAddr);
            TextView deviceName = view.findViewById(R.id.tv_deviceName);
            TextView rssi = view.findViewById(R.id.tv_rssi);
            TextView distance = view.findViewById(R.id.tv_distance);

            BluetoothDevice device = mLeDevices.get(i);
            deviceAddress.setText(device.getAddress());
            deviceName.setText(device.getName());
            rssi.setText("" + scan_iBeacons.get(i).getRssi());
            distance.setText("" + scan_iBeacons.get(i).getDistance());

            return view;
        }
    }

}*/
