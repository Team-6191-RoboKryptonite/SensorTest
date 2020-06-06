/**
 * Phoenix Software License Agreement
 *
 * Copyright (C) Cross The Road Electronics.  All rights
 * reserved.
 * 
 * Cross The Road Electronics (CTRE) licenses to you the right to 
 * use, publish, and distribute copies of CRF (Cross The Road) firmware files (*.crf) and 
 * Phoenix Software API Libraries ONLY when in use with CTR Electronics hardware products
 * as well as the FRC roboRIO when in use in FRC Competition.
 * 
 * THE SOFTWARE AND DOCUMENTATION ARE PROVIDED "AS IS" WITHOUT
 * WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING WITHOUT
 * LIMITATION, ANY WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE AND NON-INFRINGEMENT. IN NO EVENT SHALL
 * CROSS THE ROAD ELECTRONICS BE LIABLE FOR ANY INCIDENTAL, SPECIAL, 
 * INDIRECT OR CONSEQUENTIAL DAMAGES, LOST PROFITS OR LOST DATA, COST OF
 * PROCUREMENT OF SUBSTITUTE GOODS, TECHNOLOGY OR SERVICES, ANY CLAIMS
 * BY THIRD PARTIES (INCLUDING BUT NOT LIMITED TO ANY DEFENSE
 * THEREOF), ANY CLAIMS FOR INDEMNITY OR CONTRIBUTION, OR OTHER
 * SIMILAR COSTS, WHETHER ASSERTED ON THE BASIS OF CONTRACT, TORT
 * (INCLUDING NEGLIGENCE), BREACH OF WARRANTY, OR OTHERWISE
 */

/**
 * Description:
 * The MotionMagic example demonstrates the motion magic control mode.
 * Tested with Logitech F710 USB Gamepad inserted into Driver Station.
 * 
 * Be sure to select the correct feedback sensor using configSelectedFeedbackSensor() below.
 *
 * After deploying/debugging this to your RIO, first use the left Y-stick 
 * to throttle the Talon manually. This will confirm your hardware setup/sensors
 * and will allow you to take initial measurements.
 * 
 * Be sure to confirm that when the Talon is driving forward (green) the 
 * position sensor is moving in a positive direction. If this is not the 
 * cause, flip the boolean input to the setSensorPhase() call below.
 *
 * Ensure your feedback device is in-phase with the motor,
 * and you have followed the walk-through in the Talon SRX Software Reference Manual.
 * 
 * Controls:
 * Button 1: When held, put Talon in Motion Magic mode and allow Talon to drive [-10, 10] 
 * 	rotations.
 * Left Joystick Y-Axis:
 * 	+ Percent Output: Throttle Talon SRX forward and reverse, use to confirm hardware setup.
 * 	+ Motion Maigic: SErvo Talon SRX forward and reverse, [-10, 10] rotations.
 * 
 * Gains for Motion Magic may need to be adjusted in Constants.java
 * 
 * Supported Version:
 * - Talon SRX: 4.00
 * - Victor SPX: 4.00
 * - Pigeon IMU: 4.00
 * - CANifier: 4.00
 */
package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;
import java.util.concurrent.TimeUnit;		// Delay

import com.ctre.phoenix.motorcontrol.can.*;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
//import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;



public class Robot extends TimedRobot {
	/* Hardware */
	TalonSRX Rtalon = new TalonSRX(3);
	TalonSRX RtalonF = new TalonSRX(4);
	//Tips: if you want to use VictorSPX here, let the VictorSPXs follow the TalonSRX the follower
	// TalonSRX Ltalon = new TalonSRX (2);
	// TalonSRX LtalonF = new TalonSRX (1);
	Joystick _joy = new Joystick(0);
	/* Used to build string throughout loop */
	StringBuilder _sb = new StringBuilder();

	public void robotInit() {
		/* Factory default hardware to prevent unexpected behavior */
		Rtalon.configFactoryDefault();
		RtalonF.follow (Rtalon);

		/* Configure Sensor Source for Pirmary PID */
		Rtalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,
											Constants.kPIDLoopIdx, 
											Constants.kTimeoutMs);

		/**
		 * Configure Talon SRX Output and Sesnor direction accordingly
		 * Invert Motor to have green LEDs when driving Talon Forward / Requesting Postiive Output
		 * Phase sensor to have positive increment when driving Talon Forward (Green LED)
		 */
		Rtalon.setSensorPhase(false);
		Rtalon.setInverted(false);
		int MaxSpeed=3000;

		/* Set relevant frame periods to be at least as fast as periodic rate */
		// _talon.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, Constants.kTimeoutMs);
		// _talon.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Constants.kTimeoutMs);

		/* Set the peak and nominal outputs */
		// _talon.configNominalOutputForward(0, Constants.kTimeoutMs);
		// _talon.configNominalOutputReverse(0, Constants.kTimeoutMs);
		// _talon.configPeakOutputForward(1, Constants.kTimeoutMs);
		// _talon.configPeakOutputReverse(-1, Constants.kTimeoutMs);

		/* Set Motion Magic gains in slot0 - see documentation */
		Rtalon.selectProfileSlot(Constants.kSlotIdx, Constants.kPIDLoopIdx);
		Rtalon.config_kF(Constants.kSlotIdx, Constants.kGains.kF, Constants.kTimeoutMs);
		Rtalon.config_kP(Constants.kSlotIdx, Constants.kGains.kP, Constants.kTimeoutMs);
		Rtalon.config_kI(Constants.kSlotIdx, Constants.kGains.kI, Constants.kTimeoutMs);
		Rtalon.config_kD(Constants.kSlotIdx, Constants.kGains.kD, Constants.kTimeoutMs);

		/* Set acceleration and vcruise velocity - see documentation */
		Rtalon.configMotionCruiseVelocity(MaxSpeed/2, Constants.kTimeoutMs);
		Rtalon.configMotionAcceleration(3000, Constants.kTimeoutMs);

		/* Zero the sensor */
		Rtalon.setSelectedSensorPosition(0, Constants.kPIDLoopIdx, Constants.kTimeoutMs);
	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
		/* Get gamepad axis - forward stick is positive */
		double stick = -1.0 * _joy.getRawAxis(5);

		/* Get current Talon SRX motor output */
		double motorOutput = Rtalon.getMotorOutputPercent();

		/* Prepare line to print */
		_sb.append("\tOut%:");
		_sb.append(motorOutput);
		_sb.append("\tVel:");
		_sb.append(Rtalon.getSelectedSensorVelocity(Constants.kPIDLoopIdx));

		/**
		 * Peform Motion Magic when Button 1 is held,
		 * else run Percent Output, which can be used to confirm hardware setup.
		 */
		if (_joy.getRawButton(1)) {
			/* Motion Magic */ 
			
			/*4096 ticks/rev * 10 Rotations in either direction */
			double targetPos = stick * 4096 * 10.0;
			Rtalon.set(ControlMode.MotionMagic, targetPos);

			/* Append more signals to print when in speed mode */
			_sb.append("\terr:");
			_sb.append(Rtalon.getClosedLoopError(Constants.kPIDLoopIdx));
			_sb.append("\ttrg:");
			_sb.append(targetPos);
		} else {
			/* Percent Output */

			Rtalon.set(ControlMode.PercentOutput, stick);
		}

		/* Instrumentation */
		Instrum.Process(Rtalon, _sb);

		/* 10 Ms timeout, allow CAN Frames to process */
		try { TimeUnit.MILLISECONDS.sleep(10); } 	
		catch (Exception e) { /* Do Nothing */ }
	}
}