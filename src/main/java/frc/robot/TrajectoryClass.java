package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;

import java.util.ArrayList;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.trajectory.Trajectory;
//import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.util.Units;

public class TrajectoryClass {
    public Trajectory testTrajectory1() {

        // 2018 cross scale auto waypoints.
        var x5y5 = new Pose2d(Units.feetToMeters(3), Units.feetToMeters(3),
            Rotation2d.fromDegrees( 0.0));
        var x0y0 = new Pose2d(Units.feetToMeters(0), Units.feetToMeters(0),
            Rotation2d.fromDegrees(0.0));
    
        var interiorWaypoints = new ArrayList<Translation2d>();
        interiorWaypoints.add(new Translation2d(Units.feetToMeters(1.5), Units.feetToMeters(1.5)));
        interiorWaypoints.add(new Translation2d(Units.feetToMeters(1.5), Units.feetToMeters(1.5)));
    
        TrajectoryConfig config = new TrajectoryConfig(Units.feetToMeters(2), Units.feetToMeters(2)).setKinematics(Constants.Swerve.swerveKinematics);
        config.setReversed(false);
    
        return TrajectoryGenerator.generateTrajectory(
            x5y5,
            interiorWaypoints,
            x0y0,
            config);
      }

    public Trajectory testTrajectory2() {

        //var startPoint = new Pose2d(Units.feetToMeters(0), Units.feetToMeters(0), Rotation2d.fromDegrees( 0.0));

        //var endPoint = new Pose2d(Units.feetToMeters(0), Units.feetToMeters(0), Rotation2d.fromDegrees( 0.0));

        var interiorWaypoints = new ArrayList<Pose2d>();
        interiorWaypoints.add(new Pose2d(Units.feetToMeters(0), Units.feetToMeters(2), new Rotation2d(0.0)));
        interiorWaypoints.add(new Pose2d(Units.feetToMeters(2), Units.feetToMeters(2), new Rotation2d(0.0)));
        interiorWaypoints.add(new Pose2d(Units.feetToMeters(2), Units.feetToMeters(0), new Rotation2d(0.0)));
        interiorWaypoints.add(new Pose2d(Units.feetToMeters(0), Units.feetToMeters(0), new Rotation2d(0.0)));

        TrajectoryConfig config = new TrajectoryConfig(Units.feetToMeters(1), Units.feetToMeters(1)).setKinematics(Constants.Swerve.swerveKinematics);
        config.setReversed(false);

        return TrajectoryGenerator.generateTrajectory(
            //startPoint,
            interiorWaypoints,
            //endPoint,
            config);
    }
}
