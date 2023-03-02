package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.ManipulatorControl.MANIPPOS;

public class Auton1LeftCS implements OpModeInterface
{
   private RobotContainer robot;

   private int step = 0;
   private Pose2d startPoint  = new Pose2d(Units.feetToMeters(0), Units.feetToMeters(13.3),new Rotation2d(Math.PI));
   private Pose2d Path1[] = {
                    new Pose2d(Units.feetToMeters(12), Units.feetToMeters(13.3),new Rotation2d(Math.PI)),
                    new Pose2d(Units.feetToMeters(12), Units.feetToMeters(6),new Rotation2d(Math.PI)),
                    new Pose2d(Units.feetToMeters(5.25), Units.feetToMeters(6),new Rotation2d(Math.PI))
                  
                     };
    private SwervePath firstPath  = new SwervePath( Path1 );

   public Auton1LeftCS()
   {
      robot = RobotContainer.getInstance();
   }
   public void Init()
   {
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
               //System.out.println( "")
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
