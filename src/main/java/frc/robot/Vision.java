// package frc.robot;

// import static frc.robot.Constants.VisionConstants.APRILTAG_CAMERA_TO_ROBOT;
// import static frc.robot.Constants.VisionConstants.FIELD_LENGTH_METERS;
// import static frc.robot.Constants.VisionConstants.FIELD_WIDTH_METERS;

// import java.io.IOException;

// import org.photonvision.PhotonCamera;
// import org.photonvision.PhotonPoseEstimator;
// import org.photonvision.PhotonPoseEstimator.PoseStrategy;

// import edu.wpi.first.apriltag.AprilTagFieldLayout.OriginPosition;
// import edu.wpi.first.apriltag.AprilTagFields;
// import edu.wpi.first.math.VecBuilder;
// import edu.wpi.first.math.Vector;
// import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
// import edu.wpi.first.math.geometry.Pose2d;
// import edu.wpi.first.math.geometry.Rotation2d;
// import edu.wpi.first.math.geometry.Translation2d;
// import edu.wpi.first.math.numbers.N3;
// import edu.wpi.first.wpilibj.DriverStation;
// import edu.wpi.first.wpilibj.DriverStation.Alliance;
// import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
// import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
// import edu.wpi.first.wpilibj.smartdashboard.Field2d;
// import edu.wpi.first.wpilibj2.command.SubsystemBase;
// import frc.lib.util.SwerveModuleConstants;

// public class Vision {

//   private static final Vector<N3> stateStdDevs = VecBuilder.fill(0.05, 0.05, 0.1);
//   private static final Vector<N3> visionMeasurementStdDevs = VecBuilder.fill(0.5, 0.5, 0.9);

//   private final Field2d field2d = new Field2d();
//   private final PhotonPoseEstimator photonPoseEstimator;
//   private RobotContainer robot;

//   private double previousPipelineTimestamp = 0;
//   private OriginPosition originPosition = OriginPosition.kBlueAllianceWallRightSide;

//   public Vision(PhotonCamera photonCamera) {
//     PhotonPoseEstimator photonPoseEstimator;
//     try {
//       var layout = AprilTagFields.k2023ChargedUp.loadAprilTagLayoutField();
//       layout.setOrigin(originPosition);
//       photonPoseEstimator =
//           new PhotonPoseEstimator(layout, PoseStrategy.LOWEST_AMBIGUITY, photonCamera, APRILTAG_CAMERA_TO_ROBOT);
//     } catch(IOException e) {
//       DriverStation.reportError("Failed to load AprilTagFieldLayout", e.getStackTrace());
//       photonPoseEstimator = null;
//     }
//     this.photonPoseEstimator = photonPoseEstimator;

//     ShuffleboardTab tab = Shuffleboard.getTab("Vision");
//     }

//   /**
//    * Sets the alliance. This is used to configure the origin of the AprilTag map
//    * @param alliance alliance
//    */
//   public void setAlliance(Alliance alliance) {
//     var fieldTags = photonPoseEstimator.getFieldTags();
//     boolean allianceChanged = false;
//     switch(alliance) {
//       case Blue:
//         fieldTags.setOrigin(OriginPosition.kBlueAllianceWallRightSide);
//         allianceChanged = (originPosition == OriginPosition.kRedAllianceWallRightSide);
//         originPosition = OriginPosition.kBlueAllianceWallRightSide;
//         break;
//       case Red:
//         fieldTags.setOrigin(OriginPosition.kRedAllianceWallRightSide);
//         allianceChanged = (originPosition == OriginPosition.kBlueAllianceWallRightSide);
//         originPosition = OriginPosition.kRedAllianceWallRightSide;
//         break;
//       default:
//         // No valid alliance data. Nothing we can do about it
//     }
//     if (allianceChanged) {
//       // The alliance changed, which changes the coordinate system.
//       // Since a tag may have been seen and the tags are all relative to the coordinate system, the estimated pose
//       // needs to be transformed to the new coordinate system.
//       var newPose = flipAlliance(poseEstimator.getEstimatedPosition());
//       setCurrentPose(newPose);
//     }
//   }


//   private String getFomattedPose() {
//     var pose = getCurrentPose();
//     return String.format("(%.2f, %.2f) %.2f degrees", 
//         pose.getX(), 
//         pose.getY(),
//         pose.getRotation().getDegrees());
//   }

//   public Pose2d getCurrentPose() {
//     return poseEstimator.getEstimatedPosition();
//   }

//   /**
//    * Resets the current pose to the specified pose. This should ONLY be called
//    * when the robot's position on the field is known, like at the beginning of
//    * a match.
//    * @param newPose new pose
//    */
//   public void setCurrentPose(Pose2d newPose) {
//     //robot._drive.resetModulesToAbsolute();
//     poseEstimator.resetPosition(
//       robot._drive.getYaw(),
//       robot._drive.getModulePositions(),
//       newPose);
//   }

//   /**
//    * Resets the position on the field to 0,0 0-degrees, with forward being downfield. This resets
//    * what "forward" is for field oriented driving.
//    */
//   public void resetFieldPosition() {
//     setCurrentPose(new Pose2d());
//   }

//   /**
//    * Transforms a pose to the opposite alliance's coordinate system. (0,0) is always on the right corner of your
//    * alliance wall, so for 2023, the field elements are at different coordinates for each alliance.
//    * @param poseToFlip pose to transform to the other alliance
//    * @return pose relative to the other alliance's coordinate system
//    */
//   private Pose2d flipAlliance(Pose2d poseToFlip) {
//     return poseToFlip.relativeTo(new Pose2d(
//       new Translation2d(FIELD_LENGTH_METERS, FIELD_WIDTH_METERS),
//       new Rotation2d(Math.PI)));
//   }

// }