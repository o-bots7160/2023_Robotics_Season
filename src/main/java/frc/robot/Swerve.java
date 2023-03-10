package frc.robot;


import com.ctre.phoenix.sensors.Pigeon2;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Swerve {
    public boolean auton_active = false;
    private final SwerveDrivePoseEstimator poseEstimator;
    private double xy_kP  =  -5.5; // 5.5
    private double xy_kI  =  -0.15; // .015
    private double xy_kD  =  0.0;
    private double rot_kP =  5.1;
    private double rot_kI =  0.0;       
    private double rot_kD =  0.0;
    private double angle_target;
    private double rot_ctrl;
    //private double rot_err;
    private double rot_ctrlMax = 1.5;//3
    private double rot_ctrlMin = -1.5;//-3
    private double x_ctrl;
    //private double x_err = 0;
    private double x_ctrlMax = 1.0;//1.5
    private double x_ctrlMin = -1.0;//-1.5
    private double y_ctrl;
    //private double y_err = 0;
    private double y_ctrlMax = 1.0;//1.5
    private double y_ctrlMin = -1.0;//-1.5
    public SwerveModule[] mSwerveMods;
    public Pigeon2 gyro;
    private SwerveDriveKinematics currentKinematics = Constants.Swerve.swerveKinematics;
    private SwerveDriveKinematics rotationFront = new SwerveDriveKinematics(
        new Translation2d( -Units.inchesToMeters(32.5), Constants.Swerve.trackWidth / 2.0),
        new Translation2d( -Units.inchesToMeters(32), -Constants.Swerve.trackWidth / 2.0),
        new Translation2d(-Constants.Swerve.wheelBase - Units.inchesToMeters(32), Constants.Swerve.trackWidth / 2.0),
        new Translation2d(-Constants.Swerve.wheelBase - Units.inchesToMeters(32), -Constants.Swerve.trackWidth / 2.0));
    
    private PIDController rot_PID = new PIDController( rot_kP, rot_kI, rot_kD );      //FIXME
    private PIDController x_PID   = new PIDController( xy_kP,   xy_kI, xy_kD  );      //FIXME
    private PIDController y_PID   = new PIDController( xy_kP,   xy_kI, xy_kD  );      //FIXME

    public Swerve() {
        gyro = new Pigeon2(Constants.Swerve.pigeonID);
        gyro.configFactoryDefault();
        zeroGyro();

        mSwerveMods = new SwerveModule[] {
            new SwerveModule(0, Constants.Swerve.Mod0.constants),
            new SwerveModule(1, Constants.Swerve.Mod1.constants),
            new SwerveModule(2, Constants.Swerve.Mod2.constants),
            new SwerveModule(3, Constants.Swerve.Mod3.constants)
        };

        rot_PID.setTolerance( Math.toRadians(1.0) );
        rot_PID.enableContinuousInput(-Math.PI, Math.PI);
        rot_PID.setIntegratorRange(-0.08, 0.08);
        x_PID.setTolerance( 0.03, 0.03);
        x_PID.setIntegratorRange(-0.04, 0.04);
        y_PID.setTolerance( 0.03, 0.03);
        y_PID.setIntegratorRange(-0.04, 0.04);
        /* By pausing init for a second before setting module offsets, we avoid a bug with inverting motors.
         * See https://github.com/Team364/BaseFalconSwerve/issues/8 for more info.
         */
        Timer.delay(1.0);
        resetModulesToAbsolute();

        poseEstimator = new SwerveDrivePoseEstimator(Constants.Swerve.swerveKinematics, getYaw(), getModulePositions(), new Pose2d());
        // SmartDashboard.putNumber("rot_kP", rot_kP);  
        // SmartDashboard.putNumber("rot_kI", rot_kI);  
        // SmartDashboard.putNumber("rot_kD", rot_kD);  
    }

    public void drive(Translation2d translation, double rotation, boolean fieldRelative, boolean isOpenLoop) {
        SwerveModuleState[] swerveModuleStates =
            currentKinematics.toSwerveModuleStates(
                fieldRelative ? ChassisSpeeds.fromFieldRelativeSpeeds(
                                    translation.getX(), 
                                    translation.getY(), 
                                    rotation, 
                                    getYaw()
                                )
                                : new ChassisSpeeds(
                                    translation.getX(), 
                                    translation.getY(), 
                                    rotation)
                                );
        SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, Constants.Swerve.maxSpeed);

        for(SwerveModule mod : mSwerveMods){
            mod.setDesiredState(swerveModuleStates[mod.moduleNumber], isOpenLoop);
        }
    }    

    public void setRotCenter(boolean rotCenter)
    {
        if(!rotCenter)
        {
            currentKinematics = Constants.Swerve.swerveKinematics;
        }else
        {
            currentKinematics = rotationFront;
        }
    }

    //Testing rotPID for HolonomicDriveController
    public boolean rotate( Rotation2d angle )
    {
        Rotation2d current_yaw = getYaw();
        if ( ! auton_active )
        {
            angle_target = current_yaw.plus( angle ).getRadians();
            rot_PID.reset( );
            rot_PID.setSetpoint( angle_target );
        }
        //rot_err  = current_yaw.minus( new Rotation2d( angle_target)).getRadians();
        rot_ctrl = rot_PID.calculate(current_yaw.getRadians());
        drive( new Translation2d(), rot_ctrl, true, true);
        auton_active = ! rot_PID.atSetpoint();
        if ( ! auton_active )
        {
            drive( new Translation2d(), 0.0, true, true );
        } 
        return auton_active;
    }

    //Testing x_PID for HolonomicDriveController
    public boolean move_x( double distance )
    {
        Pose2d pose = getPose();
        if ( ! auton_active )
        {
            x_PID.reset( );
            x_PID.setSetpoint( pose.getX() + distance);
        }
        //x_err  = pose.getX() - distance;
        x_ctrl = x_PID.calculate( pose.getX() );
        drive( new Translation2d( x_ctrl, 0), 0, true, true);
        auton_active = ! x_PID.atSetpoint(); 
        return auton_active;
    }

    //Testing y_PID for HolonomicDriveController
    public boolean move_y( double distance )
    {
        
        Pose2d pose = getPose();
        if ( ! auton_active )
        {
            y_PID.reset( );
            y_PID.setSetpoint(pose.getY() + distance);
        }
        //y_err  = pose.getY() - distance;
        y_ctrl = y_PID.calculate( pose.getY() );
        drive( new Translation2d( 0, y_ctrl), 0, true, true);
        auton_active = ! y_PID.atSetpoint(); 
        return auton_active;
    }
    public boolean move_Pose2d( Pose2d new_pose)
    {
        Pose2d pose = getPose();
        if ( ! auton_active )
        {
            x_PID.reset( );
            x_PID.setSetpoint( new_pose.getX());
            y_PID.reset( );
            y_PID.setSetpoint( new_pose.getY());
            rot_PID.reset( );
            rot_PID.setSetpoint(new_pose.getRotation().getRadians());
        }
        //rot_err  = getYaw().getRadians() - new_pose.getRotation().getRadians();
        rot_ctrl = rot_PID.calculate( pose.getRotation().getRadians());
        if       (rot_ctrl > rot_ctrlMax) {
            rot_ctrl = rot_ctrlMax;
        }else if (rot_ctrl < rot_ctrlMin) {
            rot_ctrl = rot_ctrlMin;
        }
        //x_err    = pose.getX() - new_pose.getX();
        x_ctrl   = x_PID.calculate( pose.getX() );
        if       (x_ctrl > x_ctrlMax) {
            x_ctrl = x_ctrlMax;
        }else if (x_ctrl < x_ctrlMin) {
            x_ctrl = x_ctrlMin;
        }
        //y_err    = pose.getY() - new_pose.getY();
        y_ctrl   = y_PID.calculate( pose.getY() );
        if       (y_ctrl > y_ctrlMax) {
            y_ctrl = y_ctrlMax;
        }else if (y_ctrl < y_ctrlMin) {
            y_ctrl = y_ctrlMin;
        }
        drive( new Translation2d(-x_ctrl, -y_ctrl), rot_ctrl, true, true); //fieldRelative: MUST = true
        auton_active = !x_PID.atSetpoint() || !y_PID.atSetpoint() || !rot_PID.atSetpoint();
        return auton_active;
    }
    
    /* Used by SwerveControllerCommand in Auto */
    public void setModuleStates(SwerveModuleState[] desiredStates) {
        SwerveDriveKinematics.desaturateWheelSpeeds(desiredStates, Constants.Swerve.maxSpeed);
        
        for(SwerveModule mod : mSwerveMods){
            mod.setDesiredState(desiredStates[mod.moduleNumber], false);
        }
    }    

    public Pose2d getPose() {
        return poseEstimator.getEstimatedPosition();
    }

    public void resetOdometry(Pose2d pose) {
        //
        //   Resetting gyro is not needed for poseEstimator... however it is needed for
        //   ChassisSpeeds.
        //
        //
        gyro.setYaw( pose.getRotation().getDegrees() );
        Timer.delay(1.0);
        poseEstimator.resetPosition(getYaw(), getModulePositions(), pose);
    }

    public SwerveModuleState[] getModuleStates(){
        SwerveModuleState[] states = new SwerveModuleState[4];
        for(SwerveModule mod : mSwerveMods){
            states[mod.moduleNumber] = mod.getState();
        }
        return states;
    }

    public SwerveModulePosition[] getModulePositions(){
        SwerveModulePosition[] positions = new SwerveModulePosition[4];
        for(SwerveModule mod : mSwerveMods){
            positions[mod.moduleNumber] = mod.getPosition();
        }
        return positions;
    }

    public void zeroGyro(){
        gyro.setYaw(0);
    }

    public Rotation2d getYaw() {
        return (Constants.Swerve.invertGyro) ? Rotation2d.fromDegrees(360 - gyro.getYaw()) : Rotation2d.fromDegrees(gyro.getYaw());
    }

    public void resetModulesToAbsolute(){
        for(SwerveModule mod : mSwerveMods){
            mod.resetToAbsolute();
        }
    }

    private double prevPitch = 0;

    public boolean chargeStationAutoLevel(){

        final double pitch = gyro.getRoll();
        final double pitch_P = 0.00005;
        final double current_yaw = gyro.getYaw();
        double delta = Math.abs(Math.abs(pitch) - prevPitch);
        final double maxDelta = 3;
        double driveCommand = pitch*pitch_P;
        if(pitch > 4 && delta < maxDelta){
            if(current_yaw > 135 && current_yaw < 225){
                drive( new Translation2d( driveCommand, 0).times(Constants.Swerve.maxSpeed), 0, true, true );
            }else{
                drive( new Translation2d( -driveCommand, 0).times(Constants.Swerve.maxSpeed), 0, true, true );
            }
            
        }else if(pitch < -4 && delta < maxDelta){
            if(current_yaw > 135 && current_yaw < 225){
                drive( new Translation2d( -driveCommand, 0).times(Constants.Swerve.maxSpeed), 0, true, true );
            }else{
                drive( new Translation2d( driveCommand, 0).times(Constants.Swerve.maxSpeed), 0, true, true );
            }
            
        }else{
            return true;
        }
        
        prevPitch = Math.abs(pitch);
        return false;
    }

    public void periodic(){
        Pose2d pose = poseEstimator.update(getYaw(), getModulePositions());
        /*if (photonPoseEstimator != null) {
            // Update pose estimator with the best visible target
            photonPoseEstimator.update().ifPresent(estimatedRobotPose -> {
              var estimatedPose = estimatedRobotPose.estimatedPose;
              // Make sure we have a new measurement, and that it's on the field
              if (estimatedRobotPose.timestampSeconds != previousPipelineTimestamp
                  && estimatedPose.getX() > 0.0 && estimatedPose.getX() <= FIELD_LENGTH_METERS
                  && estimatedPose.getY() > 0.0 && estimatedPose.getY() <= FIELD_WIDTH_METERS) {
                previousPipelineTimestamp = estimatedRobotPose.timestampSeconds;
                poseEstimator.addVisionMeasurement(estimatedPose.toPose2d(), estimatedRobotPose.timestampSeconds);
              }
            });
          }
      
          Pose2d dashboardPose = getCurrentPose();
          if (originPosition == OriginPosition.kRedAllianceWallRightSide) {
            // Flip the pose when red, since the dashboard field photo cannot be rotated
            dashboardPose = flipAlliance(dashboardPose);
          }
          field2d.setRobotPose(dashboardPose);
        // rot_kP = SmartDashboard.getNumber("rot_kP", rot_kP);
        // rot_kI = SmartDashboard.getNumber("rot_kI", rot_kI);
        // rot_kD = SmartDashboard.getNumber("rot_kD", rot_kD);
        // rot_PID.setPID( rot_kP, rot_kI, rot_kD );
        // SmartDashboard.putNumber("rot_ctrl", rot_ctrl);  
        // SmartDashboard.putNumber("rot_err",  rot_err);  
        // SmartDashboard.putNumber("x_ctrl", x_ctrl);
        // SmartDashboard.putNumber("x_err",    x_err); 
        // SmartDashboard.putNumber("y_ctrl", y_ctrl);  
        // SmartDashboard.putNumber("y_err",    y_err);  
        //SmartDashboard.putNumber("X   ",     Units.metersToFeet(pose.getX()));
        //SmartDashboard.putNumber("Y   ",     Units.metersToFeet(pose.getY()));
        //SmartDashboard.putNumber("ROT ",     pose.getRotation().getDegrees());    
        // SmartDashboard.putBoolean("autonActive", auton_active);

        // for(SwerveModule mod : mSwerveMods){
        //     SmartDashboard.putNumber("Mod " + mod.moduleNumber + " Cancoder", mod.getCanCoder().getDegrees());
        //     SmartDashboard.putNumber("Mod " + mod.moduleNumber + " Integrated", mod.getPosition().angle.getDegrees());
        //     SmartDashboard.putNumber("Mod " + mod.moduleNumber + " Velocity", mod.getState().speedMetersPerSecond);    
        // }*/
    }
    public void disable()
    {
        drive( new Translation2d(), 0.0, true, true );
        auton_active = false; 
    }
    public void lock()
    {
        SwerveModuleState[] swerveModuleStates = { new SwerveModuleState( 0.1, new Rotation2d( Math.PI/4) ),
                                                   new SwerveModuleState( 0.1, new Rotation2d(-Math.PI/4) ),
                                                   new SwerveModuleState( 0.1, new Rotation2d(-Math.PI/4) ),
                                                   new SwerveModuleState( 0.1, new Rotation2d( Math.PI/4) )
        };

        disable();

        for(SwerveModule mod : mSwerveMods){
            mod.setDesiredState(swerveModuleStates[mod.moduleNumber], true);
        }
    }
}