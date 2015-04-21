package org.usfirst.frc.team246.robot.overclockedLibraries;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Diagnostics implements Runnable {
	
	public static Diagnostics instance;
	
	private static ArrayList<DiagnosticsAnalogIn> analogIns = new ArrayList<DiagnosticsAnalogIn>();
	private static ArrayList<DiagnosticsAnalogPot> analogPots = new ArrayList<DiagnosticsAnalogPot>();
	private static ArrayList<DiagnosticsEncoder> encoders = new ArrayList<DiagnosticsEncoder>();
	
	public static void initialize()
	{
		if(instance == null)
		{
			instance = new Diagnostics();
			(new Thread(instance)).start();
		}
	}
	
	public static void addAnalogIn(AnalogIn analogIn, String name)
	{
		analogIns.add(instance.new DiagnosticsAnalogIn(analogIn, name));
	}
	public static void addAnalogPot(AnalogPot analogPot, String name, SpeedController246 motor)
	{
		analogPots.add(instance.new DiagnosticsAnalogPot(analogPot, name, motor));
	}
	public static void addEncoder(Encoder encoder, String name, SpeedController246 motor)
	{
		encoders.add(instance.new DiagnosticsEncoder(encoder, name, motor));
	}

	static final double BEAGLEBONE_UNPLUGGED_MIN = 0;
	static final double BEAGLEBONE_UNPLUGGED_MAX = 0;
	static final double ROBORIO_UNPLUGGED_MIN = 0;
	static final double ROBORIO_UNPLUGGED_MAX = 0;
	static final double MIN_POT_VALUE_CHANGE_PER_SECOND = .2;
	static final double MIN_ENCODER_VALUE_CHANGE_PER_SECOND = 25;
	static final double MIN_MOTOR_VALUE = .2;
	
	@Override
	public void run() {
		while(true)
		{
			double[][] previousAnalogInValues = new double[analogIns.size()][150];
			for(int i = 0; i < analogIns.size(); i++)
			{
				DiagnosticsAnalogIn ai = analogIns.get(i);
				SmartDashboard.putNumber(ai.name, ai.sensor.get());
				for(int c = previousAnalogInValues.length - 1; c > 0; c--)
				{
					previousAnalogInValues[i][c] = previousAnalogInValues[i][c-1];
				}
				previousAnalogInValues[i][0] = ai.sensor.get();
				if(ai.sensor.onRIO)
				{
					
				}
			}
			for(int i = 0; i < analogPots.size(); i++)
			{
				DiagnosticsAnalogPot ap = analogPots.get(i);
				SmartDashboard.putNumber(ap.name, ap.sensor.get());
			}
			for(int i = 0; i < encoders.size(); i++)
			{
				DiagnosticsEncoder e = encoders.get(i);
				SmartDashboard.putNumber(e.name, e.sensor.getDistance());
			}
			Timer.delay(.1);
		}
	}
	
	private class DiagnosticsAnalogIn
	{
		public AnalogIn sensor;
		public String name;
		
		public DiagnosticsAnalogIn(AnalogIn sensor, String name)
		{
			this.sensor = sensor;
			this.name = name;
		}
	}
	private class DiagnosticsAnalogPot
	{
		public AnalogPot sensor;
		public String name;
		public SpeedController246 motor;
		
		public DiagnosticsAnalogPot(AnalogPot sensor, String name, SpeedController246 motor)
		{
			this.sensor = sensor;
			this.name = name;
			this.motor = motor;
		}
	}
	private class DiagnosticsEncoder
	{
		public Encoder sensor;
		public String name;
		public SpeedController246 motor;
		
		public DiagnosticsEncoder(Encoder sensor, String name, SpeedController246 motor)
		{
			this.sensor = sensor;
			this.name = name;
			this.motor = motor;
		}
	}
}
