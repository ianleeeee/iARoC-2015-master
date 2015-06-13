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

        //what would you like me to do, Clever Human?


    }

    /* This method is called repeatedly. */
    public void loop() throws ConnectionLostException {
        dashboard.log("Inside the loop");
        Roomba roomba = new Roomba(ioio, create, dashboard);
        roomba.goldRush();
    }


}