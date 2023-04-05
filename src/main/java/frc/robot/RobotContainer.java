package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
//import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.util.Units;
import com.playingwithfusion.TimeOfFlight;
import com.playingwithfusion.TimeOfFlight.RangingMode;

public class RobotContainer {
   //
   //  What are these lines for?
   //  They make it so that ANY instance of RobotContainer points to a single
   //  instance of RobotContainer, defined below
   //
   //
   private static RobotContainer RobotContainer_instance = null;
   //
   //  The devices this robot uses
   //
   //
   private TimeOfFlight sensor = new TimeOfFlight(101);
   public final Swerve _drive = new Swerve();
   public final ManipulatorControl _manipulator;

   private RobotContainer()
   {
      sensor.setRangingMode(RangingMode.Short, 24);
      _manipulator = new ManipulatorControl( sensor );
      Init();
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

   public void periodic()
   {
      _drive.periodic();
      _manipulator.periodic();
   }

   public void resetOdometry(Pose2d pose)
   {
      _drive.resetOdometry( pose );
   }
   public void disable()
   {
      _drive.disable();
      _manipulator.disable();
   }
}