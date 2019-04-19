package com.swyj.swyj_bluetooth_test.iBeacon;

import com.swyj.swyj_bluetooth_test.utils.WirelessPropagation;
import com.swyj.swyj_bluetooth_test.utils.utils;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.log10;

public class iBeacon implements Comparable<iBeacon> {

    //是否被用户扫描到
    private boolean discover = false;

    //无线电衰减模型参数
    private double txPower = -58;      //距离该结点1米处的RSSI值
    private double para_n = 1.5;
    //iBeacon设备自身所带参数
    private List<Double> RSSI_Value = new ArrayList<>(); //RSSI
    private int rssi;//单次扫描得到的rssi
    private String name;//名称
    private String proximityUuid;    //uuid
    private String bluetoothAddress; //address

    //预留自定义信息参数,用于扩展功能
    private int major;
    private int minor;

    private double distance;         //到该iBeacon设备的距离

    private double[] coord = new double[2]; //设置坐标

    public iBeacon() {

    }

    //布置的设备初始化器
    public iBeacon(String bluetoothAddress, double x, double y) {
        this.bluetoothAddress = bluetoothAddress;
        this.coord[0] = x;
        this.coord[1] = y;
    }
    public iBeacon(String bluetoothAddress, int rssi) {
        this.bluetoothAddress = bluetoothAddress;
        this.rssi = rssi;
        setDistance(rssi);
        setRSSI_Value();
    }

    //扫描到的设备初始化器
    public iBeacon(String bluetoothAddress) {
        this.bluetoothAddress = bluetoothAddress;
        //this.rssi = rssi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTxPower() {
        return txPower;
    }

    public void setTxPower(double txPower) {
        this.txPower = txPower;
    }

    public boolean isDiscover() {
        return discover;
    }

    public void setDiscover(boolean discover) {
        this.discover = discover;
    }

    public void setRSSI_Value(List<Double> RSSI_Value) {
        this.RSSI_Value = RSSI_Value;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public String getBluetoothAddress() {
        return bluetoothAddress;
    }

    public void setBluetoothAddress(String bluetoothAddress) {
        this.bluetoothAddress = bluetoothAddress;
    }

    public String getProximityUuid() {
        return proximityUuid;
    }

    public void setProximityUuid(String proximityUuid) {
        this.proximityUuid = proximityUuid;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }
/*封装txPower、和环境参数n
    public double getTxPower() {
        return txPower;
    }

    public double getPara_n() {
        return para_n;
    }*/

    //设置安装好的好的iBeacon的A和n
    public void setPara(List<Double> RSSI_Value, List<Double> Dist_Value) {

        //Rho = (-10)*lg(d)
        List<Double> Rho_Value = new ArrayList<>();

        for (int i = 0; i < Dist_Value.size();i++) {
            Rho_Value.set(i, (-10)*log10(Dist_Value.get(i)));
        }

        //得到均值
        double averageRSSI = utils.AverageValue(RSSI_Value);
        double averageRho = utils.AverageValue(Rho_Value);

        for (int i = 0;i < Rho_Value.size();i++) {
            this.para_n += RSSI_Value.get(i) / (Rho_Value.get(i) - averageRho);
        }

        this.txPower = averageRSSI - this.para_n * averageRho;
    }


    public double getDistance() {
        return distance;
    }

    public void setDistance(List<Double> RSSI_Value) {
        this.distance = WirelessPropagation.getDistance(RSSI_Value,txPower,para_n);
    }
    public void setDistance(double RSSI_Value) {
        this.distance = WirelessPropagation.getDistance(RSSI_Value,txPower,para_n);
    }

    public List<Double> getRSSI_Value() {
        return RSSI_Value;
    }

    public void setRSSI_Value() {
        this.RSSI_Value.add((double)rssi);
    }
    /*public void setRSSI_Value(List <Double> rssiValue) {
        this.RSSI_Value = rssiValue;
    }*/
    public void setRSSI_Value(int rssi) {
        this.RSSI_Value.add((double)rssi);
    }

    //设置安装好的好的iBeacon的坐标X和Y
    public double[] getCoord() {
        return coord;
    }

    public void setCoord(double[] coord) {
        this.coord = coord;
    }

    //重写compareTo，使用地址进行排序，用于排列组合时删除重复情况
    @Override
    public int compareTo(iBeacon o) {

        // TODO Auto-generated method stub

        return bluetoothAddress.compareTo(o.bluetoothAddress);

    }

}
