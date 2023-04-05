package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.playingwithfusion.TimeOfFlight;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxPIDController.ArbFFUnits;

//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ManipulatorControl
{
   private CANSparkMax _lift;
   private SparkMaxPIDController pid_Lift;
   private RelativeEncoder en_Lift;
   private double kP_Lift, kI_Lift, kD_Lift, kIz_Lift, kFF_Lift, kMaxOutput_Lift, kMinOutput_Lift;
   private double lift_target = 0.0;

   private TalonFX _extension;
   private double  ext_target = 0.0;

   private CANSparkMax _wrist;
   private double wristTop = -0.238095;
   private double wristLevel = -19.761831;
   private double wrist_fV  =  0.45;
   private double wrist2Radians = Math.PI/(( wristTop - wristLevel) * 2.0);
   private SparkMaxPIDController pid_Wrist;
   private RelativeEncoder en_Wrist;
   private double kP_Wrist, kI_Wrist, kD_Wrist, kIz_Wrist, kFF_Wrist, kMaxOutput_Wrist, kMinOutput_Wrist;
   private double wrist_target = 0.0;

   //private GrabberClaw _claw;
   private GrabberIntake _claw;

   private long _SysPntTimer;

   public static enum MANIPPOS {
      TOP,
      MID,
      FLOOR,
      TRAVEL,
      SUBSTATION,
      AUTONCUBE,
      MANUAL
   };

   public ManipulatorControl( TimeOfFlight sensor )
   {
      //private GrabberClaw _claw = new GrabberClaw();
      _claw = new GrabberIntake( sensor );
   }
   private MANIPPOS manipPos = MANIPPOS.MANUAL;

   public void init(){
      liftInit();
      extensionInit();
      wristInit();
      //clawInit();
      _SysPntTimer =  (System.currentTimeMillis() + 2000); //Used to print the Motors Status every 3 seconds
   }

   public void disable() 
   {
      //liftDisable();
      extensionDisable();
      //wristDisable();
      //_claw.disable();
   }

   public void setManipPos(MANIPPOS pos){
      manipPos = pos;
   }

   public void periodic()
   {
       if ( System.currentTimeMillis() > _SysPntTimer ){
          sysPrints();
       }
       _claw.periodic( wristGetPose() );
      switch(manipPos){
         case TOP:
            liftSetPose(170);      //FIXME
            if ( liftGetPose() > 120)   //FIXME
            {
               if( !UI._coneCube() ){
                  wristSetPose(-21.25); //Cone     
                  
               } else {
                  wristSetPose(-23.0); //Cube
                 
               }
            }else{
                  wristSetPose(-10);
            }
            extSetPose(295000.0);
            break;
                     
         case MID:
            
            liftSetPose(110);      
            extSetPose(0.0);
            if ( liftGetPose() > 75)   
            {
               if( !UI._coneCube() ){
                  wristSetPose(-21.25); //Cone     
                  
               } else {
                  wristSetPose(-23.0); //Cube
                 
               }
            }else{
                  wristSetPose(-10);
            }
            break;
                      
         case FLOOR:
            liftSetPose( 7);     
            extSetPose(0.0);
            if ( ( liftGetPose() < 38 ) && ( extGetPose() < 4000 ) )  
            {
               if( !UI._coneCube() ){
                  wristSetPose(-28.5);     //Cone
               } else {
                  wristSetPose(-28.5);     //Cube
               }
            }
            break;

         case TRAVEL:
            wristSetPose( 0 );
            if ( wristGetPose( ) > -25 )   
            {
               extSetPose( 0.0 );
               liftSetPose( 6 );
            }
            break;

         case SUBSTATION:
            liftSetPose(170);      
            extSetPose(0.0);
            if ( liftGetPose() > 116)   
            {
               if( !UI._coneCube() ){
                  wristSetPose(-26.75); //cone    
                  
               } else {
                  wristSetPose(-26.5); //cube
               }
            }else{
                  wristSetPose(-10);
            }
            break;

         case AUTONCUBE:
         liftSetPose(110);      
         extSetPose(30000.0);
         if ( liftGetPose() > 75)   
         {
               wristSetPose(-22.0);
         }else{
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
      _lift.setSmartCurrentLimit(35);
      _lift.setInverted(true);
      _lift.enableSoftLimit(SoftLimitDirection.kReverse, true);
      _lift.setSoftLimit(SoftLimitDirection.kReverse, 6);        //lower limit //FIXME
      _lift.enableSoftLimit(SoftLimitDirection.kForward, true);
      _lift.setSoftLimit(SoftLimitDirection.kForward, 170);      //upper limit //FIXME
      _lift.setIdleMode(IdleMode.kBrake);
      kP_Lift         =  0.8;
      kI_Lift         =  0;
      kD_Lift         =  0;
      kIz_Lift        =  0;
      kFF_Lift        =  0;
      kMaxOutput_Lift =  0.90;
      kMinOutput_Lift = -0.90;
      pid_Lift = _lift.getPIDController();
      //pid_Lift.setFeedbackDevice()
      pid_Lift.setP(kP_Lift);
      pid_Lift.setI(kI_Lift);
      pid_Lift.setD(kD_Lift);
      pid_Lift.setIZone(kIz_Lift);
      pid_Lift.setFF(kFF_Lift);
      pid_Lift.setOutputRange(kMinOutput_Lift, kMaxOutput_Lift);
      en_Lift = _lift.getEncoder();
      _lift.burnFlash();
   }

   public double liftGetPose() {
      return en_Lift.getPosition();
   }

   private boolean liftAtPosition() {
      double error = lift_target - en_Lift.getPosition();
      //System.out.println( "lift "+ lift_target + " "+ error );
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
      } else if ( new_target > 230.0 ) {
         new_target = 230.0;
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
      _wrist.setSoftLimit(SoftLimitDirection.kReverse, -29);        //lower limit  //FIXME
      _wrist.enableSoftLimit(SoftLimitDirection.kForward, true);
      _wrist.setSoftLimit(SoftLimitDirection.kForward, 0);      //upper limit
      _wrist.setIdleMode(IdleMode.kBrake);
      kP_Wrist         =  0.75;
      kI_Wrist         =  0;
      kD_Wrist         =  0;
      kIz_Wrist        =  0;
      kFF_Wrist        =  0;
      kMaxOutput_Wrist =  0.25;
      kMinOutput_Wrist = -0.25;
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

   private double getFeedForward( double new_target )
   {
      double target_rad = (new_target - wristLevel) * wrist2Radians;
      return wrist_fV * Math.cos( target_rad); 
   }

   public void wristSetPose( double new_target) {
      if (new_target < -30.0 ) {
         new_target = -30.0;
      } else if ( new_target > 0.0 ) {
         new_target = 0.0;
      }
      wrist_target = new_target;
      pid_Wrist.setReference(new_target, ControlType.kPosition, 0, getFeedForward(new_target), ArbFFUnits.kVoltage);
   }

   private boolean wristAtPosition() {
      double error = wrist_target - en_Wrist.getPosition();
      if ( ( error < 1.0 ) && ( error > -1.0 ) ) {
         return true;
      } else {
         return false;
      }
   }

   public void wristDisable() {
      _wrist.set(0.0);
   }
   public void wristWindUp()
   {
      _wrist.enableSoftLimit(SoftLimitDirection.kForward, true);
      _wrist.set( 0.10 );
      en_Wrist.setPosition( 0.0 );
   }

   public void clawGrabCone( ){
      _claw.grabCone();
   }
   public void clawGrabCube( ){
      _claw.grabCube();
   }
   public void clawRelease( ) {
      _claw.release();
   }
   public void clawDisable() {
      _claw.disable();
   }
   private void sysPrints(){
      System.out.println("Lift Motor Temp: " + _lift.getMotorTemperature());
      System.out.println("Lift Motor Pos.: " + en_Lift.getPosition());
      System.out.println("Lift Motor Cur.: " + _lift.getOutputCurrent());
      System.out.println("---");
      System.out.println("Wrst Motor Temp: " + _wrist.getMotorTemperature());
      System.out.println("Wrst Motor Pos.: " + en_Wrist.getPosition());
      System.out.println("Wrst Motor Cur.: " + _wrist.getOutputCurrent());
      System.out.println("---");
      _claw.sysPrints();
      _SysPntTimer =  (System.currentTimeMillis() + 2000);
   }
}