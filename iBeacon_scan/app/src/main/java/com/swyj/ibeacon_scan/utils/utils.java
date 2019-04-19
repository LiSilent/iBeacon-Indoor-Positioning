package com.swyj.ibeacon_scan.utils;

import com.swyj.ibeacon_scan.iBeacon.iBeacon;

import java.util.List;

public class utils {

    //求均值
    public static double AverageValue(List<Double> value) {
        double sum = 0;
        for (int i = 0;i < value.size();i++) {
            sum += value.get(i);
        }
        return sum / value.size();
    }

    //对List<double[]> 求均值
    public static double[] AverageValue(List<double[]> value, int type) {
        double[] sum = {0, 0};
        for (int i = 0;i < value.size();i++) {
            sum[0] += value.get(i)[0];
            sum[1] += value.get(i)[1];
        }
        return new double[] {sum[0] / value.size(), sum[1] / value.size()};
    }

    //在List<iBeacon>中查找是否有相同的iBeacon设备
    public static boolean Equal_iBeaconDevice(List<iBeacon> iBeacons , String deviceAddress) {
        for (int i = 0; i <iBeacons.size(); i++) {
            if(iBeacons.get(i).getBluetoothAddress().equals(deviceAddress)) {
                return true;
            }
        }
        return false;
    }

    //将找到的iBeacon设备选中
    public static iBeacon find_iBeacon(List<iBeacon> iBeacons ,String deviceAddress ,int rssi) {

        for (int i = 0; i <iBeacons.size(); i++) {
            if(iBeacons.get(i).getBluetoothAddress().equals(deviceAddress)) {
                iBeacons.get(i).setRssi(rssi);
                iBeacons.get(i).setDistance(rssi);
                return iBeacons.get(i);
            }
        }

        return null;
    }
}
