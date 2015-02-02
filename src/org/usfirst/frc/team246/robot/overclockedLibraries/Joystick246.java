package org.usfirst.frc.team246.robot.overclockedLibraries;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import edu.wpi.first.wpilibj.Joystick;

/**
 *
 * @author Paul
 */
public class Joystick246 extends Joystick{
    
    double deadband = .1; // If the magnitude of the joystick is less than this value, it will be considered at 0

    public Joystick246(final int port) {
        super(port);
    }
    
    /**
     * Set the deadband value
     * 
     * @param deadband If the magnitude of the joystick is less than this value, it will be considered at 0
     */
    public void setDeadband(double deadband)
    {
        this.deadband = deadband;
    }
    
    /**
     * Get the deadband value
     * 
     * @return deadband If the magnitude of the joystick is less than this value, it will be considered at 0
     */
    public double getDeadband()
    {
        return deadband;
    }
    
    /**
     * Get the value of the axis.
     *
     * @param axis The axis to read [1-6].
     * @return The value of the axis.
     */
    public double getRawAxis(final int axis) {
        double val = super.getRawAxis(axis);
        if(Math.abs(val) <= deadband) return 0;
        else return val;
    }
    
    @Override
    public double getDirectionRadians()
    {
    	return -super.getDirectionRadians();
    }
}
