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
        var sideStart = new Pose2d(Units.feetToMeters(5), Units.feetToMeters(5),
            Rotation2d.fromDegrees(-90));
        var crossScale = new Pose2d(Units.feetToMeters(0), Units.feetToMeters(0),
            Rotation2d.fromDegrees(90));
    
        var interiorWaypoints = new ArrayList<Translation2d>();
        interiorWaypoints.add(new Translation2d(Units.feetToMeters(2.5), Units.feetToMeters(2.5)));
        interiorWaypoints.add(new Translation2d(Units.feetToMeters(2.5), Units.feetToMeters(2.5)));
    
        TrajectoryConfig config = new TrajectoryConfig(Units.feetToMeters(1), Units.feetToMeters(1)).setKinematics(Constants.Swerve.swerveKinematics);
        config.setReversed(true);
    
        System.out.println(config);
        return TrajectoryGenerator.generateTrajectory(
            sideStart,
            interiorWaypoints,
            crossScale,
            config);
      }
}
