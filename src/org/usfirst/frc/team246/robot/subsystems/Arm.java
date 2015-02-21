package org.usfirst.frc.team246.robot.subsystems;

import org.usfirst.frc.team246.robot.Robot;
import org.usfirst.frc.team246.robot.RobotMap;
import org.usfirst.frc.team246.robot.RobotMap.ArmSetpoints;
import org.usfirst.frc.team246.robot.commands.ManualArm;
import org.usfirst.frc.team246.robot.overclockedLibraries.AlertMessage;
import org.usfirst.frc.team246.robot.overclockedLibraries.UdpAlertService;
import org.usfirst.frc.team246.robot.overclockedLibraries.Vector2D;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 *
 */
public class Arm extends Subsystem {
	
	public PIDController shoulder;
	public PIDController elbow;
	public PIDController wrist;
	
	public boolean fallenCanMode = false;
	
	public ArmSetpoints currentSetpoint;

    // Initialize your subsystem here
    public Arm() {
    	if(!Robot.trojan)
    	{
	        shoulder = new PIDController(RobotMap.ARM_SHOULDER_kP, RobotMap.ARM_SHOULDER_kI, RobotMap.ARM_SHOULDER_kD, RobotMap.armShoulderPot, RobotMap.armShoulderMotor, .02);
	        shoulder.setInputRange(RobotMap.ARM_SHOULDER_MIN, RobotMap.ARM_SHOULDER_MAX);
	        shoulder.setAbsoluteTolerance(5);
	        elbow = new PIDController(RobotMap.ARM_ELBOW_kP, RobotMap.ARM_ELBOW_kI, RobotMap.ARM_ELBOW_kD, RobotMap.armElbowPot, RobotMap.armElbowMotor, .02);
	        elbow.setInputRange(-180, 180);
	        shoulder.setAbsoluteTolerance(5);
	        wrist = new PIDController(RobotMap.ARM_WRIST_kP, RobotMap.ARM_WRIST_kI, RobotMap.ARM_WRIST_kD, RobotMap.armWristPot, RobotMap.armWristMotor, .02);
	        wrist.setInputRange(-180, 180);
	        shoulder.setAbsoluteTolerance(5);
	        
	        LiveWindow.addActuator("Arm", "shoulder", shoulder);
	        LiveWindow.addActuator("Arm", "elbow", elbow);
	        LiveWindow.addActuator("Arm", "wrist", wrist);
    	}
    }
    
    public void initDefaultCommand() {
    	if(!Robot.trojan)
    	{
    		setDefaultCommand(new ManualArm());
    	}
    }
    
    //The location of the arm is described, in inches, by the vector between the shoulder joint and the wrist joint
    //All angles are defined relative to the world with 0 being up
    public void set(double shoulderAngle, double elbowAngle, double wristAngle)
    {	
    	//Define vectors for arm segments
    	Vector2D v1 = new Vector2D(false, RobotMap.ARM_SEGMENT_1_LENGTH, shoulderAngle); //Between Shoulder and Elbow
    	Vector2D v2 = new Vector2D(false, RobotMap.ARM_SEGMENT_2_LENGTH, elbowAngle); //Between Elbow and Wrist
    	Vector2D v3 = new Vector2D(false, RobotMap.GRABBER_LENGTH, wristAngle); //From Wrist to the end of the arm
    	Vector2D v12 = Vector2D.addVectors(v1, v2); //Between Shoulder and Wrist
    	Vector2D v123 = Vector2D.addVectors(v12, v3); // From Shoulder to end of arm
    	
    	//Limit the angle of the shoulder
    	if(v1.getAngle() > RobotMap.ARM_SHOULDER_MAX)       
		{
    		UdpAlertService.sendAlert(new AlertMessage("Arm Constraint: Shoulder Max Angle"));
    		return;
		}
    	if(v1.getAngle() < RobotMap.ARM_SHOULDER_MIN)
		{
    		UdpAlertService.sendAlert(new AlertMessage("Arm Constraint: Shoulder Min Angle"));
    		return;
		}
    	
    	//Stop our turnbuckles from hitting any sprockets
    	if(v2.getAngle() - v1.getAngle() < RobotMap.ARM_TURNBUCKLE_SHOULDER_ELBOW_MIN) 
		{
    		UdpAlertService.sendAlert(new AlertMessage("Arm Constraint: Shoulder Elbow Min"));
    		return;
		}
    	if(v2.getAngle() - v1.getAngle() > RobotMap.ARM_TURNBUCKLE_SHOULDER_ELBOW_MAX)  // Too generous
		{
    		UdpAlertService.sendAlert(new AlertMessage("Arm Constraint: Shoulder Elbow Max"));
    		return;
		}
    	if(v3.getAngle() - v1.getAngle() < RobotMap.ARM_TURNBUCKLE_SHOULDER_WRIST_MIN) 
		{
    		UdpAlertService.sendAlert(new AlertMessage("Arm Constraint: Shoulder Wrist Min"));
    		return;
		}
    	if(v3.getAngle() - v1.getAngle() > RobotMap.ARM_TURNBUCKLE_SHOULDER_WRIST_MAX) 
		{
    		UdpAlertService.sendAlert(new AlertMessage("Arm Constraint: Shoulder Wrist Max"));
    		return;
		}
    	if(v3.getAngle() - v2.getAngle() < RobotMap.ARM_TURNBUCKLE_ELBOW_WRIST_MIN) 
		{
    		UdpAlertService.sendAlert(new AlertMessage("Arm Constraint: Elbow Wrist Min"));
    		return;
		}
    	if(v3.getAngle() - v2.getAngle() > RobotMap.ARM_TURNBUCKLE_ELBOW_WRIST_MAX) 
		{
    		UdpAlertService.sendAlert(new AlertMessage("Arm Constraint: Elbow Wrist Max"));
    		return;
		}
    	
    	//Limit the arm to staying above the ground and below the ceiling
    	if(v12.getY() < -RobotMap.ARM_SHOULDER_HEIGHT + RobotMap.ARM_GROUND_TOLERANCE) 
		{
    		UdpAlertService.sendAlert(new AlertMessage("Arm Constraint: Wrist Ground"));
    		return;
		}
    	if(v12.getY() > 86 - RobotMap.ARM_SHOULDER_HEIGHT - RobotMap.ARM_CEILING_TOLERANCE) 
		{
    		UdpAlertService.sendAlert(new AlertMessage("Arm Constraint: Wrist Ceiling"));
    		return;
		}
    	if(v123.getY() < -RobotMap.ARM_SHOULDER_HEIGHT + RobotMap.ARM_GROUND_TOLERANCE) 
		{
    		UdpAlertService.sendAlert(new AlertMessage("Arm Constraint: Grabber Ground"));
    		return;
		}
    	if(v123.getY() > 86 - RobotMap.ARM_SHOULDER_HEIGHT - RobotMap.ARM_CEILING_TOLERANCE) 
		{
    		UdpAlertService.sendAlert(new AlertMessage("Arm Constraint: Grabber Ceiling"));
    		return;
		}
    	
    	//Stop the arm from hitting our lift
//    	Vector2D liftVector = new Vector2D(true, RobotMap.ARM_LIFT_LOCATION, RobotMap.ARM_LIFT_HEIGHT - RobotMap.ARM_SHOULDER_HEIGHT); //Vector from the shoulder to the lift
//    	Vector2D v2b = Vector2D.subtractVectors(liftVector, v1); //Vector from elbow to lift corner
//    	Vector2D v2c = Vector2D.addVectors(v2b, new Vector2D(false, RobotMap.ARM_WIDTH/2, v2b.getAngle() + 90)); //Vector accounting for the width of the arm
    	/*
    	if(v12.getX() < RobotMap.ARM_LIFT_LOCATION + RobotMap.ARM_LIFT_TOLERANCE && v12.getY() - RobotMap.ARM_WIDTH < RobotMap.ARM_LIFT_HEIGHT - RobotMap.ARM_SHOULDER_HEIGHT + RobotMap.ARM_LIFT_TOLERANCE) 
		{
    		System.out.println("Forearm lift");
    		return;
		}
		*/
//    	Vector2D v3b = new Vector2D(false, -(RobotMap.ARM_LIFT_LOCATION + v12.getX())/Math.cos(v3.getAngle()), v3.getAngle());
//    	Vector2D v3c = Vector2D.addVectors(v3b, new Vector2D(false, RobotMap.GRABBER_WIDTH/2, v3b.getAngle() + 90));
//    	Vector2D v3b = Vector2D.addVectors(v3, new Vector2D(false, RobotMap.GRABBER_WIDTH/2, v3.getAngle() + 90));
//    	Vector2D v123b = Vector2D.addVectors(v3b, v12);
//    	if(v12.getX() < RobotMap.ARM_LIFT_LOCATION && v123b.getY() < RobotMap.ARM_LIFT_HEIGHT - RobotMap.ARM_SHOULDER_HEIGHT + RobotMap.ARM_LIFT_TOLERANCE && v123b.getX() > RobotMap.ARM_LIFT_LOCATION - RobotMap.ARM_LIFT_TOLERANCE) 
//		{
//    		System.out.println("Grabber Lift");
//    		return;
//		}
    	
    	//We passed all the tests. Go ahead and set the setpoints
    	shoulder.setSetpoint(shoulderAngle);
    	elbow.setSetpoint(elbowAngle);
    	wrist.setSetpoint(wristAngle);
    }
    
    public void set(double x, double y, double wrist, boolean bendIn)
    {
    	Vector2D v = new Vector2D(true, x, y);
    	double shoulder = lawOfCosines(RobotMap.ARM_SEGMENT_2_LENGTH, RobotMap.ARM_SEGMENT_1_LENGTH, v.getMagnitude());
    	double elbow = lawOfCosines(v.getMagnitude(),  RobotMap.ARM_SEGMENT_1_LENGTH, RobotMap.ARM_SEGMENT_2_LENGTH);
    	
    	if(bendIn)
    	{
    		shoulder = v.getAngle() - shoulder;
    		elbow = 90 - shoulder + elbow;
    	}
    	else
    	{
    		shoulder = v.getAngle() + shoulder;
    		elbow = 90 - shoulder - elbow;
    	}
    	
    	set(shoulder, elbow, wrist);
    }
    
    public void set(ArmSetpoints setpoint)
    {
    	currentSetpoint = setpoint;
    	set(setpoint.getShoulder(), setpoint.getElbow(), setpoint.getWrist());
    }
    
    public Vector2D getVector()
    {
    	return Vector2D.addVectors(getSegment1Vector(), getSegment2Vector());
    }
    
    public Vector2D getSegment1Vector()
    {
    	return new Vector2D(false, RobotMap.ARM_SEGMENT_1_LENGTH, RobotMap.armShoulderPot.get());
    }
    
    public Vector2D getSegment2Vector()
    {
    	return new Vector2D(false, RobotMap.ARM_SEGMENT_2_LENGTH, RobotMap.armElbowPot.get());
    }
    
    public Vector2D getTargetVector()
    {
    	return Vector2D.addVectors(getSegment1TargetVector(), getSegment2TargetVector());
    }
    
    public Vector2D getSegment1TargetVector()
    {
    	return new Vector2D(false, RobotMap.ARM_SEGMENT_1_LENGTH, shoulder.getSetpoint());
    }
    
    public Vector2D getSegment2TargetVector()
    {
    	return new Vector2D(false, RobotMap.ARM_SEGMENT_2_LENGTH, elbow.getSetpoint());
    }
    
    public double getWrist()
    {
    	return RobotMap.armWristPot.get();
    }
    
    public double getTargetWrist()
    {
    	return wrist.getSetpoint();
    }
    
    public boolean inTolerance()
    {
    	return Math.abs(getVector().getX() - getTargetVector().getX()) < .1 && Math.abs(getVector().getY() - getTargetVector().getY()) < .1 && Math.abs(RobotMap.armWristPot.get() - wrist.getSetpoint()) < 2;
    }
    
    public void pidOn(boolean on)
    {
    	if(on)
    	{
    		shoulder.enable();
    		elbow.enable();
    		wrist.enable();
    	}
    	else
    	{
    		shoulder.disable();
    		elbow.disable();
    		wrist.disable();
    	}
    }
    
    //returns an angle in a triangle given 3 sides
    public static double lawOfCosines(double opp, double a, double b)
    {
    	return Math.acos((Math.pow(a, 2) + Math.pow(b, 2) - Math.pow(opp, 2))/(2*a*b));
    }
}
