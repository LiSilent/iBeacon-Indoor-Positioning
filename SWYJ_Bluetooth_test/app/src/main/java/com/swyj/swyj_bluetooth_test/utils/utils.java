package com.swyj.swyj_bluetooth_test.utils;

import com.swyj.swyj_bluetooth_test.iBeacon.iBeacon;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.NaN;
import static java.lang.Double.isFinite;
import static java.lang.Double.isInfinite;
import static java.lang.Double.isNaN;
import static java.lang.Math.pow;

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

    //得到质心
    public static double[] getCentroid(List<double[]> allCoord) {
        List<Double> coordAll_x = new ArrayList<>();
        List<Double> coordAll_y = new ArrayList<>();

        for (int i = 0;i < allCoord.size();i++) {
            coordAll_x.add(allCoord.get(i)[0]);
            coordAll_y.add(allCoord.get(i)[1]);
        }

        double coord_x = AverageValue(coordAll_x);

        double coord_y = AverageValue(coordAll_y);

        return new double[] {coord_x,coord_y};
    }

    //在List<iBeacon>中查找是否有相同的iBeacon设备
    public static boolean Equal_iBeaconDevice(List<iBeacon> iBeacons ,String deviceAddress) {
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

    // 输出格式转换
    // 保留两位小数two decimals
    public static double ToTwoDecimals(double value) {
        if(isFinite(value) && isNaN(value)) {
            return NaN;
        }
        else {
            BigDecimal newValue = new BigDecimal(value);
            return newValue.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }

    }


    //得到六个点中距离最近的三个点的质心
    public static double[] getResultCoord(List<double[]> coord) {

        double d1 = pow((pow((coord.get(0)[0]-coord.get(1)[0]), 2)+ pow((coord.get(0)[1]-coord.get(1)[1]), 2)) ,0.50); //1、3点距离
        double d2 = pow((pow((coord.get(0)[0]-coord.get(1)[2]), 2)+ pow((coord.get(0)[1]-coord.get(1)[3]), 2)) ,0.50); //1、4点距离
        double d3 = pow((pow((coord.get(0)[2]-coord.get(1)[0]), 2)+ pow((coord.get(0)[3]-coord.get(1)[1]), 2)) ,0.50); //2、3点距离
        double d4 = pow((pow((coord.get(0)[2]-coord.get(1)[2]), 2)+ pow((coord.get(0)[3]-coord.get(1)[3]), 2)) ,0.50); //2、4点距离
        double d5 = pow((pow((coord.get(1)[0]-coord.get(2)[0]), 2)+ pow((coord.get(1)[1]-coord.get(2)[1]), 2)) ,0.50); //3、5点距离
        double d6 = pow((pow((coord.get(1)[0]-coord.get(2)[2]), 2)+ pow((coord.get(1)[1]-coord.get(2)[3]), 2)) ,0.50); //3、6点距离
        double d7 = pow((pow((coord.get(1)[2]-coord.get(2)[0]), 2)+ pow((coord.get(1)[3]-coord.get(2)[1]), 2)) ,0.50); //4、5点距离
        double d8 = pow((pow((coord.get(1)[2]-coord.get(2)[2]), 2)+ pow((coord.get(1)[3]-coord.get(2)[3]), 2)) ,0.50); //4、6点距离

        List<double[]> resultCoord = new ArrayList<>();
        //判断点1离点3近还是点4近
        if (d1 < d2) {
            //判断点3离点1近还是点2近
            if (d1 < d3) {
                if (d5 < d6) {
                    resultCoord.add(new double[] {coord.get(0)[0], coord.get(0)[1]}); //1、3、5
                    resultCoord.add(new double[] {coord.get(1)[0], coord.get(1)[1]});
                    resultCoord.add(new double[] {coord.get(2)[0], coord.get(2)[1]});
                } else {
                    resultCoord.add(new double[] {coord.get(0)[0], coord.get(0)[1]}); //1、3、6
                    resultCoord.add(new double[] {coord.get(1)[0], coord.get(1)[1]});
                    resultCoord.add(new double[] {coord.get(2)[2], coord.get(2)[3]});
                }

            } else {
                if (d5 < d6) {
                    resultCoord.add(new double[] {coord.get(0)[2], coord.get(0)[3]}); //2、3、5
                    resultCoord.add(new double[] {coord.get(1)[0], coord.get(1)[1]});
                    resultCoord.add(new double[] {coord.get(2)[0], coord.get(2)[1]});
                } else {
                    resultCoord.add(new double[] {coord.get(0)[2], coord.get(0)[3]}); //2、3、6
                    resultCoord.add(new double[] {coord.get(1)[0], coord.get(1)[1]});
                    resultCoord.add(new double[] {coord.get(2)[2], coord.get(2)[3]});
                }
            }

        } else {
            //判断点4离点1近还是点2近
            if (d2 < d4) {
                if (d7 < d8) {
                    resultCoord.add(new double[] {coord.get(0)[0], coord.get(0)[1]}); //1、4、5
                    resultCoord.add(new double[] {coord.get(1)[2], coord.get(1)[3]});
                    resultCoord.add(new double[] {coord.get(2)[0], coord.get(2)[1]});
                } else {
                    resultCoord.add(new double[] {coord.get(0)[0], coord.get(0)[1]}); //1、4、6
                    resultCoord.add(new double[] {coord.get(1)[2], coord.get(1)[3]});
                    resultCoord.add(new double[] {coord.get(2)[2], coord.get(2)[3]});
                }
            } else {
                if (d7 < d8) {
                    resultCoord.add(new double[] {coord.get(0)[2], coord.get(0)[3]}); //2、4、5
                    resultCoord.add(new double[] {coord.get(1)[2], coord.get(1)[3]});
                    resultCoord.add(new double[] {coord.get(2)[0], coord.get(2)[1]});
                } else {
                    resultCoord.add(new double[] {coord.get(0)[2], coord.get(0)[3]}); //2、4、6
                    resultCoord.add(new double[] {coord.get(1)[2], coord.get(1)[3]});
                    resultCoord.add(new double[] {coord.get(2)[2], coord.get(2)[3]});
                }
            }
        }



        return getCentroid(resultCoord);
    }

}
