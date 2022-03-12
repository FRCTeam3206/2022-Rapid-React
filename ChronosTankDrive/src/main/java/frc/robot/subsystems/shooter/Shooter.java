package frc.robot.subsystems.shooter;

import com.ctre.phoenix.motorcontrol.VictorSPXControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Subsystem;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import static frc.robot.subsystems.Buttons.*;
public class Shooter extends Subsystem{

    private GenericHID m_joystick;

    private CANSparkMax m_shooter;
    private VictorSPX kickerWheel;
    private SparkMaxPIDController m_pidController;
    private RelativeEncoder m_encoder;
    public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM;
    private double shooterSetPoint = 0;

    private double inc;
    private boolean shooterRunning = false;

    public Shooter(int kShooterPort, int kickerPort, double increment, GenericHID joystick) {
        m_shooter = new CANSparkMax(kShooterPort, MotorType.kBrushless);
        //m_shooter.setInverted(true);
        m_joystick = joystick;
        // m_encoder = new Encoder(kEncoderPortA, kEncoderPortB);

        m_shooter.restoreFactoryDefaults();

        m_pidController = m_shooter.getPIDController();

        m_encoder = m_shooter.getEncoder();

        // Use SetDistancePerPulse to set the multiplier for GetDistance
        // This is set up assuming a 6 inch wheel with a 360 CPR encoder.
        // m_encoder.setDistancePerPulse((Math.PI * 6) / 360.0);
        kP = Constants.sparkmax_kP;
        kI = Constants.sparkmax_kI;
        kD = Constants.sparkmax_kD;
        kIz = Constants.sparkmax_kIz;
        kFF = Constants.sparkmax_kFF;
        kMaxOutput = Constants.sparkmax_kMaxOut;
        kMinOutput = Constants.sparkmax_kMinOut;
        maxRPM = 5700;

        // set PID coefficients
        m_pidController.setP(kP);
        m_pidController.setI(kI);
        m_pidController.setD(kD);
        m_pidController.setIZone(kIz);
        m_pidController.setFF(kFF);
        m_pidController.setOutputRange(kMinOutput, kMaxOutput);

        // display PID coefficients on SmartDashboard
        SmartDashboard.putNumber("P Gain", kP);
        SmartDashboard.putNumber("I Gain", kI);
        SmartDashboard.putNumber("D Gain", kD);
        SmartDashboard.putNumber("I Zone", kIz);
        SmartDashboard.putNumber("Feed Forward", kFF);
        SmartDashboard.putNumber("Max Output", kMaxOutput);
        SmartDashboard.putNumber("Min Output", kMinOutput);

        inc = increment;

        kickerWheel=new VictorSPX(kickerPort);
    }

    public void showEncoderValOnSmartDashboard() {
        SmartDashboard.putNumber("Encoder", m_encoder.getVelocity());
    }
    public void setSpeed(double rpm_target){
      shooterSetPoint=rpm_target/maxRPM;
      shooterSetPoint = MathUtil.clamp(shooterSetPoint, kMinOutput, kMaxOutput);
      rpm_target=shooterSetPoint*maxRPM;
      m_pidController.setReference(-rpm_target, CANSparkMax.ControlType.kVelocity);
      SmartDashboard.putNumber("SetPoint", shooterSetPoint);
      SmartDashboard.putNumber("RPM_Target", rpm_target);
      SmartDashboard.putNumber("ProcessVariable", m_encoder.getVelocity());
    }
    long kickerStartTime=-1;
    boolean canShoot=true;
    boolean isShooting=false;
    public void periodic() {
        // read PID coefficients from SmartDashboard
        double p = SmartDashboard.getNumber("P Gain", 0);
        double i = SmartDashboard.getNumber("I Gain", 0);
        double d = SmartDashboard.getNumber("D Gain", 0);
        double iz = SmartDashboard.getNumber("I Zone", 0);
        double ff = SmartDashboard.getNumber("Feed Forward", 0);
        double max = SmartDashboard.getNumber("Max Output", 0);
        double min = SmartDashboard.getNumber("Min Output", 0);
    
        // if PID coefficients on SmartDashboard have changed, write new values to controller
        if((p != kP)) { m_pidController.setP(p); kP = p; }
        if((i != kI)) { m_pidController.setI(i); kI = i; }
        if((d != kD)) { m_pidController.setD(d); kD = d; }
        if((iz != kIz)) { m_pidController.setIZone(iz); kIz = iz; }
        if((ff != kFF)) { m_pidController.setFF(ff); kFF = ff; }
        if((max != kMaxOutput) || (min != kMinOutput)) { 
          m_pidController.setOutputRange(min, max); 
          kMinOutput = min; kMaxOutput = max; 
        }
    
        /**
         * PIDController objects are commanded to a set point using the 
         * SetReference() method.
         * 
         * The first parameter is the value of the set point, whose units vary
         * depending on the control type set in the second parameter.
         * 
         * The second parameter is the control type can be set to one of four 
         * parameters:
         *  com.revrobotics.CANSparkMax.ControlType.kDutyCycle
         *  com.revrobotics.CANSparkMax.ControlType.kPosition
         *  com.revrobotics.CANSparkMax.ControlType.kVelocity
         *  com.revrobotics.CANSparkMax.ControlType.kVoltage
         */
        if(m_joystick.getRawButtonReleased(B_SHOOT)){
          canShoot=true;
        }
        if (m_joystick.getRawButton(B_SHOOT)) {
          isShooting=true;
          double desiredRPM=2650;
          setSpeed(desiredRPM);//To be implemented with LL This is just for Testing
          if(Math.abs(-m_encoder.getVelocity()-desiredRPM)<25){
            //Shoot
            kickerWheel.set(VictorSPXControlMode.PercentOutput, 1);
          }
        }else{
          m_shooter.set(0);
          kickerWheel.set(VictorSPXControlMode.PercentOutput, 0);
        }
        
        
        
            // double targetPositionRotations = m_joystick.getLeftY() * 10.0 * 4096;
            // m_hoodMotor.set(ControlMode.Position, targetPositionRotations);
      }

    @Override
    public void init() {
      // TODO Auto-generated method stub
      
    }
}