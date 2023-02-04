package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Joystick;

public class Test1 implements OpModeInterface {
   private RobotContainer robot;

   //Speed & Turn reducer
   private static double speedReducer = 2;
   private static double turnReducer  = 0.75;

   Joystick Joystick; // Joystick
   int direction = 0;

   public Test1()
   {
      robot = RobotContainer.getInstance();
      Joystick = new Joystick(0);
      Joystick.getRawButtonPressed(1);
      Joystick.getRawButtonPressed( 4 );
      Joystick.getRawButtonPressed( 2 );
      Joystick.getRawButtonPressed( 3 );
      Joystick.getRawButtonPressed( 5 );
      Joystick.getRawButtonPressed( 6 );
   }
   public void Init()
   {
      // zero gyro? maybe not... only in Robot.init?
   }
   public void Periodic()
   {
      if ( robot._drive.auton_active )
      {
         System.out.println( "auton_active: "+ direction);
         switch ( direction )
         {
            case 1:
               robot._drive.move_x( Units.feetToMeters( 2));
               break;
            case 2:
               robot._drive.move_y( Units.feetToMeters( 2));
               break;
            case 3:
               robot._drive.move_y( Units.feetToMeters( -2));
               break;
            case 4:
               robot._drive.move_x( Units.feetToMeters( -2));
               break;
            case 5:
               robot._drive.rotate(90);
               break;
            case 6:
               robot._drive.rotate(-90);
               break;
         }
      }
      else
      {
         //System.out.println( "Getting button");
         if ( Joystick.getRawButtonPressed(1) )
         {
            System.out.println( "Got A button");
            direction = 1;
            robot._drive.move_x( Units.feetToMeters( 2));
      }
         if ( Joystick.getRawButtonPressed( 4 ) )
         {
            System.out.println( "Got Y button");
            direction = 4;
            robot._drive.move_x( Units.feetToMeters( -2));
         }
         if ( Joystick.getRawButtonPressed( 2 ) )
         {
            System.out.println( "Got B button");
            direction = 2;
            robot._drive.move_y( Units.feetToMeters( 2));
         }
         if ( Joystick.getRawButtonPressed( 3 ) )
         {
            System.out.println( "Got X button");
            direction = 3;
            robot._drive.move_y( Units.feetToMeters( -2));
         }
         if ( Joystick.getRawButtonPressed(5) )
         {
            System.out.println( "Got Left Bumper");
            direction = 5;
            robot._drive.rotate(90);
         }
         if ( Joystick.getRawButtonPressed(6) )
         {
            System.out.println( "Got Right Bumper");
            direction = 6;
            robot._drive.rotate(-90);
         }
      }
   }
}
