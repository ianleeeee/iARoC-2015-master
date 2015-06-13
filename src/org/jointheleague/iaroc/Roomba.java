package org.jointheleague.iaroc;

import android.os.SystemClock;

import org.jointheleague.iaroc.sensors.UltraSonicSensors;
import org.wintrisstech.irobot.ioio.IRobotCreateAdapter;
import org.wintrisstech.irobot.ioio.IRobotCreateInterface;

import ioio.lib.api.IOIO;
import ioio.lib.api.exception.ConnectionLostException;

/**
 * Created by Guest on 6/13/15.
 */
public class Roomba extends IRobotCreateAdapter {
    private final Dashboard dashboard;
    public UltraSonicSensors sonar;

    public Roomba(IOIO ioio, IRobotCreateInterface create, Dashboard dashboard)
            throws ConnectionLostException {
        super(create);
        sonar = new UltraSonicSensors(ioio);
        this.dashboard = dashboard;
    }

    private void reverse(int time) throws ConnectionLostException {
        driveDirect(-400, -400);
        SystemClock.sleep(time);
    }

    private void turnLeft(int time) throws ConnectionLostException {
        driveDirect(400, -400);
        SystemClock.sleep(time);
    }

    private void turnRight(int time) throws ConnectionLostException {
        driveDirect(-400, 400);
        SystemClock.sleep(time);
    }

    private void forward() throws ConnectionLostException {
        driveDirect(400, 400);
    }

    public void goldRush() {
        try {
            readSensors(SENSORS_INFRARED_BYTE);
            readSensors(SENSORS_BUMPS_AND_WHEEL_DROPS);
            forward();
            dashboard.log(String.valueOf(getInfraredByte()));
            //check red Buoy
            if (getInfraredByte() == 248) {
                driveDirect(250, 0);
            }
            //check green Buoy
            if (getInfraredByte() == 244) {
                driveDirect(0, 250);
            }
            //check both Buoy
            if (getInfraredByte() == 252) {
                driveDirect(100, 100);
            }
            //dashboard.log(String.valueOf(dashboard.getAzimuth()));


            if (isBumpRight() && isBumpLeft()) {
                reverse(200);
                turnLeft(250);
            } else if (isBumpRight()) {
                reverse(200);
                turnLeft(250);
            } else if (isBumpLeft()) {
                reverse(200);
                turnRight(250);
            }
        } catch(Exception e) {
            dashboard.log("gold rush failed");
            e.printStackTrace();
        }
    }
}


