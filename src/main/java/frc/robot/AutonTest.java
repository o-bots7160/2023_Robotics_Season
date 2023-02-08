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
   public  Timer timer = new Timer();

   public AutonTest()
   {
      robot = RobotContainer.getInstance();

      interiorWaypoints.add(new Pose2d(Units.feetToMeters(0), Units.feetToMeters(0), new Rotation2d(0.0)));
      interiorWaypoints.add(new Pose2d(Units.feetToMeters(0), Units.feetToMeters(2), new Rotation2d(0.0)));
      interiorWaypoints.add(new Pose2d(Units.feetToMeters(2), Units.feetToMeters(2), new Rotation2d(0.0)));
      interiorWaypoints.add(new Pose2d(Units.feetToMeters(2), Units.feetToMeters(0), new Rotation2d(0.0)));
      interiorWaypoints.add(new Pose2d(Units.feetToMeters(0), Units.feetToMeters(0), new Rotation2d(0.0)));

      TrajectoryConfig config = new TrajectoryConfig(Units.feetToMeters(2), Units.feetToMeters(2)).setKinematics(Constants.Swerve.swerveKinematics);
      config.setReversed(false);

      trajectory = TrajectoryGenerator.generateTrajectory(
          interiorWaypoints,
          config);
 }
   public void Init()
   {
      timer.reset();
      timer.start();
      robot.resetOdometry(interiorWaypoints.get(0));
   }
   public void Periodic()
   {
      switch(step)
      {
         case 0:
            if (timer.get() > trajectory.getTotalTimeSeconds())
            {
               step++;
               robot.drive(new Translation2d(0,0), 0, true, false);
            }
            else
            {
               Trajectory.State new_state = trajectory.sample( timer.get() );
               robot.drive( new_state , new Rotation2d());
            }
            // if(_drive.move_Pose2d(1.0, 0.0, 0.0)) {
            //   step++;
            // }
            // case 1:
            // if(_drive.move_x(0.5)) {
            //   step++;
            // }
            // case 2:
            // if(_drive.move_y(-0.5)) {
            //   step++;
            // }
            // case 3:
            // if(_drive.move_x(-0.5)) {
            //   step++;
            //   _drive.drive(new Translation2d(0,0), 0, true, false);
            // }
            // if ( _drive.move_y(2) )
            // {
            //   step++;
            //   _drive.drive( new Translation2d(), 0, true, true);
            // }
            // if (_drive.goTo(new Pose2d(1,0,new Rotation2d(0)))){
            //   step++;
            // }
            break;
      }
   }
}