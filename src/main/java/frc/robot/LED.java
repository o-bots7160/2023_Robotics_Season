package frc.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.motorcontrol.Spark;

public class LED{
   
   public static Spark _LED     = new Spark(0);
   private Timer endGameTimer   = new Timer();

   public void disable() {
      _LED.set(-0.49);
   }

   public void Init() {
      endGameTimer.reset();
      endGameTimer.start();
      _LED.set(-0.49);
   }

   public void Periodic() {
      if (endGameTimer.get() < 238.0d) {
         if (UI._coneCube() == true) {
            _LED.set(0.91);
         } else if (UI._coneCube() == false) {
            _LED.set(0.69);
         } else {
            _LED.set(-0.49);
         } 
      } else {
         _LED.set(-0.25);
      }
   }
}
