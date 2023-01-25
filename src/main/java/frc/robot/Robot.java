package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;

import java.util.List;
//import java.util.Timer;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.Joystick;

public class Robot extends TimedRobot {
  public static CTREConfigs ctreConfigs = new CTREConfigs();
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private static double speedReducer = 2;
  private static double turnReducer = 0.75;
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  private final Swerve _drive = new Swerve();
  private Trajectory autonTrajectory;
  public Timer timer = new Timer();
  //Joystick Joystick = new Joystick(0); // Joystick
  Joystick Joystick = new Joystick(0); // Gamepad

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
    _drive.zeroGyro();
  }
  @Override
  public void robotPeriodic() {
    _drive.periodic();
   }

  @Override
  public void autonomousInit() {
    timer.reset();
    timer.start();
    _drive.zeroGyro();
    _drive.resetOdometry(new Pose2d(0.0, 0.0, new Rotation2d(0)));
    curr_Auto = AUTONS.TEST_AUTO;

    TrajectoryConfig config = new TrajectoryConfig(.1, 0.2);

    autonTrajectory = TrajectoryGenerator.generateTrajectory(new Pose2d(0,0, new Rotation2d(0)), List.of(new Translation2d(-1,-1), new Translation2d(1,1)), new Pose2d(0,0, new Rotation2d(0)), config);
    //m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    //System.out.println("Auto selected: " + m_autoSelected);
  }

  @Override
  public void autonomousPeriodic() {
    switch(curr_Auto){
      case TEST_AUTO:
      _drive.drive(autonTrajectory.sample(timer.get()));
      break;
    }
  }

  private void test_Auto(){
    switch(step){
      case 0:
      // if (_drive.goTo(new Pose2d(1,0,new Rotation2d(0)))){
      //   step++;
      // }
      break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
    _drive.zeroGyro();
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    // Convert these from -1.0 - 1.0 to min/max speed or rotation
    double x = Joystick.getRawAxis(0) / speedReducer;
    double y = Joystick.getRawAxis(1) / speedReducer;
    double z = Joystick.getRawAxis(4) / turnReducer;
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

    _drive.drive( new Translation2d( y, x).times(Constants.Swerve.maxSpeed), z, true, true );
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
