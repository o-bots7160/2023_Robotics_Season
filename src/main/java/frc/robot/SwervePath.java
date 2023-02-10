package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;

class SwervePath {
    private RobotContainer robot;
    private int index;
    private Pose2d this_list[];

    public SwervePath( Pose2d new_list[] )
    {
        robot = RobotContainer.getInstance();
        this_list = new_list;
        index = 0;
    }
    public boolean atDestination()
    {
        boolean done = false;
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
}
