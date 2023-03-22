package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import frc.robot.ManipulatorControl.MANIPPOS;

public class Auton2RightCS implements OpModeInterface
{
   private RobotContainer robot;
   private long _releaseTimer = 0;
//*****************************************THIS AUTON NOT CURRENTLY USED************************************************* */
   private int step = 0;
   private Pose2d startPoint  = new Pose2d(Units.feetToMeters(0), Units.feetToMeters(0),new Rotation2d(0.0));
   private Pose2d Path1[] = {
                    new Pose2d(Units.feetToMeters(10), Units.feetToMeters(-0.5),new Rotation2d(0.0)) }; //TODO test points only
   private Pose2d Path2[] = {
                    new Pose2d(Units.feetToMeters(15), Units.feetToMeters(-0.5),new Rotation2d(Math.PI)) };
   private Pose2d Path3[] = {
                    new Pose2d(Units.feetToMeters(0), Units.feetToMeters(-0.5),new Rotation2d(0.0)) };
   private Pose2d Path4[] = {
                    new Pose2d(Units.feetToMeters(0.25), Units.feetToMeters(5),new Rotation2d(0.0)),
                    new Pose2d(Units.feetToMeters(6.5), Units.feetToMeters(5),new Rotation2d(0.0)) };
    private SwervePath firstPath  = new SwervePath( Path1 );
    private SwervePath secondPath = new SwervePath( Path2 );
    private SwervePath thirdPath  = new SwervePath( Path3 );
    private SwervePath fourthPath = new SwervePath( Path4 );

   public Auton2RightCS()
   {
      robot = RobotContainer.getInstance();
   }
   public void Init()
   {
      step = 0;
      robot.resetOdometry(startPoint);
      robot._manipulator.clawGrabCube();
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
               robot._manipulator.clawRelease();
               _releaseTimer = ( System.currentTimeMillis() + 750 ) ; //One second delay
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
               _releaseTimer = ( System.currentTimeMillis() + 750 ); //One second delay
               step++;
            }
            break;
         case 4:
            if(System.currentTimeMillis() > _releaseTimer){
               robot._manipulator.setManipPos(MANIPPOS.TRAVEL);
               step++;
            }
            break;
         case 5:
            if( thirdPath.atDestination() )
            {
               robot._manipulator.setManipPos(MANIPPOS.TOP);
               step++;
            }
            break;
         case 6:
            if (robot._manipulator.atPosition() )
            {
               robot._manipulator.clawRelease();
               _releaseTimer = ( System.currentTimeMillis() + 750 ); //One second delay
               step++;
            }
            break;
         case 7:
            if(System.currentTimeMillis() > _releaseTimer){
               robot._manipulator.setManipPos(MANIPPOS.TRAVEL);
               step++;
            }
            break;
         case 8:
            if ( fourthPath.atDestination() )
            {
               robot._drive.chargeStationAutoLevel();
               step++;
            }
            break;
         case 9:
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
