package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.util.Units;

public class RobotContainer {
   //
   //  What are these lines for?
   //  They make it so that ANY instance of RobotContainer points to a single
   //  instance of RobotContainer, defined below
   //
   //
   private static RobotContainer RobotContainer_instance = null;

   private RobotContainer()
   {
   }

   public static RobotContainer getInstance()
   {
      if (RobotContainer_instance == null)
      {
         RobotContainer_instance = new RobotContainer();
      }
 
      return RobotContainer_instance;
   }
   //
   //  The devices this robot uses
   //
   //
   private final Swerve _drive = new Swerve();
   private final ManipulatorControl _manipulator = new ManipulatorControl();
   //
   //  The methods used to control the robot... whether teleop or auton
   //
   //
   public void Init()
   {
      _drive.resetOdometry( new Pose2d( Units.feetToMeters( 0 ), Units.feetToMeters(0), new Rotation2d( 0.0)  ) );
      _manipulator.init();
   }
  
   public void drive(Translation2d translation, double rotation, boolean fieldRelative, boolean isOpenLoop) 
   {
      _drive.drive( translation, rotation, fieldRelative, isOpenLoop );
   }    

   public boolean drive( Trajectory.State _state )
   {
      return _drive.drive( _state );
   }
   public void periodic()
   {
      _drive.periodic();
      _manipulator.periodic();
   }

   public void resetOdometry(Pose2d pose)
   {
      _drive.resetOdometry( pose );
   }
}