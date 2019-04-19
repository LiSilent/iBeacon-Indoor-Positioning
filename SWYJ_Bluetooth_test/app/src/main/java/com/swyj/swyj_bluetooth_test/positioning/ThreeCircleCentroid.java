package com.swyj.swyj_bluetooth_test.positioning;

import com.swyj.swyj_bluetooth_test.iBeacon.iBeacon;
import com.swyj.swyj_bluetooth_test.utils.utils;

import java.util.ArrayList;
import java.util.List;

import static com.swyj.swyj_bluetooth_test.utils.utils.getResultCoord;

public class ThreeCircleCentroid {

    public static double[] getThreeCircleCentroid(List<iBeacon> iBeacons) {

        TwoCircleStrategy twoCircleStrategy = new TwoCircleStrategy();
        List<double[]> strategyCoord = new ArrayList<>();

        for (int i = 0; i < iBeacons.size(); i++) {

            /*System.out.println(iBeacons.get(i).getCoord()[0] + " " + iBeacons.get(i).getCoord()[1]);
            System.out.println("----------------------------");*/
            for(int j = i + 1; j < iBeacons.size() - i; j++) {
                //六点质心法
                //strategyCoord.addAll(twoCircleStrategy.GetCalculateCoords_SP(iBeacons.get(i).getCoord(), iBeacons.get(j).getCoord(),iBeacons.get(i).getDistance(),iBeacons.get(j).getDistance()));

                //采用三点法
                strategyCoord.add(twoCircleStrategy.GetCalculateCoords_TP(iBeacons.get(i).getCoord(), iBeacons.get(j).getCoord(),iBeacons.get(i).getDistance(),iBeacons.get(j).getDistance()));

                //最近三交点质心法
                //strategyCoord.add(twoCircleStrategy.GetNodeCoord(iBeacons.get(i).getCoord(), iBeacons.get(j).getCoord(),iBeacons.get(i).getDistance(),iBeacons.get(j).getDistance()));
            }
        }

        //return getResultCoord(strategyCoord);
        return utils.getCentroid(strategyCoord);
    }
}
