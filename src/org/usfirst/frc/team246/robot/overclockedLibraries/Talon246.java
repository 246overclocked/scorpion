package org.usfirst.frc.team246.robot.overclockedLibraries;

import edu.wpi.first.wpilibj.Talon;

public class Talon246 extends Talon{
	
	boolean overridden = false;
    
    public Talon246(final int channel)
    {
        super(channel);
    }
    
    public void set(double speed)
    {
        if(!overridden) super.set(speed);
    }
    
    /**
     * sets the speed of the motor, and disables the set command
     * @param speed the speed which the motor should move at
     */
    public void overridingSet(double speed)
    {
        overridden = true;
        super.set(speed);
    }
    
    /**
     * enables the set command after overridingSet has been called
     */
    public void returnControl()
    {
        overridden = false;
    }
}