package org.jointheleague.iaroc;

import android.os.SystemClock;
import android.provider.Settings;

import ioio.lib.api.IOIO;
import ioio.lib.api.exception.ConnectionLostException;

import org.wintrisstech.irobot.ioio.IRobotCreateAdapter;
import org.wintrisstech.irobot.ioio.IRobotCreateInterface;
import org.jointheleague.iaroc.sensors.UltraSonicSensors;

public class Brain extends IRobotCreateAdapter {
    private final Dashboard dashboard;
    public UltraSonicSensors sonar;
    private IOIO ioio;
    private IRobotCreateInterface create;
    private long spinScanTime = 0;
    public Brain(IOIO ioio, IRobotCreateInterface create, Dashboard dashboard)
            throws ConnectionLostException {
        super(create);
        sonar = new UltraSonicSensors(ioio);
        this.dashboard = dashboard;
        this.ioio = ioio;
        this.create = create;

    }

    /* This method is executed when the robot first starts up. */
    public void initialize() throws ConnectionLostException {
        dashboard.log("Hello! I'm a Clever Robot!");
        spinScanTime = System.currentTimeMillis();
        //what would you like me to do, Clever Human?


    }

    /* This method is called repeatedly. */
    public void loop() throws ConnectionLostException {
        dashboard.log("Inside the loop");
        goldRush();
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

    private void spinScan() throws ConnectionLostException {
        int angle = 0;
        readSensors(SENSORS_GROUP_ID6);
        while (angle < 360 && (getInfraredByte() == 255 || getInfraredByte() == 240)) {
            driveDirect(350, -350);
            readSensors(SENSORS_GROUP_ID6);
            angle += getAngle();

        }

    }

    private void forward() throws ConnectionLostException {
        driveDirect(400, 400);
    }

    public void goldRush() {
        try {
            readSensors(SENSORS_INFRARED_BYTE);
            readSensors(SENSORS_BUMPS_AND_WHEEL_DROPS);
            forward();
            if (System.currentTimeMillis() - spinScanTime >= 5000){
                spinScanTime = System.currentTimeMillis();
                spinScan();
            }

            dashboard.log(String.valueOf(getInfraredByte()));
            //check red Buoy
            if (getInfraredByte() == 248)
                driveDirect(250, 0);

            //check green Buoy
            if (getInfraredByte() == 244)
                driveDirect(0, 250);

            //check both Buoy
            if (getInfraredByte() == 252)
                driveDirect(300, 300);

            //check Red Buoy, Green Buoy, and Force Field
            if (getInfraredByte() == 254)
                driveDirect(500, 500);

            //check Green Buoy, and Force Field
            if (getInfraredByte() == 250)
                driveDirect(100, 500);

            //check Red Buoy, and Force Field
            if (getInfraredByte() == 246)
                driveDirect(500, 100);

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
        } catch (Exception e) {
            dashboard.log("gold rush failed");
            e.printStackTrace();
        }
    }

}