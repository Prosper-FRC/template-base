package frc.robot.subsystems.elevator;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.simulation.EncoderSim;
import edu.wpi.first.wpilibj.simulation.SingleJointedArmSim;
import frc.robot.subsystems.elevator.ElevatorConstants.SIM;

public class ElevatorIOSim implements ElevatorIO { 

    private Encoder realEncoder = new Encoder(ElevatorConstants.encoderPorts[0],ElevatorConstants.encoderPorts[1]);
    private PWMSparkMax pseudoMotor = new PWMSparkMax(0); 

    private SingleJointedArmSim armSim = new SingleJointedArmSim(
        DCMotor.getNEO(1), 
        ElevatorConstants.gearRatio, 
        SingleJointedArmSim.estimateMOI(SIM.metersArmLength, SIM.kiloArmMass),
        SIM.metersArmLength, 
        SIM.minAngle, 
        SIM.maxAngle, 
        true, 
        0); 
    
    private EncoderSim encoderSim = new EncoderSim(realEncoder); 
    
    public ElevatorIOSim(){ 
        realEncoder.setDistancePerPulse(ElevatorConstants.conversionFactor);
    } 
    
    public double getEncoderPos(){ 
        return encoderSim.getDistance();
    }
    
    public void updateInputs(ElevatorIOInputs inputs){ 
        armSim.update(0.02); 
        inputs.velocity = armSim.getVelocityRadPerSec(); 
        inputs.encoderPosRads = encoderSim.getDistance(); 
        inputs.currentOutput = armSim.getCurrentDrawAmps(); 
    } 

    public void setManualArm(double volts){ 
        pseudoMotor.setVoltage(MathUtil.clamp(volts,-12,12));
    }

}
