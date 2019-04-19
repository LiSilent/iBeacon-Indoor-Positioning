package com.swyj.swyj_bluetooth_test.positioning;

public class CirIntersect {
    /**
     * 圆A   (x-x1)^2 + (y-y1)^2 = r1^2
     */
    private Circle c1;
    /**
     * 圆B   (x-x2)^2 + (y-y2)^2 = r2^2
     */
    private Circle c2;
    private double x1;
    private double y1;
    private double x2;
    private double y2;
    private double r1;
    private double r2;

    public CirIntersect(Circle C1,Circle C2) {
        c1= C1;
        c2= C2;
        x1=c1.getX();
        y1=c1.getY();
        x2=c2.getX();
        y2=c2.getY();
        r1=c1.getR();
        r2=c2.getR();
    }
    /**
     * 求相交
     * @return {x1 , y1 , x2 , y2}
     */
    public double[] intersect() {

        // 在一元二次方程中 a*x^2+b*x+c=0
        double a,b,c;

        //x的两个根 x_1 , x_2
        //y的两个根 y_1 , y_2
        double x_1 = 0,x_2=0,y_1=0,y_2=0;

        //判别式的值
        double delta = -1;

        //如果 y1!=y2
        if (y1!=y2) {

            //为了方便代入
            double A = (x1*x1 - x2*x2 +y1*y1 - y2*y2 + r2*r2 - r1*r1)/(2*(y1-y2));
            double B = (x1-x2)/(y1-y2);

            a = 1 + B * B;
            b = -2 * (x1 + (A - y1) * B);
            c = x1 * x1 + (A - y1) * (A - y1) - r1 * r1;

            //下面使用判定式 判断是否有解
            delta = b * b - 4 * a * c;

            if (delta > 0) {

                x_1 = (-b + Math.sqrt(b * b - 4 * a * c)) / ( 2 * a);
                x_2 = (-b - Math.sqrt(b * b - 4 * a * c)) / ( 2 * a);
                y_1 = A - B * x_1;
                y_2 = A - B * x_2;
            }
            else if (delta == 0) {

                x_1 = x_2 = -b / (2 * a);
                y_1 = y_2 = A - B * x_1;
            }else {

                System.err.println("两个圆不相交");
                return null;
            }
        }
        else if (x1 != x2) {

            //当y1=y2时，x的两个解相等
            x_1 = x_2 = (x1 * x1 - x2 * x2 + r2 * r2 - r1 * r1) / (2 * (x1 - x2));

            a = 1;
            b = -2 * y1;
            c = y1 * y1 - r1 * r1 + (x_1 - x1) * (x_1 - x1);

            delta = b * b - 4 * a * c;

            if (delta > 0) {

                y_1 = (-b + Math.sqrt(b * b - 4 * a * c)) / (2 * a);
                y_2 = (-b - Math.sqrt( b * b - 4 * a * c)) / (2 * a);
            }
            else if (delta == 0) {

                y_1 = y_2 = -b / (2 * a);
            }else {

                System.err.println("两个圆不相交");
                return null;
            }
        }
        else {

            System.out.println("无解");
            return null;
        }

        return new double[]{x_1, y_1, x_2, y_2};
    }
}
