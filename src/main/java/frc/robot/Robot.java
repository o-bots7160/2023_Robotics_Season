package frc.robot;

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
      m_chooser.setDefaultOption("AutonTest", new AutonTest());
      m_chooser.addOption("Auton2", new Auton2() );
      SmartDashboard.putData("Auto choices", m_chooser);
  }
  @Override
  public void robotPeriodic() {
      robot.periodic();
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
