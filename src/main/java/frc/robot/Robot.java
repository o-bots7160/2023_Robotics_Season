package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//
// Any Operation mode must implement the following interface
//
//
interface OpModeInterface
{
   public void Init();
   public void Periodic();
}

public class Robot extends TimedRobot {
   private OpModeInterface teleop;
   private OpModeInterface auton;
   private OpModeInterface test;
   private RobotContainer  robot;

   private final SendableChooser<OpModeInterface> m_chooser = new SendableChooser<>();

  public  static CTREConfigs ctreConfigs = new CTREConfigs();

  @Override
  public void robotInit() {
      robot = RobotContainer.getInstance();
      m_chooser.setDefaultOption("Auton3Left", new Auton3Left() );
      m_chooser.addOption("Auton3Right", new Auton3Right() );
      m_chooser.addOption("Auton2LeftCS", new Auton2LeftCS());
      m_chooser.addOption("Auton2RightCS", new Auton2RightCS());
      m_chooser.addOption("Auton1CoOp", new Auton1CoOp());
      SmartDashboard.putData("Auto choices", m_chooser);
  }
  @Override
  public void robotPeriodic() {
      //robot.periodic();
   }

  @Override
   public void autonomousInit()
   {
      auton = m_chooser.getSelected();
      System.out.println("Auto selected: " + auton.toString());

      auton.Init();
  }

  @Override
   public void autonomousPeriodic()
   {
      auton.Periodic();
  }

  @Override
  public void teleopInit() {
      teleop = new Teleop();
      teleop.Init();
  }

  @Override
  public void teleopPeriodic() {
      teleop.Periodic();
      robot.periodic(); // take out later
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {
    robot.disable();
  }

  @Override
  public void testInit()
  {
     test = new TestPose2d();
     test.Init();
  }

  @Override
  public void testPeriodic()
  {
    test.Periodic();
  }
}
