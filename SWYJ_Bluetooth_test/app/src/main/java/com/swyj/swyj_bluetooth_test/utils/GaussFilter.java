package com.swyj.swyj_bluetooth_test.utils;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

//高斯滤波
//传入List<Double> 输出滤波后的List<Double>
public class GaussFilter {
    //private List<Double> value;
    private double averageValue;//平均值
    private double variance;//方差
    private double MSE;//标准差
    private double l_Params; //左阈值
    private double r_Params; //右阈值

    //高斯滤波器初始化
    //l_Params = r_Params = 1 进行MSE规则滤波，l_Params = r_Params = 0.85 常规滤波
    public GaussFilter(double l_Params, double r_Params) {
        this.l_Params = l_Params;
        this.r_Params = r_Params;
    }

    /*public List<Double> getValue() {
        return value;
    }

    public void setValue(List<Double> value) {
        this.value = value;
    }*/

    public double getAverageValue() {
        return averageValue;
    }
    //计算原始数据均值
    public void setAverageValue(List<Double> value) {
        /*double sumValue = 0;
        for (int i = 0;i < value.size();i++) {
            sumValue += value.get(i);
        }
        this.averageValue = sumValue / value.size();*/

        this.averageValue = utils.AverageValue(value);
    }

    public double getVariance() {
        return variance;
    }
    //计算原始数据方差
    public void setVariance(List<Double> value) {
        double num = 0;
        for (int i = 0;i < value.size();i++) {
            num += pow((value.get(i)-averageValue),2);
        }

        this.variance = num / (value.size()-1);
    }

    public double getMSE() {
        return MSE;
    }
    //计算原始数据均方误差
    public void setMSE() {
        this.MSE = sqrt(variance);
    }

    //Gauss Filter
    public List<Double> runGF(List<Double> value) {
        setAverageValue(value);
        setVariance(value);
        setMSE();
        List<Double> newValue = new ArrayList<>();

        for(int i = 0;i < value.size();i++) {
            if (value.get(i) >= (averageValue - MSE*l_Params) && value.get(i) <= (averageValue + MSE*r_Params)) {
                newValue.add(value.get(i));
            }
        }
        return newValue;
    }
}
