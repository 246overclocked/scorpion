package org.usfirst.frc.team246.robot.commands;

import org.usfirst.frc.team246.robot.Robot;
import org.usfirst.frc.team246.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class OTSController extends Command {

    public OTSController() {
        requires(Robot.ots);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.ots.enable();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.ots.setSetpoint(RobotMap.OTS_TARGET_RPM);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
