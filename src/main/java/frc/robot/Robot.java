package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.Joystick;

public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  private final DriveTrainSwerve _drive = new DriveTrainSwerve();
  //Joystick Joystick = new Joystick(0); // Joystick
  Joystick Joystick = new Joystick(1); // Gamepad

  enum AUTONS {
    TEST_AUTO
  };

  AUTONS curr_Auto;
  private int step = 0;

  @Override
  public void robotInit() {
    //m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    //m_chooser.addOption("My Auto", kCustomAuto);
    //SmartDashboard.putData("Auto choices", m_chooser);
    _drive.zeroGyroscope();
  }
  @Override
  public void robotPeriodic() {
    _drive.periodic();
   }

  @Override
  public void autonomousInit() {
    _drive.zeroGyroscope();
    _drive.setPosition(new Pose2d(0.0, 0.0, new Rotation2d(0)));
    curr_Auto = AUTONS.TEST_AUTO;
    //m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    //System.out.println("Auto selected: " + m_autoSelected);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch(curr_Auto){
      case TEST_AUTO:
      break;
    }
  }

  private void test_Auto(){
    switch(step){
      case 0:
      if (_drive.goTo(new Pose2d(1,0,new Rotation2d(0)))){
        step++;
      }
      break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
    _drive.zeroGyroscope();
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    // Convert these from -1.0 - 1.0 to min/max speed or rotation
    double x = Joystick.getRawAxis(0) * 3.0;
    double y = Joystick.getRawAxis(1) * 3.0;
    double z = Joystick.getRawAxis(4) * 3.0;
    if (x < Constants.JOYSTICK_X_POSITIVE_DEADBAND && x > Constants.JOYSTICK_X_NEGATIVE_DEADBAND)
    {
      x = 0;
    }
    if (y < Constants.JOYSTICK_Y_POSITIVE_DEADBAND && y > Constants.JOYSTICK_Y_NEGATIVE_DEADBAND)
    {
      y = 0;
    }
    if (z < Constants.JOYSTICK_Z_POSITIVE_DEADBAND && z > Constants.JOYSTICK_Z_NEGATIVE_DEADBAND)
    {
      z = 0;
    }

    _drive.drive(-x, y, z);
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}
