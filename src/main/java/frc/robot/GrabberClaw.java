package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.playingwithfusion.TimeOfFlight;
import com.playingwithfusion.TimeOfFlight.RangingMode;

public class GrabberClaw
{
   private boolean  setCone = false;
   private boolean  grabbing = false;
   private CANSparkMax _claw;
   private SparkMaxPIDController pid_Claw;
   private RelativeEncoder en_Claw;
   private double kP_Claw, kI_Claw, kD_Claw, kIz_Claw, kFF_Claw, kMaxOutput_Claw, kMinOutput_Claw;
   private TimeOfFlight sensor = new TimeOfFlight(101);

   public GrabberClaw()
   {
      _claw = new CANSparkMax(53, MotorType.kBrushless);
      _claw.setInverted(false);
      _claw.enableSoftLimit(SoftLimitDirection.kReverse, true);
      _claw.setSoftLimit(SoftLimitDirection.kReverse, 1);
      _claw.enableSoftLimit(SoftLimitDirection.kForward, true);
      _claw.setSoftLimit(SoftLimitDirection.kForward, 20);
      _claw.setIdleMode(IdleMode.kBrake);
      kP_Claw         = 0.8;
      kI_Claw         = 0;
      kD_Claw         = 0;
      kIz_Claw        = 0;
      kFF_Claw        = 0;
      kMaxOutput_Claw = 0.65;
      kMinOutput_Claw = -0.65;
      pid_Claw = _claw.getPIDController();
      pid_Claw.setP(kP_Claw);
      pid_Claw.setI(kI_Claw);
      pid_Claw.setD(kD_Claw);
      pid_Claw.setIZone(kIz_Claw);
      pid_Claw.setFF(kFF_Claw);
      pid_Claw.setOutputRange(kMinOutput_Claw, kMaxOutput_Claw);
      en_Claw = _claw.getEncoder();
      sensor.setRangingMode(RangingMode.Short, 24);
   }
   public double getPose() {
      return en_Claw.getPosition();
   }
   public void periodic()
   {
      // close claw if grabbing and something in fron of sensor
      // open claw if not grabbing
      if ( grabbing && sensor.getRange() > 45 )
      {
         if ( setCone )
         {
            setPose(19.0);
         }
         else
         {
            setPose(14.0);
         }
      }
      else
      {
         setPose(2.0);
      }
   }

   private void setPose( double new_target)
   {
      pid_Claw.setReference(new_target, ControlType.kPosition);
   }
   public void grabCone( )
   {
      setCone = true;
      grabbing = true;
   }
   public void grabCube( )
   {
      setCone = false;
      grabbing = true;
   }
   public void release( )
   {
      grabbing = false;
   }
   public void disable()
   {
      grabbing = false;
      _claw.set(0.0);
   }
   public void sysPrints(){
      System.out.println("Claw Motor Temp: " + _claw.getMotorTemperature());
      System.out.println("Claw Motor Pos.: " + en_Claw.getPosition());
      System.out.println("Claw Motor Cur.: " + _claw.getOutputCurrent());
      System.out.println("---");
   }
}