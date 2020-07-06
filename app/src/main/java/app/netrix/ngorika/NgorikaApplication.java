package app.netrix.ngorika;

import android.app.Application;

import app.netrix.ngorika.bluetooth.NgBluetooth;

/**
 * Created by chrissie on 12/11/15.
 */
public class NgorikaApplication extends Application {

    public NgBluetooth mapsBluetooth;

    @Override
    public void onCreate() {
        super.onCreate();
        mapsBluetooth = new NgBluetooth();
    }
}
