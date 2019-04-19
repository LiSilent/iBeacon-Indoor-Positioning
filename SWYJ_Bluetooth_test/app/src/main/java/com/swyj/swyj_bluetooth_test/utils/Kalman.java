package com.swyj.swyj_bluetooth_test.utils;

import java.util.ArrayList;
import java.util.List;

import Jama.Matrix;
//Jama包提供matrix相关操作
//matrix初始化：Matrix(double[][] A)
//            Construct a matrix from a 2-D array.
//            Matrix(double[][] A, int m, int n)
//            Construct a matrix quickly without checking arguments.
//            Matrix(double[] vals, int m)
//            Construct a matrix from a one-dimensional packed array
//            Matrix(int m, int n)
//            Construct an m-by-n matrix of zeros.
//            Matrix(int m, int n, double s)
//            Construct an m-by-n constant matrix.
//            求反 inverse()
//            倒置 transpose()


//使用时先new一个Kalman对象，并对Kalman参数进行初始化，用高斯滤波值对初始值进行赋值
//RSSI值滤波时传入List<Double> ,滤波结果放入List<Double> 中
//坐标滤波时传入List<double []> ,滤波结果放入List<double []> 中
public class Kalman {

    //蓝牙模块中不需要u和B
    private Matrix params_u;//系统控制量
    private Matrix params_B;//参数B

    private Matrix params_A;//协方差阵  参数A
    private Matrix params_Q;//系统噪声
    private Matrix params_R;//环境噪声
    private Matrix params_H;//转换矩阵
    private Matrix params_I;//单模型单测量状态 I=1

    private Matrix params_z;//观测值z
    private Matrix kalmanGain;//卡尔曼增益
    //高斯滤波器
    private GaussFilter gs_MSE = new GaussFilter(1,1);
    private GaussFilter gs_FT = new GaussFilter(0.85,0.85);

    //用构造函数进行初始化
    //RSSI滤波参数初始化
    public Kalman(double Q, double R) {
        params_A = new Matrix(1,1,1);
        params_H = new Matrix(1,1,1);
        params_Q = new Matrix(1,1, Q); //Q一般设为0.001
        params_R = new Matrix(1,1, R); //R一般设为0.1到0.5
        params_I = new Matrix(1,1,1);

    }
    //坐标滤波参数初始化
    public Kalman(Matrix params_A, Matrix params_Q, Matrix params_R, Matrix params_H, Matrix params_I, Matrix params_B, Matrix params_u ){
        this.params_A = params_A;
        this.params_Q = params_Q;
        this.params_R = params_R;
        this.params_H = params_H;
        this.params_I = params_I;
        this.params_u = params_u;
        this.params_B = params_B;
    }

    //对RSSI值进行滤波
    //传入一组RSSI值
    public List<Double> RSSI_KalmanFilter(List<Double> RSSI_value) {

        Matrix params_P = new Matrix(1,1,1);
        Matrix params_x = new Matrix(1,1,utils.AverageValue(gs_MSE.runGF(gs_FT.runGF(RSSI_value))));
        List<Double> RSSI = new ArrayList<>();
        for (int i = 0; i < RSSI_value.size(); i++){

            //预测方程
            params_x = params_A.times(params_x);
            params_P = params_A.times(params_P).times((params_A.transpose())).plus(params_Q);
            //params_P.print(1,3);
            //更新方程
            params_z = new Matrix(1,1,RSSI_value.get(i)); //观测值
            kalmanGain = params_P.times(params_H.transpose()).times((params_H.times(params_P).times(params_H.transpose()).plus(params_R)).inverse());

            params_x = params_x.plus(kalmanGain.times(params_z.minus(params_H.times(params_x))));
            //params_x.print(1,3);
            params_P = (params_I.minus(kalmanGain.times(params_H))).times(params_P);
            //params_P = (params_P.minus(kalmanGain.times(params_H).times(params_P)));

            RSSI.add(params_x.get(0,0));
        }

        return RSSI;
    }


    //坐标滤波
    //传入一组坐标值
    public List<double[]> Coord_KalmanFilter(List<double[]> coordValue) {

        List<double[]> new_Coord = new ArrayList<>();

        //初始化
        Matrix params_P = new Matrix(4,4);
        Matrix params_x = new Matrix(new double[][] {{utils.AverageValue(coordValue,1)[0]},{utils.AverageValue(coordValue,1)[1]},{0},{0}});//用均值作为初始化条件
        for (int i = 0; i < coordValue.size(); i++){

            //预测方程
            params_x = params_A.times(params_x);
            params_P = params_A.times(params_P).times((params_A.transpose())).plus(params_Q);
            //更新方程
            params_z = new Matrix(new double[][] {{coordValue.get(i)[0]}, {coordValue.get(i)[1]}}); //观测值
            kalmanGain = params_P.times(params_H.transpose()).times((params_H.times(params_P).times(params_H.transpose()).plus(params_R)).inverse());

            params_x = params_x.plus(kalmanGain.times(params_z.minus(params_H.times(params_x))));
            //params_x.print(4,4);
            new_Coord.add(new double[]{params_x.get(0,0), params_x.get(1,0)});
            params_P = (params_I.minus(kalmanGain.times(params_H))).times(params_P);
            //params_P = (params_P.minus(kalmanGain.times(params_H).times(params_P)));
        }
        return new_Coord;

    }

}