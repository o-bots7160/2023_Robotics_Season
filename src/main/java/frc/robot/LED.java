package frc.robot;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;

import com.playingwithfusion.TimeOfFlight;
import com.playingwithfusion.TimeOfFlight.RangingMode;



public class LED{
	//private RobotContainer robot;
	private AddressableLED m_led; //Main LED
	private AddressableLEDBuffer m_ledBuffer; //Main LED Buffer
	private int m_rainbowFirstPixelHue; // Store what the last hue of the first pixel is
	private AddressableLED s_led; //Secondary LED
	private AddressableLEDBuffer s_ledBuffer; //Secondary LED Buffer
	private final TimeOfFlight _tof;
   
	private long _endGameTimer;
	private long _liftTimer; 
	double minRead = 50; //Min read distance for TOF sensor
	double maxRead = 1500; //Max read distance for TOF sensor

   private boolean travelPressed = false;
   public LED( TimeOfFlight new_tof){
      //robot = RobotContainer.getInstance();
		_tof = new_tof;
   }

   public void disable() {
      m_led.setData(m_ledBuffer);
      rainbow();
   }

   public void Init() {
      _endGameTimer = ( System.currentTimeMillis() / 1000 ) + 122; //138
      _liftTimer = 0;
      travelPressed = false;
      
	   
      //_tof.setRangingMode    ( RangingMode.Short, 24.0d );
	  
		m_led = new AddressableLED(0); //Main LEDs PWM port 9
		m_ledBuffer = new AddressableLEDBuffer(40); //Main LED Length
		m_led.setLength(m_ledBuffer.getLength());      
		m_led.start();
		
		/* 
      s_led = new AddressableLED(0); //Secondary LEDs PWM port 8
		s_ledBuffer = new AddressableLEDBuffer(40); //Secondary LED Length
		s_led.setLength(s_ledBuffer.getLength());
		s_led.start();		
      */

      
	  
	  
   }

   public void Periodic( double liftPose ) {
      m_led.setData(m_ledBuffer);
		//s_led.setData(s_ledBuffer);
		//newDistance();
      if (UI._travel()) 
      {
         travelPressed = true;
      }

      if ( travelPressed && ( liftPose < 75) ) 
      {
         _liftTimer = System.currentTimeMillis() + 1500;
         travelPressed = false;
      }

      if ( System.currentTimeMillis() < _liftTimer ) 
      {
         green();
      }
      else if ( ( (System.currentTimeMillis()/1000)  > _endGameTimer ) &&
                ( ( (System.currentTimeMillis()/1000) % 2) == 0 ) ) 
      {
         red();
      }
      else
      {
         if ( UI._coneCube() )
         {
            purple(); 
         }
         else 
         {
            yellow(); 
         }
      }
   }
   
	private void rainbow() {
    
		for (var i = 0; i < m_ledBuffer.getLength(); i++) {
      		final var hue = (m_rainbowFirstPixelHue + (i * 180 / m_ledBuffer.getLength())) % 180;
      		m_ledBuffer.setHSV(i, hue, 255, 128);  
        
		}
		// Increase by to make the rainbow "move"
		m_rainbowFirstPixelHue += 3;
		// Check bounds
		m_rainbowFirstPixelHue %= 180;
	}
  
	private void red(){
		for (int r = 0; r < m_ledBuffer.getLength(); r++) {
			//m_ledBuffer.setRGB(r, 255, 0, 0);
			m_ledBuffer.setLED(r, Color.kRed);
		}
		  
	}
  
	private void green(){
		for (int g = 0; g < m_ledBuffer.getLength(); g++) {
			//m_ledBuffer.setRGB(g, 0, 255, 0);
         m_ledBuffer.setLED(g, Color.kGreen);
		}
	  
	}
	
	private void purple(){
		 for (int p = 0; p < m_ledBuffer.getLength(); p++) {
			m_ledBuffer.setLED(p, Color.kPurple);
		} 
	}
  
	private void yellow(){
		 for (int y = 0; y < m_ledBuffer.getLength(); y++) {
			m_ledBuffer.setLED(y, Color.kYellow);
		}
	}
	
  /*
	private void newDistance(){
		double rob_Distance = (_tof.getRange() < minRead ||_tof.getRange() > maxRead)? maxRead : _tof.getRange();
		float percent = (float) ((rob_Distance - minRead)/(maxRead-minRead));
		//SmartDashboard.putNumber("Rob", rob_Distance);
		int numOflEDS = Math.round((float) s_ledBuffer.getLength() * (1-percent));
		

		// Turn the leds red as we get closer
		for (int x = 0; x < numOflEDS; x++) {
		  s_ledBuffer.setRGB(x, 255, 0, 0);
		}
		
		// Have the remaining leds off
		for (int x = numOflEDS; x < s_ledBuffer.getLength(); x++) {
		  s_ledBuffer.setRGB(x, 0, 0, 0);
		}
    

	}
	*/
   
}
