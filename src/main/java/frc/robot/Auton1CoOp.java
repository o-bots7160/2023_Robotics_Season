package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;

public class Auton1CoOp implements OpModeInterface
{
   private RobotContainer robot;

   private int step = 0;
   private Pose2d startPoint  = new Pose2d(Units.feetToMeters(0), Units.feetToMeters(0),new Rotation2d(0.0));
   private Pose2d Path1[] = {
                    new Pose2d(Units.feetToMeters(12), Units.feetToMeters(0),new Rotation2d(0.0)),
                    new Pose2d(Units.feetToMeters(8), Units.feetToMeters(0),new Rotation2d(0.0)) };
    private SwervePath firstPath  = new SwervePath( Path1 );

   public Auton1CoOp()
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
         default:
            break;
      }
   }
}
