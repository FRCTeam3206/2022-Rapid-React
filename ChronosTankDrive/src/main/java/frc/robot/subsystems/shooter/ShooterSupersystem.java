package frc.robot.subsystems.shooter;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Subsystem;
import static frc.robot.Constants.Buttons.*;
public class ShooterSupersystem extends Subsystem{
    private Shooter shooter;
    private Hood hood;
    private Limelight limelight;
    private DifferentialDrive driveTrain;
    private GenericHID joystick;
    public ShooterSupersystem(Shooter shooter,Hood hood,Limelight limelight,DifferentialDrive driveTrain,GenericHID joystick){
        this.shooter=shooter;
        this.hood=hood;
        this.limelight=limelight;
        this.driveTrain=driveTrain;
        this.joystick=joystick;
    }
    @Override
    public void init() {
        shooter.init();
        hood.init();
        
    }
    @Override
    public void periodic() {
        double[] distanceAndAngle=limelight.getAdjustedDistanceAndAngleToTarget();
        double distance=distanceAndAngle[0];
        double angle=distanceAndAngle[1];
        boolean aligned=false;
        if(joystick.getRawButton(B_ALIGN)){
            double turn=0;
            if(Math.abs(angle)>10){
                turn=.7;
            }else if(Math.abs(angle)<3){
                turn=.5;
            }else{
                aligned=true;
            }
            if(angle>0){
                turn*=-1;
            }
        hood.setAngle(60);//There will be a function based on ll to find this
        }
        if(joystick.getRawButton(B_SHOOT)){
            shooter.shoot(2650);//There will be a function based on ll to find this
            
        }
    }
}