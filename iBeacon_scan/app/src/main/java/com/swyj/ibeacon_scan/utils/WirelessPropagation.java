package com.swyj.ibeacon_scan.utils;

import java.util.List;

import static java.lang.Math.pow;

//一般para_n在1.5-4之间，取2左右效果较好
public class WirelessPropagation {

    //传入滤波后的RSSI值、iBeacon的txPower 和环境因子para_n
    public static double getDistance(List<Double> RSSI_Value, double txPower, double para_n) {

        double sumValue = 0;
        for(int i = 0;i < RSSI_Value.size();i++) {
            sumValue += RSSI_Value.get(i);
        }
        double averageValue = sumValue/RSSI_Value.size();

        //double averageValue = utils.AverageValue(RSSI_Value);
        if (0 == averageValue) {
            return -1;
        } else {
            return pow(10, (txPower - averageValue) / (10 * para_n));
        }
    }
    //传入单个RSSI值
    public static double getDistance(double RSSI_Value, double txPower, double para_n) {

        if (0 == RSSI_Value) {
            return -1;
        } else {
            return pow(10, (txPower - RSSI_Value) / (10 * para_n));
        }
    }
}
