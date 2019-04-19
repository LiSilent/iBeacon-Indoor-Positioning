package com.swyj.ibeacon_scan.iBeacon;

import java.util.ArrayList;
import java.util.List;

public class iBeaconDevice {
    private static List<iBeacon> installed_iBeacons = new ArrayList<>();

    public static List<iBeacon> getInstalled_iBeacons() {

        //已安装好的iBeacon
        /*iBeacon iBeacon_1 = new iBeacon("19:18:FC:09:E3:E3",0,0);
        iBeacon iBeacon_2 = new iBeacon("19:18:FC:09:E4:94",0,4);
        iBeacon iBeacon_3 = new iBeacon("19:18:FC:09:E4:9F",4,0);
        iBeacon iBeacon_4 = new iBeacon("19:18:FC:09:E3:BB",4,4);*/
        iBeacon iBeacon_5 = new iBeacon("19:18:FC:09:E4:98",1,2);

        /*installed_iBeacons.add(iBeacon_1);
        installed_iBeacons.add(iBeacon_2);
        installed_iBeacons.add(iBeacon_3);
        installed_iBeacons.add(iBeacon_4);*/
        installed_iBeacons.add(iBeacon_5);

        return installed_iBeacons;
    }

    public void setiBeaconDevices(List<iBeacon> iBeacons) {

        this.installed_iBeacons = iBeacons;
    }
}
