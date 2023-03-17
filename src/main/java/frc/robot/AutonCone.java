package frc.robot;

import frc.robot.ManipulatorControl.MANIPPOS;

public class AutonCone implements OpModeInterface
{
   private RobotContainer robot;

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
               robot._manipulator.clawRelease();
               step++;
            }
             break;
         case 1:
             if(robot._manipulator.clawGetPose() < 8){
                robot._manipulator.setManipPos(MANIPPOS.TRAVEL);
                step++;
             }
             break;
         default:
            break;
      }
   }
}
