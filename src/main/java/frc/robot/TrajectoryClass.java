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
        var sideStart = new Pose2d(Units.feetToMeters(1), Units.feetToMeters(1),
            Rotation2d.fromDegrees(-180));
        var crossScale = new Pose2d(Units.feetToMeters(-1), Units.feetToMeters(-1),
            Rotation2d.fromDegrees(180));
    
        var interiorWaypoints = new ArrayList<Translation2d>();
        interiorWaypoints.add(new Translation2d(Units.feetToMeters(1), Units.feetToMeters(1)));
        interiorWaypoints.add(new Translation2d(Units.feetToMeters(-1), Units.feetToMeters(-1)));
    
        TrajectoryConfig config = new TrajectoryConfig(Units.feetToMeters(1), Units.feetToMeters(1));
        config.setReversed(true);
    
        return TrajectoryGenerator.generateTrajectory(
            sideStart,
            interiorWaypoints,
            crossScale,
            config);
      }
}
