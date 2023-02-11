package frc.robot;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;

import com.ctre.phoenix.sensors.Pigeon2;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.math.controller.HolonomicDriveController;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Swerve {
    public boolean auton_active = false;
    public SwerveDriveOdometry swerveOdometry;
    private double angle_target;
    private double rot_ctrl;
    private double rot_err;
    private double rot_ctrlMax = 3;
    private double rot_ctrlMin = -3;
    private double x_ctrl;
    private double x_err = 0;
    private double x_ctrlMax = 2.5;
    private double x_ctrlMin = -2.5;
    private double y_ctrl;
    private double y_err = 0;
    private double y_ctrlMax = 2.5;
    private double y_ctrlMin = -2.5;
    public SwerveModule[] mSwerveMods;
    public Pigeon2 gyro;
    
    private ProfiledPIDController rotProPID = new ProfiledPIDController(-13.775, 6, 0.0,  //FIXME for comp bot Kp = -13.775 Ki = 6 Kd = -137.5
      new TrapezoidProfile.Constraints(5, 12.5));                   
    private PIDController rotPID = new PIDController(-4.8, 0, 0);                      //FIXME
    private PIDController x_PID  = new PIDController(5.5, 0, 0);                      //FIXME
    private PIDController y_PID  = new PIDController(5.5, 0, 0);                      //FIXME

    public HolonomicDriveController controller = new HolonomicDriveController( x_PID, y_PID, rotProPID );

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

        rotProPID.setTolerance( Math.toRadians(1.0), 0.25);
        rotProPID.enableContinuousInput(-Math.PI, Math.PI);
        rotProPID.setIntegratorRange(-0.04, 0.04);
        rotPID.setTolerance( Math.toRadians(4.0) );
        rotPID.enableContinuousInput(-Math.PI, Math.PI);
        rotPID.setIntegratorRange(-0.04, 0.04);
        x_PID.setTolerance( 0.02, 0.01);
        y_PID.setTolerance( 0.02, 0.01);
        /* By pausing init for a second before setting module offsets, we avoid a bug with inverting motors.
         * See https://github.com/Team364/BaseFalconSwerve/issues/8 for more info.
         */
        Timer.delay(1.0);
        resetModulesToAbsolute();

        swerveOdometry = new SwerveDriveOdometry(Constants.Swerve.swerveKinematics, getYaw(), getModulePositions());
    }

    public void drive(Translation2d translation, double rotation, boolean fieldRelative, boolean isOpenLoop) {
        SwerveModuleState[] swerveModuleStates =
            Constants.Swerve.swerveKinematics.toSwerveModuleStates(
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

    public boolean drive(Trajectory.State _state, Rotation2d _final ) {
        SwerveModuleState[] swerveModuleStates = Constants.Swerve.swerveKinematics.toSwerveModuleStates(
        controller.calculate(getPose(), _state, _final));
        SmartDashboard.putNumber("getHeading", _state.poseMeters.getRotation().getDegrees());
        SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, Constants.Swerve.maxSpeed);

        for(SwerveModule mod : mSwerveMods){
            mod.setDesiredState(swerveModuleStates[mod.moduleNumber], true);
        }
        return false;
    }

    //Testing rotPID for HolonomicDriveController
    public boolean rotate( Rotation2d angle )
    {
        Rotation2d current_yaw = getYaw();
        if ( ! auton_active )
        {
            angle_target = current_yaw.plus( angle ).getRadians();
            rotPID.reset( );
            rotPID.setSetpoint( angle_target );
        }
        rot_err  = current_yaw.minus( new Rotation2d( angle_target)).getRadians();
        rot_ctrl = rotPID.calculate(current_yaw.getRadians());
        drive( new Translation2d(), rot_ctrl, true, true);
        auton_active = ! rotPID.atSetpoint();
        if ( ! auton_active )
        {
            drive( new Translation2d(), 0.0, true, true );
        } 
        return auton_active;
    }

    //Testing x_PID for HolonomicDriveController
    public boolean move_x( double distance )
    {
        Pose2d pose = getPose(); //swerveOdometry.update(getYaw(), getModulePositions());
        if ( ! auton_active )
        {
            x_PID.reset( );
            x_PID.setSetpoint( pose.getX() + distance);
        }
        x_err  = pose.getX() - distance;
        x_ctrl = x_PID.calculate( pose.getX() );
        drive( new Translation2d( x_ctrl, 0), 0, true, true);
        auton_active = ! x_PID.atSetpoint(); 
        return auton_active;
    }

    //Testing y_PID for HolonomicDriveController
    public boolean move_y( double distance )
    {
        Pose2d pose = getPose(); //swerveOdometry.update(getYaw(), getModulePositions());
        if ( ! auton_active )
        {
            y_PID.reset( );
            y_PID.setSetpoint(pose.getY() + distance);
        }
        y_err  = pose.getY() - distance;
        y_ctrl = y_PID.calculate( pose.getY() );
        drive( new Translation2d( 0, y_ctrl), 0, true, true);
        auton_active = ! y_PID.atSetpoint(); 
        return auton_active;
    }
    // public void newPose2d( Pose2d new_pose )
    // {
    //     x_PID.reset( );
    //     x_PID.setSetpoint( new_pose.getX());
    //     y_PID.reset( );
    //     y_PID.setSetpoint( new_pose.getY());
    //     rotPID.reset( );
    //     rotPID.setSetpoint(new_pose.getRotation().getRadians());
    // }
    public boolean move_Pose2d( Pose2d new_pose)
    {
        Pose2d pose = getPose(); //swerveOdometry.update(getYaw(), getModulePositions());
        if ( ! auton_active )
        {
            x_PID.reset( );
            x_PID.setSetpoint( new_pose.getX());
            y_PID.reset( );
            y_PID.setSetpoint( new_pose.getY());
            rotPID.reset( );
            rotPID.setSetpoint(new_pose.getRotation().getRadians());
        }
        rot_err  = getYaw().getRadians() - new_pose.getRotation().getRadians();
        rot_ctrl = rotPID.calculate( pose.getRotation().getRadians());
        if       (rot_ctrl > rot_ctrlMax) {
            rot_ctrl = rot_ctrlMax;
        }else if (rot_ctrl < rot_ctrlMin) {
            rot_ctrl = rot_ctrlMin;
        }
        x_err    = pose.getX() - new_pose.getX();
        x_ctrl   = x_PID.calculate( pose.getX() );
        if       (x_ctrl > x_ctrlMax) {
            x_ctrl = x_ctrlMax;
        }else if (x_ctrl < x_ctrlMin) {
            x_ctrl = x_ctrlMin;
        }
        y_err    = pose.getY() - new_pose.getY();
        y_ctrl   = y_PID.calculate( pose.getY() );
        if       (y_ctrl > y_ctrlMax) {
            y_ctrl = y_ctrlMax;
        }else if (y_ctrl < y_ctrlMin) {
            y_ctrl = y_ctrlMin;
        }
        drive( new Translation2d(x_ctrl, y_ctrl), rot_ctrl, true, true); //fieldRelative: MUST = true
        auton_active = !x_PID.atSetpoint() || !y_PID.atSetpoint() || !rotPID.atSetpoint();
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
        return swerveOdometry.getPoseMeters();
    }

    public void resetOdometry(Pose2d pose) {
        swerveOdometry.resetPosition(getYaw(), getModulePositions(), pose);
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

    public void periodic(){
        Pose2d pose = swerveOdometry.update(getYaw(), getModulePositions());
        SmartDashboard.putNumber("rot_ctrl", rot_ctrl);  
        SmartDashboard.putNumber("rot_err",  rot_err);  
        SmartDashboard.putNumber("x_ctrl", x_ctrl);
        SmartDashboard.putNumber("x_err",    x_err); 
        SmartDashboard.putNumber("y_ctrl", y_ctrl);  
        SmartDashboard.putNumber("y_err",    y_err);  
        SmartDashboard.putNumber("X   ",     Units.metersToFeet(pose.getX()));
        SmartDashboard.putNumber("Y   ",     Units.metersToFeet(pose.getY()));
        SmartDashboard.putNumber("ROT ",     pose.getRotation().getDegrees());    
        SmartDashboard.putBoolean("autonActive", auton_active);

        for(SwerveModule mod : mSwerveMods){
            SmartDashboard.putNumber("Mod " + mod.moduleNumber + " Cancoder", mod.getCanCoder().getDegrees());
            SmartDashboard.putNumber("Mod " + mod.moduleNumber + " Integrated", mod.getPosition().angle.getDegrees());
            SmartDashboard.putNumber("Mod " + mod.moduleNumber + " Velocity", mod.getState().speedMetersPerSecond);    
        }
    }
    public void disable()
    {
        drive( new Translation2d(), 0.0, true, true );
       auton_active = false; 
    }
}