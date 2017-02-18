package org.usfirst.frc.team503.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team503.robot.Robot;
import org.usfirst.frc.team503.robot.subsystems.ClimberSubsystem;
import org.usfirst.frc.team503.robot.RobotState;

/**
 *
 */
public class ClimbAccelerateCommand extends Command {
	
	public ClimbAccelerateCommand() {
		// Use requires() here to declare subsystem dependencies
		requires(ClimberSubsystem.getInstance());
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		if (RobotState.getInstance().getClimberCanAccelerate()){
			ClimberSubsystem.getInstance().setPower(-1);
			RobotState.getInstance().setClimberStatus(true);
			RobotState.getInstance().setClimberAccelerationStatus(true);
		} else {
			ClimberSubsystem.getInstance().setPower(0);
			RobotState.getInstance().setClimberStatus(false);
			RobotState.getInstance().setClimberAccelerationStatus(false);
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		return true;
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
	}
}