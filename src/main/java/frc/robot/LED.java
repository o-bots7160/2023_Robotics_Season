package frc.robot;

import edu.wpi.first.wpilibj.motorcontrol.Spark;

public class LED{
   private RobotContainer robot;
   
   
   public static Spark _LED         = new Spark(0);
   private long _endGameTimer;
   private long _liftTimer;

   private boolean travelPressed = false;
   public LED(){
      robot = RobotContainer.getInstance();
   }

   public void disable() {
      _LED.set(-0.49);
   }

   public void Init() {
      _endGameTimer = ( System.currentTimeMillis() / 1000 ) + 122; //138
      _LED.set(-0.49); //Rainbow
      _liftTimer = 0;
      travelPressed = false;
   }

   public void Periodic() {
      if (UI._travel()) 
      {
         travelPressed = true;
      }

      if ( travelPressed && (robot._manipulator.liftGetPose() < 75) ) 
      {
         _liftTimer = System.currentTimeMillis() + 1500;
         travelPressed = false;
      }

      if ( System.currentTimeMillis() < _liftTimer ) 
      {
         _LED.set(0.75);  //green
      }
      else if ( ( (System.currentTimeMillis()/1000)  > _endGameTimer ) &&
                ( ( (System.currentTimeMillis()/1000) % 2) == 0 ) ) 
      {
         _LED.set(0.61);  //red
      }
      else
      {
         if ( UI._coneCube() )
         {
            _LED.set(0.91);  //purple
         }
         else 
         {
            _LED.set(0.67);  //yellow
         }
      }
   }
}
