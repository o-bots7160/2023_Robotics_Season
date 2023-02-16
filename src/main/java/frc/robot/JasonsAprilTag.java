package frc.robot;



import java.io.IOException;
import java.util.List;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonTrackedTarget;
import org.photonvision.targeting.TargetCorner;

import edu.wpi.first.apriltag.AprilTagDetection;
import edu.wpi.first.apriltag.AprilTagDetector;
import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Robot extends TimedRobot {
  Transform3d CAMERA_TO_ROBOT =new Transform3d( new Translation3d( 0.0, -0.1375, -0.90), new Rotation3d( 0.0, 0.0, -0.10));

  PhotonCamera camera = new PhotonCamera("Cam1");
  @Override
  public void robotInit() {
   
  }

  @Override
  public void robotPeriodic() {
    try{
    AprilTagFieldLayout aprilTagFieldLayout = AprilTagFieldLayout.loadFromResource(AprilTagFields.k2023ChargedUp.m_resourceFile);
        // Query the latest result from PhotonVision
          var result = camera.getLatestResult();
        // Check if the latest result has any targets.
          boolean hasTargets = result.hasTargets();
        // Get a list of currently tracked targets.
        if(hasTargets){
        List<PhotonTrackedTarget> targets = result.getTargets();
        // Get the current best target.
          PhotonTrackedTarget target = result.getBestTarget();
        // Get information from target.
        int tarId = target.getFiducialId();
        //double yaw = target.getYaw();
        //double pitch = target.getPitch();
        //double area = target.getArea();
        //double skew = target.getSkew();
        //double[] targetLoc = new double[]{tarId, yaw, pitch, area, skew};
        //SmartDashboard.putNumberArray("Target Loc", targetLoc);
        SmartDashboard.putNumber("Target ID: ", tarId);
        //List<TargetCorner> corners = target.getCorners();
        // Get information from target.
        //int targetID = target.getFiducialId();

        double poseAmbiguity = target.getPoseAmbiguity();
        Pose3d targetPose = aprilTagFieldLayout.getTagPose(tarId).get();
        Transform3d bestCameraToTarget = target.getBestCameraToTarget();
        Pose3d camPose = targetPose.transformBy(bestCameraToTarget.inverse());
        var visionMeasurement = camPose.transformBy(CAMERA_TO_ROBOT);
        
        //Transform3d alternateCameraToTarget = target.getAlternateCameraToTarget();
        //SmartDashboard.putNumber("PoseA", poseAmbiguity);
        double camX = bestCameraToTarget.getX();
        double camY = bestCameraToTarget.getY();
        double camA = Math.toDegrees(bestCameraToTarget.getRotation().getAngle());
        double fieldX = aprilTagFieldLayout.getTagPose(tarId).get().getX();
        double fieldY = aprilTagFieldLayout.getTagPose(tarId).get().getY();
        SmartDashboard.putNumber("X: ", camX);
        SmartDashboard.putNumber("Y: ", camY);
        SmartDashboard.putNumber("Angle: ", camA);
        SmartDashboard.putNumber("Target X: ", fieldX);
        SmartDashboard.putNumber("Target Y: ", fieldY);
        SmartDashboard.putNumber("Actual X", visionMeasurement.toPose2d().getX());
        SmartDashboard.putNumber("Actual Y", visionMeasurement.toPose2d().getY());
        SmartDashboard.putNumber("Actual ROT", visionMeasurement.toPose2d().getRotation().getDegrees());
        }
    }catch(IOException e){
      DriverStation.reportError("Failed to load AprilTagFieldLayout", e.getStackTrace());
    }

     
  }
 

}
