package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.ManipulatorControl.MANIPPOS;

public class Auton1RightCS implements OpModeInterface
{
   private RobotContainer robot;

   private int step = 0;
   private Pose2d startPoint  = new Pose2d(Units.feetToMeters(0), Units.feetToMeters(0),new Rotation2d(Math.PI));
   private Pose2d Path1[] = {
                    new Pose2d(Units.feetToMeters(13), Units.feetToMeters(0),new Rotation2d(Math.PI)),
                    new Pose2d(Units.feetToMeters(13), Units.feetToMeters(6),new Rotation2d(Math.PI)),
                    new Pose2d(Units.feetToMeters(5.25), Units.feetToMeters(6),new Rotation2d(Math.PI))
                  
                     };
    private SwervePath firstPath  = new SwervePath( Path1 );

   public Auton1RightCS()
   {
      
      robot = RobotContainer.getInstance();
   }
   public void Init()
   {
      // if (DriverStation.getAlliance() == Alliance.Red ) {
      //    System.out.println("RED");
      //    startPoint = SwervePath.transform(startPoint);
      //    firstPath.swapSides();

      //    //secondPath.swapSides();
      // }
      step = 0;
      robot.resetOdometry(startPoint);
      robot._manipulator.clawGrabCone();
      robot._manipulator.setManipPos( MANIPPOS.TOP );
      robot._manipulator.periodic();
}
   public void Periodic()
   {
      switch( step )
      {
         case 0:
            if (robot._manipulator.atPosition() )
            {
               step++;
               robot._manipulator.clawRelease();
               robot._manipulator.setManipPos(MANIPPOS.TRAVEL);
            }
             break;
         case 1:
            if ( robot._manipulator.atPosition() && firstPath.atDestination())
            {
               robot._drive.lock();
               step++;
            }
            break;
         default:
            break;
      }
   }
}
