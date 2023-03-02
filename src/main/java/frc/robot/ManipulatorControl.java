package frc.robot;

import javax.lang.model.util.ElementScanner14;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
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
   private boolean haveCone = false;

   private CANSparkMax _lift;
   private SparkMaxPIDController pid_Lift;
   private RelativeEncoder en_Lift;
   private double kP_Lift, kI_Lift, kD_Lift, kIz_Lift, kFF_Lift, kMaxOutput_Lift, kMinOutput_Lift;
   private double lift_target = 0.0;

   private TalonFX _extension;
   private double  ext_target = 0.0;

   private CANSparkMax _wrist;
   private SparkMaxPIDController pid_Wrist;
   private RelativeEncoder en_Wrist;
   private double kP_Wrist, kI_Wrist, kD_Wrist, kIz_Wrist, kFF_Wrist, kMaxOutput_Wrist, kMinOutput_Wrist;
   private double wrist_target = 0.0;

   private CANSparkMax _claw;
   private SparkMaxPIDController pid_Claw;
   private RelativeEncoder en_Claw;
   private double kP_Claw, kI_Claw, kD_Claw, kIz_Claw, kFF_Claw, kMaxOutput_Claw, kMinOutput_Claw;

   public static enum MANIPPOS {
      TOP,
      MID,
      FLOOR,
      TRAVEL,
      SUBSTATION,
      MANUAL
   };

   private MANIPPOS manipPos = MANIPPOS.MANUAL;

   public void init(){
      liftInit();
      extensionInit();
      wristInit();
      clawInit();
   }

   public void disable() 
   {
      //liftDisable();
      extensionDisable();
      //wristDisable();
      //clawDisable();
   }

   public void setManipPos(MANIPPOS pos){
      manipPos = pos;
   }

   public void periodic()
   {
      switch(manipPos){
         case TOP:
            if( haveCone ){
               liftSetPose(235);
            }else{
               liftSetPose(235);
            }
            if ( liftGetPose() > 150)
            {
               wristSetPose(-23);
            } else {
               wristSetPose(-10);
            }
            extSetPose(295000.0);
            break;
         case MID:
            extSetPose(0.0);
            if(haveCone){// if true cube is selected
               liftSetPose(170);
            }else{
               liftSetPose(155);
            }
            if ( liftGetPose() > 100 )
            {
               wristSetPose(-23);
            } else {
               wristSetPose(-10);
            }
            break;
         case FLOOR:
            liftSetPose( 0);
            extSetPose(0.0);
            if ( ( liftGetPose() < 50 ) && ( extGetPose() < 3000 ) )
            {
               wristSetPose(-26);
            }
            break;
         case TRAVEL:
            wristSetPose( 0 );
            if ( wristGetPose( ) < -20 )
            {
               extSetPose( 0.0 );
               liftSetPose( 0 );
            }
            break;
         case SUBSTATION:
            liftSetPose(235);
            extSetPose(0.0);
            if ( liftGetPose() > 150)
            {
               wristSetPose(-25);
            } else {
               wristSetPose(-10);
            }
            break;
         case MANUAL:
            break;
      }
      
   }
   public boolean atPosition() {
      return ( liftAtPosition() && extAtPosition() && wristAtPosition() );
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

   private boolean liftAtPosition() {
      double error = lift_target - en_Lift.getPosition();
      if ( ( error < 3.0 ) && ( error > -3.0 ) ) {
         return true;
      } else {
         //System.out.println( "lift "+ lift_target + " "+ error );
         return false;
      }
   }

   public void liftSetPose( double new_target) {
      if (new_target < 0.0 ) {
         new_target = 0.0;
      } else if ( new_target > 235.0 ) {
         new_target = 235.0;
      }
      lift_target = new_target;
      pid_Lift.setReference(new_target, ControlType.kPosition);
   }

   public void liftDisable() {
      _lift.set(0.0);
   }

   private void extensionInit(){
      _extension = new TalonFX(51);
      _extension.configFactoryDefault();
    _extension.configReverseSoftLimitThreshold( 3000.0);      //lower limit
    
    _extension.configForwardSoftLimitThreshold( 290000.0); //upper limit  300000.0
    _extension.configForwardSoftLimitEnable(true, 0);
    _extension.configReverseSoftLimitEnable(true, 0);
    _extension.config_kP( 0, 0.06, 30);
    //_extension.config_kI( 0, 0.0, 0);
    //_extension.config_kD( 0, 0.0, 0);
    //_extension.config_IntegralZone( 0, 0.0, 0);
    _extension.setNeutralMode( NeutralMode.Brake);

   }

   private void extensionDisable() {
      _extension.set(ControlMode.PercentOutput, 0.0);
   }

   public double extGetPose() {
      return _extension.getSelectedSensorPosition();
   }

   public void extSetPose( double new_target) {
      if (new_target < 3000.0 ) {
         new_target = 3000.0;
      } else if ( new_target > 290000.0 ) {
         new_target = 290000.0;
      }
      ext_target = new_target;
      _extension.set(ControlMode.Position, new_target);
   }

   private boolean extAtPosition() {
      double error = ext_target - _extension.getSelectedSensorPosition();
      if ( ( error < 4000.0 ) && ( error > -4000.0 ) ) {
         return true;
      } else {
         //System.out.println( "ext "+ ext_target + " "+ error );
         return false;
      }
   }

   private void wristInit(){
      _wrist = new CANSparkMax(52, MotorType.kBrushless);
      _wrist.setInverted(false);
      _wrist.enableSoftLimit(SoftLimitDirection.kReverse, true);
      _wrist.setSoftLimit(SoftLimitDirection.kReverse, -28);        //lower limit
      _wrist.enableSoftLimit(SoftLimitDirection.kForward, true);
      _wrist.setSoftLimit(SoftLimitDirection.kForward, 0);      //upper limit
      _wrist.setIdleMode(IdleMode.kBrake);
      kP_Wrist         =  0.1;
      kI_Wrist         =  0;
      kD_Wrist         =  0;
      kIz_Wrist        =  0;
      kFF_Wrist        =  0;
      kMaxOutput_Wrist =  0.15;
      kMinOutput_Wrist = -0.15;
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
      if (new_target < -30.0 ) {
         new_target = -30.0;
      } else if ( new_target > 0.0 ) {
         new_target = 0.0;
      }
      wrist_target = new_target;
      pid_Wrist.setReference(new_target, ControlType.kPosition);
   }

   private boolean wristAtPosition() {
      double error = wrist_target - en_Wrist.getPosition();
      if ( ( error < 1.0 ) && ( error > -1.0 ) ) {
         return true;
      } else {
         //System.out.println( "wrst "+ wrist_target + " "+ error );
         return false;
      }
   }

   public void wristDisable() {
      _wrist.set(0.0);
   }

   private void clawInit(){
      _claw = new CANSparkMax(53, MotorType.kBrushless);
      _claw.setInverted(false);
      _claw.enableSoftLimit(SoftLimitDirection.kReverse, true);
      _claw.setSoftLimit(SoftLimitDirection.kReverse, 1);
      _claw.enableSoftLimit(SoftLimitDirection.kForward, true);
      _claw.setSoftLimit(SoftLimitDirection.kForward, 18);
      _claw.setIdleMode(IdleMode.kBrake);
      kP_Claw         = 0.8;
      kI_Claw         = 0;
      kD_Claw         = 0;
      kIz_Claw        = 0;
      kFF_Claw        = 0;
      kMaxOutput_Claw = 0.55;
      kMinOutput_Claw = -0.55;
      pid_Claw = _claw.getPIDController();
      pid_Claw.setP(kP_Claw);
      pid_Claw.setI(kI_Claw);
      pid_Claw.setD(kD_Claw);
      pid_Claw.setIZone(kIz_Claw);
      pid_Claw.setFF(kFF_Claw);
      pid_Claw.setOutputRange(kMinOutput_Claw, kMaxOutput_Claw);
      en_Claw = _claw.getEncoder();
   }

   public double clawGetPose() {
      return en_Claw.getPosition();
   }

   private void clawSetPose( double new_target) {
      pid_Claw.setReference(new_target, ControlType.kPosition);
   }
   public void clawGrabCone( ){
      haveCone = true;
      clawSetPose(18.5);
   }
   public void clawGrabCube( ){
      haveCone = false;
      clawSetPose(12.0);
   }
   public void clawRelease( ) {
      clawSetPose(2.0);
   }
   public void clawDisable() {
      _claw.set(0.0);
   }

}