package frc.robot;

import edu.wpi.first.wpilibj.motorcontrol.Spark;

public class LED{
   
   public static ManipulatorControl _manipulator = new ManipulatorControl();
   public static Spark _LED         = new Spark(0);
   long _endGameTimer
   long _liftTimer
   public void disable() {
      _LED.set(-0.49);
   }

   public void Init() {
     _endGameTimer = System.currentTimeMillis();
     _liftTimer = System.currentTimeMillis();
      _LED.set(-0.49); //Rainbow
   }

   public void Periodic() {
      if (System.currentTimeMillis() - (_endGameTimer /1000) > 138.0d) { //System Time is in Milliseconds We divide by 1000 to convert to seconds
         if (UI._coneCube()) 
         {
            _LED.set(0.91);  //purple
            // if (_manipulator.liftGetPose() < 50)   //FIXME
            // {
            //    liftTimer.start();
            //    if (liftTimer.get() > 0.0) 
            //    {
            //       if (liftTimer.get() < 1.5) 
            //       {
            //          _LED.set(0.77);
            //       }
            //    }
            // }
         } 
         else if (!UI._coneCube()) 
         {
            _LED.set(0.69);  //yellow
            // if (_manipulator.liftGetPose() < 50)   //FIXME
            // {
            //    liftTimer.start();
            //    if (liftTimer.get() > 0.0) 
            //    {
            //       if (liftTimer.get() < 1.5) 
            //       {
            //          _LED.set(0.77);
            //       }
            //    }
            // }
         } 
      } 
      else 
      {
         if ( (Math.round(_endGameTimer/1000) & 1) == 0 ) 
         {//If timer is even
            _LED.set(-0.25);  //red
         } 
         else 
         {//If the timer is odd
            if (UI._coneCube()) {
               _LED.set(0.91);  //purple
            } 
            else if (!UI._coneCube()) 
            {
               _LED.set(0.69);  //yellow
            }
         }
      }
   }
}
