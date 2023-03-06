package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;

public class Auton3Right implements OpModeInterface
{
   private RobotContainer robot;
//*****************************************THIS AUTON NOT CURRENTLY USED************************************************* */
   private int step = 0;
   private Pose2d startPoint  = new Pose2d(Units.feetToMeters(0), Units.feetToMeters(0),new Rotation2d(0.0));
   private Pose2d Path1[] = {
                    new Pose2d(Units.feetToMeters(10), Units.feetToMeters(0),new Rotation2d(0.0)),
                    new Pose2d(Units.feetToMeters(14), Units.feetToMeters(0),new Rotation2d(Math.PI)),
                    new Pose2d(Units.feetToMeters(14), Units.feetToMeters(0),new Rotation2d(0.0)),
                    new Pose2d(Units.feetToMeters(0.25), Units.feetToMeters(0),new Rotation2d(0.0)) };
   private Pose2d Path2[] = {
                    new Pose2d(Units.feetToMeters(14), Units.feetToMeters(0),new Rotation2d(0.0)),
                    new Pose2d(Units.feetToMeters(14), Units.feetToMeters(4),new Rotation2d(Math.PI)),
                    new Pose2d(Units.feetToMeters(14), Units.feetToMeters(0),new Rotation2d(0.0)),
                    new Pose2d(Units.feetToMeters(0.25), Units.feetToMeters(0),new Rotation2d(0.0)) };
    private SwervePath firstPath  = new SwervePath( Path1 );
    private SwervePath secondPath = new SwervePath( Path2 );

   public Auton3Right()
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
            if ( firstPath.atDestination() )
            {
               step++;
            }
            break;
         case 1:
            step++;
            // Place cone or something
            break;
         case 2:
            if ( secondPath.atDestination() )
            {
               step++;
            }
            break;
         default:
            break;
      }
   }
}
