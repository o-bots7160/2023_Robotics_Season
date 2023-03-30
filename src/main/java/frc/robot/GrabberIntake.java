package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.playingwithfusion.TimeOfFlight;
import com.playingwithfusion.TimeOfFlight.RangingMode;

public class GrabberIntake
{
   private boolean  setCone = false;
   private boolean  grabbing = false;
   private CANSparkMax _intake;
   private TimeOfFlight sensor = new TimeOfFlight(101);

   public GrabberIntake()
   {
      _intake = new CANSparkMax(53, MotorType.kBrushless);
      _intake.setInverted(false);
      _intake.enableSoftLimit(SoftLimitDirection.kReverse, false);
      _intake.enableSoftLimit(SoftLimitDirection.kForward, false);
      _intake.setIdleMode(IdleMode.kBrake);
      sensor.setRangingMode(RangingMode.Short, 24);
   }
   public void periodic()
   {
      //System.out.println("Distance: " + sensor.getRange());
      if ( grabbing )
      {
         if ( setCone && sensor.getRange() < 60 )
         {
            _intake.set( 0.0 );
         }
         else if ( !setCone && sensor.getRange() < 85)
         {
            _intake.set( 0.0 );
         }
         else if ( sensor.getRange() > 275)
         {
            _intake.set( 0.0 );
         }
      }
      else
      {
         if ( setCone && sensor.getRange() > 200 )
         {
            _intake.set( 0.0 );
         }
         else if ( !setCone && sensor.getRange() > 400 )
         {
            _intake.set( 0.0 );
         }
         else if ( sensor.getRange() < 30 )
         {
            _intake.set( 0.0 );
         }
      }
   }

   public void grabCone( )
   {
      setCone = true;
      grabbing = true;
      _intake.set(1.0);
      System.out.println("Cone");
   }
   public void grabCube( )
   {
      setCone = false;
      grabbing = true;
      _intake.set( 1.0 );
      System.out.println("Cube");
   }
   public void release( )
   {
      grabbing = false;
      _intake.set( -1.0 );
      
      System.out.println("Release");
   }
   public void disable()
   {
      grabbing = false;
      _intake.set(0.0);
   }
   public void sysPrints(){
      System.out.println("Claw Motor Temp: " + _intake.getMotorTemperature());
      System.out.println("Claw Motor Cur.: " + _intake.getOutputCurrent());
      System.out.println("---");
   }
}