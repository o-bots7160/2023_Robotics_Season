package frc.robot;

import java.util.ArrayList;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Timer;

public class AutonTest implements OpModeInterface
{
   private RobotContainer robot;

   private int step = 0;
   private Trajectory trajectory;
   private ArrayList<Pose2d> interiorWaypoints = new ArrayList<Pose2d>();
   private Pose2d startPoint = new Pose2d(Units.feetToMeters(0), Units.feetToMeters(0),new Rotation2d(0.0));
   private Pose2d endPoint = new Pose2d(Units.feetToMeters(12), Units.feetToMeters(0),new Rotation2d(0.0));

   public AutonTest()
   {
      robot = RobotContainer.getInstance();
      // var interiorWaypoints = new ArrayList<Translation2d>();
      //    interiorWaypoints.add(new Translation2d(Units.feetToMeters(1), Units.feetToMeters(1)));
      //    interiorWaypoints.add(new Translation2d(Units.feetToMeters(2), Units.feetToMeters(2)));

   }
   public void Init()
   {
      robot.resetOdometry(startPoint);
      //robot._drive.newPose2d( endPoint );
   }
   public void Periodic()
   {
      switch( step )
      {
         case 0:
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
