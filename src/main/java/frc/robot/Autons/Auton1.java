package frc.robot.Autons;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
//import edu.wpi.first.wpilibj.DriverStation;
//import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.OpModeInterface;
import frc.robot.RobotContainer;
import frc.robot.SwervePath;
import frc.robot.ManipulatorControl.MANIPPOS;

public class Auton1 implements OpModeInterface
{
   private RobotContainer robot;
   private long _releaseTimer = 0;

   private int step = 0;
   private Pose2d startPoint  = new Pose2d(Units.feetToMeters(0), Units.feetToMeters(0),new Rotation2d(Math.PI));
   private Pose2d Path1[] = {
                    new Pose2d(Units.feetToMeters(13), Units.feetToMeters(0),new Rotation2d(Math.PI))
                     };
    private SwervePath firstPath  = new SwervePath( Path1, 1.5, -1.5, 1.0, -1.0 );

   public Auton1()
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
               //System.out.println( "")
               step++;
               robot._manipulator.clawRelease();
               robot._manipulator.setManipPos(MANIPPOS.TRAVEL);
            }
             break;
         case 1:
             //if(robot._manipulator.clawGetPose() < 10){
                robot._manipulator.setManipPos(MANIPPOS.TRAVEL);
                step++;
             //}
             break;
         case 2:
            if (robot._manipulator.atPosition() )
            {
               //System.out.println( "")
               robot._manipulator.clawRelease();
               //robot._manipulator.setManipPos(MANIPPOS.TRAVEL);
               _releaseTimer = ( System.currentTimeMillis() + 750 ) ; //One second delay
               step++;
            }
            break;
          case 3:
          if(System.currentTimeMillis() > _releaseTimer){
             robot._manipulator.setManipPos(MANIPPOS.TRAVEL);
             step++;
          }
          break;
         case 4:
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
