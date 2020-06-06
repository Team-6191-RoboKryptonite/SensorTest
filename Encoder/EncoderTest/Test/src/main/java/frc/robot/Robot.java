
package frc.robot;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
//import edu.wpi.first.wpilibj.SpeedControllerGroup;

//import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends TimedRobot {
private DifferentialDrive m_robotDrive;
private Joystick m_stick;
private Timer m_timer;
WPI_TalonSRX DriveLeft;
WPI_TalonSRX DriveRight;
WPI_TalonSRX DriveLeftr;
WPI_TalonSRX DriveRightr;


@Override
public void robotInit() {
 m_timer=new Timer();
 m_stick=new Joystick(0);
 DriveLeft=new WPI_TalonSRX(2);
 DriveLeftr=new WPI_TalonSRX(1);
 DriveRightr=new WPI_TalonSRX(4);
 DriveRight=new WPI_TalonSRX(3);
 DriveLeftr.follow(DriveLeft);
 DriveRightr.follow(DriveRight);
 //DriveLeftr.setInverted(InvertType.FollowMaster);
 //DriveRightr.setInverted(InvertType.FollowMaster);
 m_robotDrive=new DifferentialDrive(DriveLeft, DriveRight);

}

/**
* This function is run once each time the robot enters autonomous mode.
*/
@Override
public void autonomousInit() {
 m_timer.reset();
 m_timer.start();
}

/**
* This function is called periodically during autonomous.
*/
@Override
public void autonomousPeriodic() {

}

/**
* This function is called once each time the robot enters teleoperated mode.
*/
@Override
public void teleopInit() {

}

/**
* This function is called periodically during teleoperated mode.
*/
@Override
public void teleopPeriodic() {
m_robotDrive.arcadeDrive(m_stick.getRawAxis(5)* (-0.5), m_stick.getRawAxis(4)*0.5);
}


}



