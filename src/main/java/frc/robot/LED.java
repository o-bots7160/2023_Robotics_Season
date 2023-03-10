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
      if (endGameTimer.get() < 138.0d) {
         if (UI._coneCube()) {
            _LED.set(0.91);  //purple
         } else if (!UI._coneCube()) {
            _LED.set(0.69);  //yellow
         } else {
            _LED.set(-0.49);
          } 
       } else {
          _LED.set(-0.25);
       }
   }
}
