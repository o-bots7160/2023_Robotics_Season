package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;

class SwervePath {
    private RobotContainer robot;
    private int index;
    private Pose2d this_list[];

    public SwervePath( Pose2d new_list[] )
    {
        robot = RobotContainer.getInstance();
        this_list = new_list;
        index = 0;
        System.out.println("Next Pose: " + index + this_list[index]);
    }
    public boolean atDestination()
    {
        boolean done = false;
        System.out.println("Driving to: " + this_list[index]);
        if ( ! robot._drive.move_Pose2d( this_list[index] ) )
        {
            index++;
            if ( index >= this_list.length )
            {
                done = true;
                robot.drive(new Translation2d(0,0), 0, true, false);
            }
        }
        return done;
    }

    static Pose2d transform( Pose2d new_item)
    {
        double new_x = 16.54 - new_item.getX(); // need half field width
        double new_y = Units.feetToMeters(8.25) + new_item.getY(); // need half field height
        Rotation2d new_rotation = new Rotation2d( new_item.getRotation().getRadians() - Math.PI );
 
        return new Pose2d( new_x, new_y, new_rotation);
    }

    public void swapSides() {
        int index = 0;
        for ( Pose2d item : this_list) {
            this_list[ index ] = transform( item );
            index++;
        }
    }
    public Pose2d[] points(){
        return this_list;
    }
}
