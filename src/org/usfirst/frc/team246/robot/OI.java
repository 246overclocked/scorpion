package org.usfirst.frc.team246.robot;

import org.usfirst.frc.team246.robot.overclockedLibraries.Joystick246;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    
	public Joystick246 driverLeftJoystick;
    public Joystick246 driverRightJoystick;
    public Joystick246 operatorJoystick;
    
    public OI()
    {
        driverLeftJoystick = new Joystick246(1);
        driverLeftJoystick.setDeadband(.1);
        driverRightJoystick = new Joystick246(2);
        driverRightJoystick.setDeadband(.1);
        operatorJoystick = new Joystick246(3);
        operatorJoystick.setDeadband(.1);
    }
}

