package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import java.util.List;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//Swerve-specific imports
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;

public class Robot extends TimedRobot {

  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";

  //Speed & Turn reducer
  private static double speedReducer = 2;
  private static double turnReducer  = 0.75;

  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  private final Swerve _drive = new Swerve();
  private final frc.robot.TrajectoryClass _trajectory = new frc.robot.TrajectoryClass();
  private Trajectory autonTrajectory;
  private String m_autoSelected;

  public  Timer timer = new Timer();
  public  static CTREConfigs ctreConfigs = new CTREConfigs();

  Joystick Joystick = new Joystick(0); // Joystick
  //Joystick Joystick = new Joystick(1); // Gamepad

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

    //TrajectoryConfig config = new TrajectoryConfig(.1, 0.2);
    autonTrajectory = _trajectory.testTrajectory1(); // TrajectoryGenerator.generateTrajectory(new Pose2d(0,0, new Rotation2d(0)), List.of(new Translation2d(-1,-1), new Translation2d(1,1)), new Pose2d(0,0, new Rotation2d(0)), config);
    //m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    //System.out.println("Auto selected: " + m_autoSelected);
  }

  @Override
  public void autonomousPeriodic() {
    switch(curr_Auto){
      case TEST_AUTO:
      Trajectory.State new_state = autonTrajectory.sample(timer.get());
      _drive.drive( new_state);
      //_drive.controller = _drive.drive(null, kDefaultPeriod, isAutonomousEnabled(), isAutonomous());
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

  @Override
  public void teleopInit() {
    _drive.zeroGyro();
  }

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

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}
}
