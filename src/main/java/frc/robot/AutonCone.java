package frc.robot;

import frc.robot.ManipulatorControl.MANIPPOS;

public class AutonCone implements OpModeInterface
{
   private RobotContainer robot;
   private long _releaseTimer = 0;
   private int step = 0;

   public AutonCone()
   {
      robot = RobotContainer.getInstance();
   }
   public void Init()
   {
      step = 0; 
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
               robot._manipulator.clawRelease();
               //robot._manipulator.setManipPos(MANIPPOS.TRAVEL);
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
         default:
            break;
      }
   }
}
