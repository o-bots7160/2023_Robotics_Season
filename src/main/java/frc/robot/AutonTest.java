package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;

public class AutonTest implements OpModeInterface
{
   private RobotContainer robot;

   private int step = 0;
   private Pose2d startPoint   = new Pose2d(Units.feetToMeters(0), Units.feetToMeters(0),new Rotation2d(0.0));
   private Pose2d firstPoint   = new Pose2d(Units.feetToMeters(10), Units.feetToMeters(0),new Rotation2d(0.0));
   private Pose2d secondPoint  = new Pose2d(Units.feetToMeters(14), Units.feetToMeters(0),new Rotation2d(Math.PI));
   private Pose2d thirdPoint   = new Pose2d(Units.feetToMeters(14), Units.feetToMeters(0),new Rotation2d(0.0));
   private Pose2d fourthPoint  = new Pose2d(Units.feetToMeters(1), Units.feetToMeters(0),new Rotation2d(0.0));
   private Pose2d fifthPoint   = new Pose2d(Units.feetToMeters(14), Units.feetToMeters(0),new Rotation2d(0.0));
   private Pose2d sixthPoint   = new Pose2d(Units.feetToMeters(14), Units.feetToMeters(5),new Rotation2d(Math.PI));
   private Pose2d seventhPoint = new Pose2d(Units.feetToMeters(14), Units.feetToMeters(0),new Rotation2d(0.0));
   private Pose2d endPoint     = new Pose2d(Units.feetToMeters(1), Units.feetToMeters(0),new Rotation2d(0.0));

   public AutonTest()
   {
      robot = RobotContainer.getInstance();
   }
   public void Init()
   {
      robot.resetOdometry(startPoint);
   }
   public void Periodic()
   {
      switch( step )
      {
         case 0:
            if ( ! robot._drive.move_Pose2d( firstPoint ) )
            {
               step++;
               robot.drive(new Translation2d(0,0), 0, true, false);
            }
            break;
         case 1:
            if ( ! robot._drive.move_Pose2d( secondPoint ) )
            {
               step++;
               robot.drive(new Translation2d(0,0), 0, true, false);
            }
            break;
         case 2:
            if ( ! robot._drive.move_Pose2d( thirdPoint ) )
            {
               step++;
               robot.drive(new Translation2d(0,0), 0, true, false);
            }
            break;
         case 3:
            if ( ! robot._drive.move_Pose2d( fourthPoint ) )
            {
               step++;
               robot.drive(new Translation2d(0,0), 0, true, false);
            }
            break;
         case 4:
            if ( ! robot._drive.move_Pose2d( fifthPoint ) )
            {
               step++;
               robot.drive(new Translation2d(0,0), 0, true, false);
            }
            break;
         case 5:
            if ( ! robot._drive.move_Pose2d( sixthPoint ) )
            {
               step++;
               robot.drive(new Translation2d(0,0), 0, true, false);
            }
            break;
         case 6:
            if ( ! robot._drive.move_Pose2d( seventhPoint ) )
            {
               step++;
               robot.drive(new Translation2d(0,0), 0, true, false);
            }
            break;
         case 7: 
            if ( ! robot._drive.move_Pose2d( endPoint ) )
            {
               step++;
               robot.drive(new Translation2d(0,0), 0, true, false);
            }
            break;
         default:
            robot.drive(new Translation2d(0,0), 0, true, false);
            break;
      }
   }
}
