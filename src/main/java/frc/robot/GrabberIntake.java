package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import com.playingwithfusion.TimeOfFlight;

public class GrabberIntake
{
   private boolean      setCone         = false;
   private boolean      grabbing        = false;
   private boolean      was_grabbing    = false;
   private double       target_distance = 0.0;
   private CANSparkMax  _intake;
   private TimeOfFlight sensor;

   public GrabberIntake( TimeOfFlight sensor )
   {
      _intake = new CANSparkMax(53, MotorType.kBrushless);
      _intake.setInverted(false);
      _intake.enableSoftLimit(SoftLimitDirection.kReverse, false);
      _intake.enableSoftLimit(SoftLimitDirection.kForward, false);
      _intake.setIdleMode(IdleMode.kBrake);
      this.sensor = sensor;
   }
   public void periodic( double wristPos )
   {
      double distance = sensor.getRange();
      if ( sensor.isRangeValid() )
      {
         if ( grabbing != was_grabbing )
         {
            if ( was_grabbing )
            {
               System.out.println("Release start");
               _intake.set( -0.75 );
            }
            else
            {
               System.out.println("intake start");
               _intake.set(1.0);
            }
            was_grabbing = grabbing;
         }
         else
         {
            if ( was_grabbing )
            {
               if ( distance < target_distance )
               {
                  System.out.println("intake stop1");
                  _intake.set( 0.0 );
               }
            }
            else
            {
               if ( distance > target_distance )
               {
                  System.out.println("intake stop2");
                  _intake.set( 0.0 );
               }
            }
         }
      }
      else
      {
         // if ( grabbing != was_grabbing )
         // {
         //    _intake.set( 0.0 );
         //    was_grabbing = grabbing;
         // }
      }
   }

   public void grabCone( )
   {
      if ( !grabbing )
      {
         setCone         = true;
         grabbing        = true;
         target_distance = 60;  // if less than this distance we've captured the cone
         System.out.println("Cone");
      }
   }
   public void grabCube( )
   {
      if ( ! grabbing )
      {
         setCone         = false;
         grabbing        = true;
         target_distance = 85;  // if less than this distance we've captured the cube
         System.out.println("Cube");
      }
   }
   public void release( )
   {
      if (grabbing )
      {
         grabbing = false;
         if (setCone )
         {
            target_distance = 200; // if greater than this distance we've released the cone
         }
         else
         {
            target_distance = 279; // if greater than this distance we've released the cube
         }
         System.out.println("Release");
   }
   }
   public void disable()
   {
      grabbing     = false;
      was_grabbing = false;
      _intake.set(0.0);
   }
   public void sysPrints(){
      System.out.println("Intk Motor Temp: " + _intake.getMotorTemperature());
      System.out.println("Intk Motor Cur.: " + _intake.getOutputCurrent());
      System.out.println("Intk Distance: "   + sensor.getRange());
      System.out.println("Intk valid: "      + sensor.isRangeValid());
      //System.out.println("Cone");
      System.out.println("---");
   }
}