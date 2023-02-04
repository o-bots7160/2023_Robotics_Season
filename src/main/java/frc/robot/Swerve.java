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
import edu.wpi.first.math.controller.HolonomicDriveController;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Swerve {
    public boolean auton_active = false;
    public SwerveDriveOdometry swerveOdometry;
    private double rot_ctrl;
    private double rot_err;
    private double x_ctrl;
    private double x_err = 0;
    private double y_ctrl;
    private double y_err = 0;
    public SwerveModule[] mSwerveMods;
    public Pigeon2 gyro;
    
    private ProfiledPIDController rotPID = new ProfiledPIDController(3.5, 0.0, 0.0,  //FIXME for comp bot Kp = 0.25 Ki = 0 Kd = -0.1
      new TrapezoidProfile.Constraints(3, 5));                   
    private PIDController x_PID = new PIDController(3.7, 0, 0);                      //FIXME
    private PIDController y_PID = new PIDController(3.4, 0, 0);                      //FIXME

    public HolonomicDriveController controller = new HolonomicDriveController( x_PID, y_PID, rotPID );

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

        rotPID.setTolerance( Math.toRadians(1.0), 0.25);
        rotPID.enableContinuousInput(0.0, 2*Math.PI);
        x_PID.setTolerance( 0.01, 0.01);
        y_PID.setTolerance( 0.01, 0.01);
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

    public boolean drive(Trajectory.State _state ) {
        SwerveModuleState[] swerveModuleStates = Constants.Swerve.swerveKinematics.toSwerveModuleStates(
        controller.calculate(getPose(), _state, new Rotation2d(0.0)));
        SmartDashboard.putNumber("getHeading", _state.poseMeters.getRotation().getDegrees());
        SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, Constants.Swerve.maxSpeed);

        for(SwerveModule mod : mSwerveMods){
            mod.setDesiredState(swerveModuleStates[mod.moduleNumber], true);
        }
        return false;
    }

    //Testing rotPID for HolonomicDriveController
    public boolean rotate( double angle )
    {
        if ( ! auton_active )
        {
            auton_active = true;
            rotPID.reset(getYaw().getRadians());
        }
        rot_err  = getYaw().getRadians() - Math.toRadians(angle);
        rot_ctrl = rotPID.calculate( Math.toRadians(angle));
        drive( new Translation2d(), rot_ctrl, true, true);
        auton_active = ! rotPID.atGoal(); 
        return auton_active;
    }

    //Testing x_PID for HolonomicDriveController
    public boolean move_x( double distance )
    {
        System.out.println( "move_x");
        Pose2d pose = swerveOdometry.update(getYaw(), getModulePositions());
        if ( ! auton_active )
        {
            auton_active = true;
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
        System.out.println( "move_y");
        Pose2d pose = swerveOdometry.update(getYaw(), getModulePositions());
        if ( ! auton_active )
        {
            auton_active = true;
            y_PID.reset( );
            y_PID.setSetpoint(pose.getY() + distance);
        }
        y_err  = pose.getY() - distance;
        y_ctrl = y_PID.calculate( pose.getY() );
        drive( new Translation2d( 0, y_ctrl), 0, true, true);
        auton_active = ! y_PID.atSetpoint(); 
        return auton_active;
    }
    public boolean move_Pose2d( Pose2d new_pose)
    {
        Pose2d pose = swerveOdometry.update(getYaw(), getModulePositions());
        if ( ! auton_active )
        {
            x_PID.reset( );
            x_PID.setSetpoint( new_pose.getX());
            y_PID.reset( );
            y_PID.setSetpoint( new_pose.getY());
            rotPID.reset(getYaw().getRadians());
        }
        rot_err  = getYaw().getRadians() - new_pose.getRotation().getRadians();
        rot_ctrl = rotPID.calculate( new_pose.getRotation().getRadians());
        x_err    = pose.getX() - new_pose.getX();
        x_ctrl   = x_PID.calculate( pose.getX() );
        y_err    = pose.getY() - new_pose.getY();
        y_ctrl   = y_PID.calculate( pose.getY() );
        drive( new Translation2d(x_ctrl, y_ctrl), rot_ctrl, true, true);
        auton_active = x_PID.atSetpoint() | y_PID.atSetpoint() | rotPID.atGoal();
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
        SmartDashboard.putNumber("x_err",    x_err);  
        SmartDashboard.putNumber("y_err",    y_err);  
        SmartDashboard.putNumber("X   ",     pose.getX());
        SmartDashboard.putNumber("Y   ",     pose.getY());
        SmartDashboard.putNumber("ROT ",     pose.getRotation().getDegrees());    

        for(SwerveModule mod : mSwerveMods){
            SmartDashboard.putNumber("Mod " + mod.moduleNumber + " Cancoder", mod.getCanCoder().getDegrees());
            SmartDashboard.putNumber("Mod " + mod.moduleNumber + " Integrated", mod.getPosition().angle.getDegrees());
            SmartDashboard.putNumber("Mod " + mod.moduleNumber + " Velocity", mod.getState().speedMetersPerSecond);    
        }
    }
}