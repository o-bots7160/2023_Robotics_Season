package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class AutonPose implements OpModeInterface
{
   private RobotContainer robot;

   private Pose2d startPoint  = new Pose2d(Units.feetToMeters(0), Units.feetToMeters(0),new Rotation2d(Math.PI));

   public AutonPose()
   {
      robot = RobotContainer.getInstance();
   }
   public void Init()
   {
      if (DriverStation.getAlliance() == Alliance.Red ) {
         startPoint = SwervePath.transform(startPoint);
      }
      robot.resetOdometry(startPoint);
   }
   public void Periodic()
   {
   }
}
