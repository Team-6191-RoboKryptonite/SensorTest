/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.function.DoublePredicate;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  VictorSP motor;
  Joystick m_stick;
  Ultrasonic ultra;
  double ultra_read;
  int povbutton;
  @Override
  public void robotInit() {
    motor = new VictorSP(0);
    m_stick = new Joystick(0);
    ultra = new Ultrasonic(8, 9);
    ultra.setAutomaticMode(true);
  }
  @Override
  public void robotPeriodic() {
  }

  @Override
  public void autonomousInit() {

  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {

  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    
    ultra_read = ultra.getRangeMM();
    SmartDashboard.putNumber("ultrasonic", ultra_read);
    if(m_stick.getRawButton(1) && ultra_read > 100){
      motor.set(0.3);
    }else{
      motor.stopMotor();
    }

  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
