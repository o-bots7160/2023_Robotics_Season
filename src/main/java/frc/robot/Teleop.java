package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class Teleop implements OpModeInterface {
   private RobotContainer robot;

   //Speed & Turn reducer
   private static double speedReducer = 2;
   private static double turnReducer  = .5;

   Joystick Joystick = new Joystick(0); // Joystick

   public Teleop()
   {
      robot = RobotContainer.getInstance();
   }
   public void Init()
   {
      if (DriverStation.getAlliance() == Alliance.Red) {
         speedReducer = -speedReducer;
      }
      // zero gyro? maybe not... only in Robot.init?
   }
   public void Periodic()
   {
      // Convert these from -1.0 - 1.0 to min/max speed or rotation
      double x = Joystick.getRawAxis(4) / speedReducer;
      double y = Joystick.getRawAxis(5) / speedReducer;
      double z = Joystick.getRawAxis(0) / turnReducer;
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
      robot.drive( new Translation2d( y, x).times(Constants.Swerve.maxSpeed), z, true, true );
   }
}
