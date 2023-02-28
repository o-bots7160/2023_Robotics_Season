package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class Teleop implements OpModeInterface {
   private RobotContainer robot;

   //Speed & Turn reducer
   private static double speedReducer = -4;
   private static double turnReducer  = 1;

   Joystick Joystick = new Joystick(0); // Joystick

   public Teleop()
   {
      robot = RobotContainer.getInstance();
   }
   public void Init()
   {
      // zero gyro? maybe not... only in Robot.init?
   }
   public void Periodic()
   {
      // Convert these from -1.0 - 1.0 to min/max speed or rotation
      double x = Joystick.getRawAxis(4) / speedReducer;
      double y = Joystick.getRawAxis(5) / speedReducer;
      double z = -Joystick.getRawAxis(0) / turnReducer;
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
      //
      // Manual Manipulator Controls
      //
      if ( UI._manualUp() )
      {
         robot._manipulator.liftSetPose( robot._manipulator.liftGetPose() + 20);
      }
      if ( UI._manualDn() )
      {
         robot._manipulator.liftSetPose( robot._manipulator.liftGetPose() - 20);
      }
      if ( UI._manualOut() )
      {
         robot._manipulator.extSetPose( robot._manipulator.extGetPose() + 10000);
      }
      if ( UI._manualIn() )
      {
         robot._manipulator.extSetPose( robot._manipulator.extGetPose() - 10000);
      }
      if ( UI._manualRotUp() )
      {
         robot._manipulator.wristSetPose( robot._manipulator.wristGetPose() + 2);
      }
      if ( UI._manualRotDn() )
      {
         robot._manipulator.wristSetPose( robot._manipulator.wristGetPose() - 2);
      }

      if(UI._floor()){
         robot._manipulator.liftSetPose(0);
      }else if(UI._mid()){
         if(UI._coneCube()){// if true cube is selected
            robot._manipulator.liftSetPose(128);
         }else{
            robot._manipulator.liftSetPose(182);
         }
      }else if(UI._top()){
         if(UI._coneCube()){// if true cube is selected
            robot._manipulator.liftSetPose(200);
         }else{
            robot._manipulator.liftSetPose(235);
         }
      }

      if(UI._wristTravelPos()){

      }else if(UI._wristPlacePos()){
         if(UI._coneCube()){// if true cube is selected

         }else{

         }
      }

      if(UI._clawIn()){
         robot._manipulator.clawSetPose(robot._manipulator.clawGetPose() + .5);
      }else if(UI._clawOut()){
         robot._manipulator.clawSetPose(robot._manipulator.clawGetPose() - .5);
      }

      //if(UI._wristPlacePos())

      System.out.println("Claw pos:" + robot._manipulator.clawGetPose());
   }
}
