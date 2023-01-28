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
      pid_Lift = lift.getPIDController();
      en_Lift = lift.getEncoder();
   }

   private void extentionInit(){
      extention = new TalonSRX(51);
   }

   private void wristInit(){
      wrist = new CANSparkMax(52, MotorType.kBrushless);
      pid_Wrist = wrist.getPIDController();
      en_Wrist = wrist.getEncoder();
   }

   private void clawInit(){
      claw = new CANSparkMax(53, MotorType.kBrushless);
      pid_Claw = claw.getPIDController();
      en_Claw = claw.getEncoder();
   }
   
}
