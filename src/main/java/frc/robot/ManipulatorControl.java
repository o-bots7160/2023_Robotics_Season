package frc.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.CounterBase.EncodingType;

public class ManipulatorControl {

   private CANSparkMax lift;
   private SparkMaxPIDController pid_Lift;
   private RelativeEncoder en_Lift;
   public double kP_Lift, kI_Lift, kD_Lift, kIz_Lift, kFF_Lift, kMaxOutput_Lift, kMinOutput_Lift;


   private TalonSRX extention;

   private CANSparkMax wrist;
   private SparkMaxPIDController pid_Wrist;
   private RelativeEncoder en_Wrist;
   public double kP_Wrist, kI_Wrist, kD_Wrist, kIz_Wrist, kFF_Wrist, kMaxOutput_Wrist, kMinOutput_Wrist;

   private CANSparkMax claw;
   private SparkMaxPIDController pid_Claw;
   private RelativeEncoder en_Claw;
   public double kP_Claw, kI_Claw, kD_Claw, kIz_Claw, kFF_Claw, kMaxOutput_Claw, kMinOutput_Claw;


   public ManipulatorControl(){
      liftInit();
      extentionInit();
      wristInit();
      clawInit();
   }

   private void liftInit(){
      lift = new CANSparkMax(50, MotorType.kBrushless);
      kP_Lift         = 0;
      kI_Lift         = 0;
      kD_Lift         = 0;
      kIz_Lift        = 0;
      kFF_Lift        = 0;
      kMaxOutput_Lift = 0;
      kMinOutput_Lift = 0;
      pid_Lift = lift.getPIDController();
      pid_Lift.setP(kP_Lift);
      pid_Lift.setI(kI_Lift);
      pid_Lift.setD(kD_Lift);
      pid_Lift.setIZone(kIz_Lift);
      pid_Lift.setFF(kFF_Lift);
      pid_Lift.setOutputRange(kMinOutput_Lift, kMaxOutput_Lift);
      en_Lift = lift.getEncoder();
   }

   private void extentionInit(){
      extention = new TalonSRX(51);
   }

   private void wristInit(){
      wrist = new CANSparkMax(52, MotorType.kBrushless);
      kP_Wrist         = 0;
      kI_Wrist         = 0;
      kD_Wrist         = 0;
      kIz_Wrist        = 0;
      kFF_Wrist        = 0;
      kMaxOutput_Wrist = 0;
      kMinOutput_Wrist = 0;
      pid_Wrist = wrist.getPIDController();
      pid_Wrist.setP(kP_Wrist);
      pid_Wrist.setI(kI_Wrist);
      pid_Wrist.setD(kD_Wrist);
      pid_Wrist.setIZone(kIz_Wrist);
      pid_Wrist.setFF(kFF_Wrist);
      pid_Wrist.setOutputRange(kMinOutput_Wrist, kMaxOutput_Wrist);
      en_Wrist = wrist.getEncoder();
   }

   private void clawInit(){
      claw = new CANSparkMax(53, MotorType.kBrushless);
      kP_Claw         = 0;
      kI_Claw         = 0;
      kD_Claw         = 0;
      kIz_Claw        = 0;
      kFF_Claw        = 0;
      kMaxOutput_Claw = 0;
      kMinOutput_Claw = 0;
      pid_Claw = claw.getPIDController();
      pid_Claw.setP(kP_Claw);
      pid_Claw.setI(kI_Claw);
      pid_Claw.setD(kD_Claw);
      pid_Claw.setIZone(kIz_Claw);
      pid_Claw.setFF(kFF_Claw);
      en_Claw = claw.getEncoder();
   }
   
}
