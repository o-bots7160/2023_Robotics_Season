package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.ManipulatorControl.MANIPPOS;

public class Teleop implements OpModeInterface {
   private RobotContainer robot;

   //Speed & Turn reducer
   private double speedReducer = 2.25;
   private double turnReducer  = .7;

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
      
      double y = -Joystick.getRawAxis(4) / speedReducer;
      double x = -Joystick.getRawAxis(5) / speedReducer;
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

      robot._drive.setRotCenter(UI._changeRotPoint());
     
      robot.drive( new Translation2d( x, y).times(Constants.Swerve.maxSpeed), z, true, true );
      //
      // Manual Manipulator Controls
      //
      if ( UI._manualUp() )
      {
         robot._manipulator.setManipPos( MANIPPOS.MANUAL);
         robot._manipulator.liftSetPose( robot._manipulator.liftGetPose() + 5);
      }
      if ( UI._manualDn() )
      {
         robot._manipulator.setManipPos( MANIPPOS.MANUAL);
         robot._manipulator.liftSetPose( robot._manipulator.liftGetPose() - 5);
      }
      if ( UI._manualOut() )
      {
         robot._manipulator.setManipPos( MANIPPOS.MANUAL);
         robot._manipulator.extSetPose( robot._manipulator.extGetPose() + 10000);
      }
      if ( UI._manualIn() )
      {
         robot._manipulator.setManipPos( MANIPPOS.MANUAL);
         robot._manipulator.extSetPose( robot._manipulator.extGetPose() - 10000);
      }
      if ( UI._manualRotUp() )
      {
         robot._manipulator.setManipPos( MANIPPOS.MANUAL);
         robot._manipulator.wristSetPose( robot._manipulator.wristGetPose() + 0.75);
      }
      if ( UI._manualRotDn() )
      {
         robot._manipulator.setManipPos( MANIPPOS.MANUAL);
         robot._manipulator.wristSetPose( robot._manipulator.wristGetPose() - 0.75);
      }

      if(UI._floor()){
         robot._manipulator.setManipPos( MANIPPOS.FLOOR);
      }else if(UI._mid()){
         robot._manipulator.setManipPos( MANIPPOS.MID);
      }else if(UI._top()){
         robot._manipulator.setManipPos( MANIPPOS.TOP);
      } else if(UI._wristTravelPos()){
         robot._manipulator.setManipPos( MANIPPOS.TRAVEL);
      }

      if ( UI._grabRelease() )
      {
         if (!UI._coneCube()) //yes cone
         {
            robot._manipulator.clawGrabCone();
         } else {
            robot._manipulator.clawGrabCube();
         }
      } else {
         robot._manipulator.clawRelease();;
      }
      robot._manipulator.atPosition();   

      if ( UI._substation() )
         {
            robot._manipulator.setManipPos( MANIPPOS.SUBSTATION);
         }

      if ( UI._lock() )
      {
          robot._drive.lock();
       }

      if ( UI._turbo() )
      {
         speedReducer = 1.5;
         turnReducer = .5;
      }else if ( UI._slow() )
      {
         speedReducer = 6;
         turnReducer = 1.25;
      }else{
         speedReducer = 2.25;
         turnReducer = .7;
      }
      
   }


   public void testPeriodic(){
      if ( UI._manualUp() )
      {
         robot._manipulator.setManipPos( MANIPPOS.MANUAL);
         robot._manipulator.liftSetPose( robot._manipulator.liftGetPose() + 5);
      }
      if ( UI._manualDn() )
      {
         robot._manipulator.setManipPos( MANIPPOS.MANUAL);
         robot._manipulator.liftSetPose( robot._manipulator.liftGetPose() - 5);
      }
   }

      
}
