package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import edu.wpi.first.wpilibj.*;
import com.revrobotics.CANSparkMax;
import com.revrobotics.MotorFeedbackSensor;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

//import edu.wpi.first.wpilibj.;

public class ManipulatorControl {

   // Mid lift pos cone = 182
   // Mid lift pos cube = 128

   // Top lift pos cone = 235
   // Top lift pos cube = 200

   private CANSparkMax _lift;
   private SparkMaxPIDController pid_Lift;
   private RelativeEncoder en_Lift;
   private double kP_Lift, kI_Lift, kD_Lift, kIz_Lift, kFF_Lift, kMaxOutput_Lift, kMinOutput_Lift;


   private TalonFX _extension;

   private CANSparkMax _wrist;
   private SparkMaxPIDController pid_Wrist;
   private RelativeEncoder en_Wrist;
   private double kP_Wrist, kI_Wrist, kD_Wrist, kIz_Wrist, kFF_Wrist, kMaxOutput_Wrist, kMinOutput_Wrist;

   private CANSparkMax _claw;
   private SparkMaxPIDController pid_Claw;
   private RelativeEncoder en_Claw;
   private double kP_Claw, kI_Claw, kD_Claw, kIz_Claw, kFF_Claw, kMaxOutput_Claw, kMinOutput_Claw;


   public void init(){
      liftInit();
      extensionInit();
      wristInit();
      clawInit();
   }
   public void periodic()
   {
      //extensionPeriodic();
      //System.out.println("Moving extension");
   }
   public void disable() 
   {
      extensionDisable();
   }

   private void liftInit(){
      _lift = new CANSparkMax(50, MotorType.kBrushless);
      _lift.setInverted(true);
      _lift.enableSoftLimit(SoftLimitDirection.kReverse, true);
      _lift.setSoftLimit(SoftLimitDirection.kReverse, 0);        //lower limit
      _lift.enableSoftLimit(SoftLimitDirection.kForward, true);
      _lift.setSoftLimit(SoftLimitDirection.kForward, 235);      //upper limit
      _lift.setIdleMode(IdleMode.kCoast);
      kP_Lift         =  0.8;
      kI_Lift         =  0;
      kD_Lift         =  0;
      kIz_Lift        =  0;
      kFF_Lift        =  0;
      kMaxOutput_Lift =  0.85;
      kMinOutput_Lift = -0.85;
      pid_Lift = _lift.getPIDController();
      //pid_Lift.setFeedbackDevice()
      pid_Lift.setP(kP_Lift);
      pid_Lift.setI(kI_Lift);
      pid_Lift.setD(kD_Lift);
      pid_Lift.setIZone(kIz_Lift);
      pid_Lift.setFF(kFF_Lift);
      pid_Lift.setOutputRange(kMinOutput_Lift, kMaxOutput_Lift);
      en_Lift = _lift.getEncoder();
   }

   public double liftGetPose() {
      return en_Lift.getPosition();
   }

   public void liftSetPose( double new_target) {
      pid_Lift.setReference(new_target, ControlType.kPosition);
   }

   private void extensionInit(){
      _extension = new TalonFX(51);
      _extension.configFactoryDefault();
    _extension.configReverseSoftLimitThreshold( 0.0, 0);      //lower limit
    _extension.configForwardSoftLimitThreshold( 265000.0, 0); //upper limit
    _extension.configForwardSoftLimitEnable(true, 0);
    _extension.configReverseSoftLimitEnable(true, 0);
    _extension.config_kP( 0, 0.06, 30);
    //_extension.config_kI( 0, 0.0, 0);
    //_extension.config_kD( 0, 0.0, 0);
    //_extension.config_IntegralZone( 0, 0.0, 0);
    _extension.setNeutralMode( NeutralMode.Coast);

   }

   private void extensionPeriodic() {
      //_extension.set(ControlMode.PercentOutput, -0.1); // neg = in, pos = out
   }

   private void extensionDisable() {
      _extension.set(ControlMode.PercentOutput, 0.0);
   }

   public double extGetPose() {
      return _extension.getSelectedSensorPosition();
   }

   public void extSetPose( double new_target) {
      _extension.set(ControlMode.Position, new_target);
   }

   private void wristInit(){
      _wrist = new CANSparkMax(52, MotorType.kBrushless);
      _wrist.setInverted(false);
      _wrist.enableSoftLimit(SoftLimitDirection.kReverse, true);
      _wrist.setSoftLimit(SoftLimitDirection.kReverse, -30);        //lower limit
      _wrist.enableSoftLimit(SoftLimitDirection.kForward, true);
      _wrist.setSoftLimit(SoftLimitDirection.kForward, 0);      //upper limit
      _wrist.setIdleMode(IdleMode.kBrake);
      kP_Wrist         =  0.1;
      kI_Wrist         =  0;
      kD_Wrist         =  0;
      kIz_Wrist        =  0;
      kFF_Wrist        =  0;
      kMaxOutput_Wrist =  0.07;
      kMinOutput_Wrist = -0.07;
      pid_Wrist = _wrist.getPIDController();
      pid_Wrist.setP(kP_Wrist);
      pid_Wrist.setI(kI_Wrist);
      pid_Wrist.setD(kD_Wrist);
      pid_Wrist.setIZone(kIz_Wrist);
      pid_Wrist.setFF(kFF_Wrist);
      pid_Wrist.setOutputRange(kMinOutput_Wrist, kMaxOutput_Wrist);
      en_Wrist = _wrist.getEncoder();
   }

   public double wristGetPose() {
      return en_Wrist.getPosition();
   }

   public void wristSetPose( double new_target) {
      pid_Wrist.setReference(new_target, ControlType.kPosition);
   }

   // 9.5 Cube
   // 18.5 Cone

   private void clawInit(){
      _claw = new CANSparkMax(53, MotorType.kBrushless);
      _claw.setInverted(false);
      _claw.enableSoftLimit(SoftLimitDirection.kReverse, true);
      _claw.setSoftLimit(SoftLimitDirection.kReverse, 4);
      _claw.enableSoftLimit(SoftLimitDirection.kForward, true);
      _claw.setSoftLimit(SoftLimitDirection.kForward, 19);
      kP_Claw         = 0.8;
      kI_Claw         = 0;
      kD_Claw         = 0;
      kIz_Claw        = 0;
      kFF_Claw        = 0;
      kMaxOutput_Claw = 0.01;
      kMinOutput_Claw = -0.01;
      pid_Claw = _claw.getPIDController();
      pid_Claw.setP(kP_Claw);
      pid_Claw.setI(kI_Claw);
      pid_Claw.setD(kD_Claw);
      pid_Claw.setIZone(kIz_Claw);
      pid_Claw.setFF(kFF_Claw);
      en_Claw = _claw.getEncoder();
   }

   public double clawGetPose() {
      return en_Claw.getPosition();
   }

   public void clawSetPose( double new_target) {
      pid_Claw.setReference(new_target, ControlType.kPosition);
   }
}