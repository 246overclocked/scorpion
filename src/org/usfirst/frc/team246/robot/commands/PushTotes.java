package org.usfirst.frc.team246.robot.commands;

import org.usfirst.frc.team246.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class PushTotes extends Command {

    public PushTotes() {
        requires(Robot.pusher);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.pusher.enable();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.pusher.setSetpoint(1);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Robot.pusher.onTarget();
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
