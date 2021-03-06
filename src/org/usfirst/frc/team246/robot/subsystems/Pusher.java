package org.usfirst.frc.team246.robot.subsystems;

import org.usfirst.frc.team246.robot.Robot;
import org.usfirst.frc.team246.robot.RobotMap;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 *
 */
public class Pusher extends PIDSubsystem {

    // Initialize your subsystem here
    public Pusher() {
        super(RobotMap.PUSHER_kP, RobotMap.PUSHER_kI, RobotMap.PUSHER_kD, RobotMap.PUSHER_kF, .02);
        this.setAbsoluteTolerance(.01);
        LiveWindow.addActuator("Pusher", "pusherPID", this.getPIDController());
    }
    
    public void initDefaultCommand() {
    }
    
    protected double returnPIDInput() {
    	if(Robot.trojan)
    	{
    		return 0;
    	}
    	else
    	{
    		return RobotMap.pusherPot.get();
    	}
    }
    
    protected void usePIDOutput(double output) {
        RobotMap.pusherMotor.set(output);
    }
}
