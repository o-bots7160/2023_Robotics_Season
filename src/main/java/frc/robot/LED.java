package frc.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.motorcontrol.Spark;

public class LED{
   
   public static ManipulatorControl _manipulator = new ManipulatorControl();
   public static Spark _LED         = new Spark(0);
   public static Timer liftTimer    = new Timer();
   private Timer endGameTimer       = new Timer();

   public void disable() {
      _LED.set(-0.49);
   }

   public void Init() {
      endGameTimer.reset();
      endGameTimer.start();
      _LED.set(-0.49);
   }

   public void Periodic() {
      if (endGameTimer.get() < 50.0d) { //138
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
         if ( (Math.round(endGameTimer.get()) & 1) == 0 ) 
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
