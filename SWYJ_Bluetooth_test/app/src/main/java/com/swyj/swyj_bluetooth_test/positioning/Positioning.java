package com.swyj.swyj_bluetooth_test.positioning;

import com.swyj.swyj_bluetooth_test.iBeacon.iBeacon;
import com.swyj.swyj_bluetooth_test.utils.Kalman;
import com.swyj.swyj_bluetooth_test.utils.utils;

import java.util.ArrayList;
import java.util.List;

import Jama.Matrix;

import static com.swyj.swyj_bluetooth_test.positioning.Combination.findsort;
import static com.swyj.swyj_bluetooth_test.positioning.ThreeCircleCentroid.getThreeCircleCentroid;

public class Positioning {

    //坐标卡尔曼滤波参数
    private static Matrix coord_A = new Matrix(new double[][] {{1,0,1,0},{0,1,0,1},{0,0,1,0},{0,0,0,1}}); //协方差阵 不需要改动
    private static Matrix coord_Q = new Matrix(new double[][] {{0.001,0,0,0},{0,0.001,0,0},{0,0,0.001,0},{0,0,0,0.001}}); //系统误差
    private static Matrix coord_R = new Matrix(new double[][] {{0.2,0},{0,0.2}}); //噪声误差
    private static Matrix coord_H = new Matrix(new double[][] {{1,0,0,0},{0,1,0,0}}); //转换矩阵 不需要改动
    private static Matrix coord_I = new Matrix(new double[][] {{1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1}}); //单模型单测量状态 I=1 不需要改动
    private static Matrix coord_u = new Matrix(4,4); //蓝牙定位中系统控制量应为0
    private static Matrix coord_B = new Matrix(4,4); //蓝牙定位中参数B应为0

    //RSSI卡尔曼滤波器初始化
    private static Kalman RSSI_Kalman = new Kalman(0.001,0.1);
    //坐标卡尔曼滤波器器初始化
    private static Kalman coord_Kalman = new Kalman(coord_A,coord_Q,coord_R,coord_H,coord_I,coord_u,coord_B);
    //最终坐标
    private static double[] resultCoord;
    //运行定位
    public static double[] runPositioning(List<iBeacon> iBeacons) {

        List<iBeacon> new_iBeacons = new ArrayList<>();
        //筛选得到可用iBeacon设备
        for (int i = 0; i < iBeacons.size(); i++){
            List<Double> New_RSSI = RSSI_Kalman.RSSI_KalmanFilter(iBeacons.get(i).getRSSI_Value());

            double ave_RSSI = utils.AverageValue(New_RSSI);
            //double ave_RSSI = iBeacons.get(i).getRssi();
            iBeacons.get(i).setDistance(ave_RSSI);

            if(iBeacons.get(i).getDistance() < 10) {
                new_iBeacons.add(iBeacons.get(i));
            }
        }

        List<double[]> centroidCoords = new ArrayList<>();
        //从可用的iBeacon设备选出3个进行排列组合
        List<List<iBeacon>> result = findsort(new_iBeacons, 3);

        /*for (int i = 0;i < result.size(); i++){
            for (int j = 0;j < result.get(i).size(); j++) {
                System.out.println(result.get(i).get(j).getBluetoothAddress());
            }

            System.out.println("----------------------------");
        }*/

       // System.out.println("result.size()" + result.size());

        //得到每三个iBeacon设备之间的质心
        for (int i = 0; i < result.size(); i++) {
            centroidCoords.add(getThreeCircleCentroid(result.get(i)));
        }

/*        for (int i = 0;i < centroidCoords.size(); i++){

            System.out.println(centroidCoords.get(i)[0] + centroidCoords.get(i)[1]);


            System.out.println("----------------------------");
        }*/

        //将得到的坐标进行卡尔曼滤波
        List<double[]> resultCoords = coord_Kalman.Coord_KalmanFilter(centroidCoords);
        //得到最终坐标
        resultCoord = utils.AverageValue(resultCoords,1);

        //System.out.println(" x:" + resultCoord[0] + " y:" + resultCoord[1]);

        return resultCoord;
    }

}
