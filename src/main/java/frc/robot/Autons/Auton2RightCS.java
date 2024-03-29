package frc.robot.Autons;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import frc.robot.OpModeInterface;
import frc.robot.RobotContainer;
import frc.robot.SwervePath;
import frc.robot.ManipulatorControl.MANIPPOS;

public class Auton2RightCS implements OpModeInterface
{
   private RobotContainer robot;
   private long _releaseTimer = 0;
//*****************************************THIS AUTON NOT CURRENTLY USED************************************************* */
   private int step = 0;
   private Pose2d startPoint  = new Pose2d(Units.feetToMeters(0), Units.feetToMeters(0),new Rotation2d(Math.PI));
   private Pose2d Path1[] = {
                    new Pose2d(Units.feetToMeters(11.0), Units.feetToMeters(-1),new Rotation2d(Math.PI)) }; //TODO test points only
   private Pose2d Path2[] = {
                    new Pose2d(Units.feetToMeters(11.0), Units.feetToMeters(-1),new Rotation2d(Units.degreesToRadians(2.0))) };
   private Pose2d Path3[] = {
                    new Pose2d(Units.feetToMeters(15.0), Units.feetToMeters(-1),new Rotation2d(Units.degreesToRadians(2.0))) };
   private Pose2d Path4[] = {
                    new Pose2d(Units.feetToMeters(14.5), Units.feetToMeters(-1),new Rotation2d(Units.degreesToRadians(179.0))),
                    new Pose2d(Units.feetToMeters(0.8), Units.feetToMeters(-2.7),new Rotation2d(Units.degreesToRadians(179.0))) };
   // private Pose2d Path5[] = {
   //                  new Pose2d(Units.feetToMeters(0.5), Units.feetToMeters(5),new Rotation2d(Math.PI)),
   //                  new Pose2d(Units.feetToMeters(8.25), Units.feetToMeters(5),new Rotation2d(Math.PI)) };
   private SwervePath firstPath  = new SwervePath( Path1, 1.5, -1.5, 1.5, -1.5 );
   private SwervePath secondPath = new SwervePath( Path2, 1.5, -1.5, 1.5, -1.5 );
   private SwervePath thirdPath  = new SwervePath( Path3, 1.5, -1.5, 1.5, -1.5 );
   private SwervePath fourthPath = new SwervePath( Path4, 1.5, -1.5, 1.5, -1.5 );
   //private SwervePath fifthPath  = new SwervePath( Path5 );

   public Auton2RightCS()
   {
      robot = RobotContainer.getInstance();
   }
   public void Init()
   {
      step = 0;
      robot.resetOdometry(startPoint);
      robot._manipulator.clawGrabCube();
      robot._manipulator.setManipPos( MANIPPOS.AUTONCUBE );
      robot._manipulator.periodic();
   }
   public void Periodic()
   {
      switch( step )
      {
         case 0:
            if (robot._manipulator.atPosition() )
            {
               robot._manipulator.clawRelease();
               _releaseTimer = ( System.currentTimeMillis() + 250 ) ; //750 millisecond delay
               step++;
            }
             break;
         case 1:
            if(System.currentTimeMillis() > _releaseTimer){
               robot._manipulator.setManipPos(MANIPPOS.TRAVEL);
               step++;
            }
            break;
         case 2:
            if ( firstPath.atDestination() )
            {
               robot._manipulator.setManipPos(MANIPPOS.FLOOR);
               step++;
            }
            break;
         case 3:
            if ( secondPath.atDestination() )
            {
               robot._manipulator.clawGrabCone();
               step++; 
            }
            break;
         case 4:
            if ( robot._manipulator.atPosition() )
            {
               step++;
            }
            break;
         case 5:
            robot._manipulator.clawGrabCone();
            if( thirdPath.atDestination() ) 
               {
                  robot._manipulator.clawGrabCone();
                  _releaseTimer = ( System.currentTimeMillis() + 250 ); //750 millisecond delay
                  step++;
               }
            break;
         case 6:
            if(System.currentTimeMillis() > _releaseTimer){
               robot._manipulator.setManipPos(MANIPPOS.TRAVEL);
               step++;
            }
            break;
         case 7:
            if( fourthPath.atDestination() )
            {
               robot._manipulator.setManipPos(MANIPPOS.MID);
               step++;
            }
            break;
         case 8:
            if (robot._manipulator.atPosition() )
            {
               robot._manipulator.clawRelease();
               _releaseTimer = ( System.currentTimeMillis() + 750 ); //750 millisecond delay
               step++;
            }
            break;
         case 9:
            if(System.currentTimeMillis() > _releaseTimer){
               robot._manipulator.setManipPos(MANIPPOS.TRAVEL);
               step++;
            }
            break;
         case 10:
            //if ( fifthPath.atDestination() )
            //{
            //   robot._drive.chargeStationAutoLevel();
               step++;
            //}
            break;
         case 11:
            if (robot._drive.chargeStationAutoLevel())
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
