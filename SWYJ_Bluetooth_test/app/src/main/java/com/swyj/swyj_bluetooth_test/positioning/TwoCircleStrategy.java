package com.swyj.swyj_bluetooth_test.positioning;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

//传入两个圆心坐标，传入半径，计算得到策略点坐标
public class TwoCircleStrategy {

    //圆心坐标
    private double COM_x1;
    private double COM_y1;
    private double COM_x2;
    private double COM_y2;
    //两圆半径
    private double radius_1;
    private double radius_2;
    //两圆距离
    private double distance;

/*    public double getCOM_x1() {
        return COM_x1;
    }*/

    public void setPara(double[] firstPoint,double[] secondPoint,double firstValue, double secondValue) {
        this.COM_x1 = firstPoint[0];
        this.COM_y1 = firstPoint[1];
        this.COM_x2 = secondPoint[0];
        this.COM_y2 =  secondPoint[1];
        this.radius_1 = firstValue;
        this.radius_2 = secondValue;
        this.distance = pow((pow((COM_x1-COM_x2), 2)+ pow((COM_y1-COM_y2), 2)) ,0.50);
    }

    public double getCOM_y1() {
        return COM_y1;
    }

/*    public void setCOM_y1(double[] pointCoord) {
        this.COM_y1 = pointCoord[1];
    }*/

    public double getCOM_x2() {
        return COM_x2;
    }

/*    public void setCOM_x2(double[] pointCoord) {
        this.COM_x2 = pointCoord[0];
    }*/

    public double getCOM_y2() {
        return COM_y2;
    }

/*    public void setCOM_y2(double[] pointCoord) {
        this.COM_y2 =  pointCoord[1];
    }*/

    public double getRadius_1() {
        return radius_1;
    }

/*    public void setRadius_1(double radius_1) {
        this.radius_1 = radius_1;
    }*/

    public double getRadius_2() {
        return radius_2;
    }

/*    public void setRadius_2(double radius_2) {
        this.radius_2 = radius_2;
    }*/

    public double getDistance() {
        return distance;
    }

/*    public void setDistance(double[] firstPoint,double[] secondPoint,double firstValue, double secondValue) {
        setPara(firstPoint,secondPoint,firstValue,secondValue);
        this.distance = pow((pow((COM_x1-COM_x2), 2)+ pow((COM_y1-COM_y2), 2)) ,0.50);
    }*/

    //六点法 返回两个点坐标
    public List<double[]> GetCalculateCoords_SP(double[] firstPoint, double[] secondPoint, double firstValue, double secondValue) {
        setPara(firstPoint,secondPoint,firstValue,secondValue);
        double proportion_1 = (distance-radius_2) / distance;
        double proportion_2 = radius_1 / distance;
        double[] coord_1 = new double[] {(COM_x2-COM_x1)*proportion_1+COM_x1,(COM_y2-COM_y1)*proportion_1+COM_y1};
        double[] coord_2 = new double[] {(COM_x2-COM_x1)*proportion_2+COM_x1,(COM_y2-COM_y1)*proportion_2+COM_y1};

        List<double[]> coord = new ArrayList<>();
        coord.add(coord_1);
        coord.add(coord_2);
        return coord;
    }

    //三点法 返回一个点坐标
    public double[] GetCalculateCoords_TP(double[] firstPoint, double[] secondPoint, double firstValue, double secondValue) {

        setPara(firstPoint,secondPoint,firstValue,secondValue);
        //strategyDistance为d - (r1 + r2)
        //strategyDistance < 0 两圆相离
        //strategyDistance > 0 两圆相交
        double strategyDistance = radius_1 + radius_2 - distance;
        //构造相交三角形
        double newDistance = radius_1 + radius_2 - abs(strategyDistance);
        //求比例
        double p = 0.5 * (radius_1 + radius_2 + newDistance);
        double h = 4 * p * (p - radius_1) * (p - radius_2) * (p - newDistance) / pow(newDistance, 2);
        double a = pow((pow(radius_1, 2) - h), 0.5);
        double b = pow((pow(radius_2, 2) - h), 0.5);
        double proportion = 1; //初始化
        if(strategyDistance > 0) {
            proportion = (radius_1 - b) / distance;
        }else {
            proportion = (radius_1 + a) / distance;
        }
        //计算坐标
        double[] coord = new double[] {(COM_x2 - COM_x1) * proportion + COM_x1,(COM_y2-COM_y1) * proportion + COM_y1};

        return coord;
    }
    //求交点法 返回两个交点坐标
    public double[] GetNodeCoord(double[] firstPoint,double[] secondPoint,double firstValue,double secondValue) {

        Circle circle1 = new Circle(firstPoint[0],firstPoint[1],firstValue);

        Circle circle2 = new Circle(secondPoint[0],secondPoint[1],secondValue);

        CirIntersect mCirIntrsect = new CirIntersect(circle1,circle2);

        return mCirIntrsect.intersect();
    }
}
