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
   private LED             _LED = new LED();

   private final SendableChooser<OpModeInterface> m_chooser = new SendableChooser<>();

  public  static CTREConfigs ctreConfigs = new CTREConfigs();

  @Override
  public void robotInit() {
      robot = RobotContainer.getInstance();
      m_chooser.setDefaultOption("Auton1CoOp", new Auton1CoOp());
      //m_chooser.addOption("Auton3Right", new Auton3Right() );
      //m_chooser.addOption("Auton2LeftCS", new Auton2LeftCS());
      //m_chooser.addOption("Auton2RightCS", new Auton2RightCS());
      m_chooser.addOption("Auton1RightCS", new Auton1RightCS());
      m_chooser.addOption("Auton1LeftCS", new Auton1LeftCS());
      m_chooser.addOption("AutonPose", new AutonPose()); 
      m_chooser.addOption("Auton1", new Auton1()); 
      //m_chooser.addOption("Auton3Left", new Auton3Left() );
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
      //System.out.println("Auto selected: " + auton.toString());

      auton.Init();
      _LED.Init();
  }

  @Override
   public void autonomousPeriodic()
   {
      auton.Periodic();
      _LED.Periodic();
  }

  @Override
  public void teleopInit() {
      teleop = new Teleop();
      teleop.Init();
  }

  @Override
  public void teleopPeriodic() {
      teleop.Periodic();
      _LED.Periodic();
      //robot.periodic(); // take out later
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
    teleop = new Teleop();
  }

  @Override
  public void testPeriodic()
  {
    
  }
}
