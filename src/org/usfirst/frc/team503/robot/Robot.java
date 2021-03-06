
package org.usfirst.frc.team503.robot;

import org.usfirst.frc.team503.auton.CenterPegCenterStart;
import org.usfirst.frc.team503.auton.LeftPegLeftStartAuton;
import org.usfirst.frc.team503.commands.ArcadeDriveCommand;
import org.usfirst.frc.team503.commands.GyroTurnCommand;
import org.usfirst.frc.team503.commands.TeleopDeflectorCommand;
import org.usfirst.frc.team503.commands.TeleopTurretCommand;
import org.usfirst.frc.team503.commands.TurnTurretCommand;
import org.usfirst.frc.team503.subsystems.DeflectorSubsystem;
import org.usfirst.frc.team503.subsystems.DrivetrainSubsystem;
import org.usfirst.frc.team503.subsystems.IndexerSubsystem;
import org.usfirst.frc.team503.subsystems.ShooterSubsystem;
import org.usfirst.frc.team503.subsystems.TurretSubsystem;
import org.usfirst.frc.team503.utils.Logger;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public static RobotHardwarePracticeBot bot = null;
	private static double startTime;


	
	/**
	 * RobotInit - Fires when robot is powered-up 
	 */
	@Override
	public void robotInit() {
        NetworkTable.globalDeleteAll(); //Removes unused garbage from SmartDashboard
        NetworkTable.initialize();      //Initialize Network Tables
		
        bot = new RobotHardwarePracticeBot();
		bot.initialize();
		bot.logSmartDashboard();         /*put name of selected bot on smartdashboard */
		
		//SteamworksChooser.getInstance().autonInitChooser();
		RobotState.getInstance().setState(RobotState.State.DISABLED);
	}
 
	/**
	 * disabledInit - fires when disabled mode is pressed.
	 */
	@Override
	public void disabledInit() {
		DrivetrainSubsystem.getInstance().stopTrapezoidControl();    	
		DrivetrainSubsystem.getInstance().percentVoltageMode();
		ShooterSubsystem.getInstance().setMotorPower(0);
		IndexerSubsystem.getInstance().setMotorPower(0);
		DeflectorSubsystem.getInstance().setMotorPower(0);
		TurretSubsystem.getInstance().getThread().stopTurret();
		RobotState.getInstance().setState(RobotState.State.DISABLED);
	}

	/**
	 * disabledPeriodic - fires when disabled mode is pressed.
	 */
	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * autonomousInit - fires when Auto mode is selected  
	 */
	@Override
	public void autonomousInit() {
		//SteamworksChooser.getInstance().executeAuton();
		startTime = Timer.getFPGATimestamp();
		RobotState.getInstance().setState(RobotState.State.AUTON);
		//(new LeftPegLeftStartAuton()).start();
		(new GyroTurnCommand(30)).start();
	}

	/**
	 * AutonomousPeriodic - This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		//DrivetrainSubsystem.getInstance()trainSubsystem.getInstance().populateLog(startTime);
		Scheduler.getInstance().run();
		if (!Robot.bot.getName().equals("ProgrammingBot")){
			SmartDashboard.putNumber("Shooter Motor Speed", ShooterSubsystem.getInstance().getSpeed());
			DeflectorSubsystem.getInstance().sendDashboardData();
			TurretSubsystem.getInstance().sendDashboardData();
		}
	}

	/**
	 * teleopInit - first with Telop is selected 
	 */
	@Override
	public void teleopInit() {
		// Ensure the autonomous commands are cancelled if not finished 
		//if (autonomousCommand != null)
		//	autonomousCommand.cancel();
		DrivetrainSubsystem.getInstance().stopTrapezoidControl();    	
		DrivetrainSubsystem.getInstance().percentVoltageMode();
    	DrivetrainSubsystem.getInstance().resetEncoders();
		OI.initialize();
		RobotState.getInstance().setState(RobotState.State.TELEOP);
	    startTime = Timer.getFPGATimestamp();
	    //start commands that use joysticks and dpads manually from Robot.java
    	(new ArcadeDriveCommand()).start();
    	if (!Robot.bot.getName().equals("ProgrammingBot")){
    	    TurretSubsystem.getInstance().getThread().startTurret();
    		//(new TeleopTurretCommand()).start();
        	//(new TeleopDeflectorCommand()).start();
    	}
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		DrivetrainSubsystem.getInstance().populateLog(startTime);
		if (!Robot.bot.getName().equals("ProgrammingBot")){
			SmartDashboard.putNumber("Shooter Motor Speed", ShooterSubsystem.getInstance().getSpeed()/4);
			SmartDashboard.putNumber("Shooter position", ShooterSubsystem.getInstance().getPosition());
			SmartDashboard.putNumber("Shooter Enc Speed", ShooterSubsystem.getInstance().getEncSpeed());
			DeflectorSubsystem.getInstance().sendDashboardData();
			TurretSubsystem.getInstance().sendDashboardData();
		}
	}
	
	/**
	 * This function is called to initialize the test mode 
	 */
	@Override
	public void testInit(){
		Logger.froglog("In Roborio Test Mode...initiating Power On Self Test (POST) Diagnostics ...");
		RobotState.getInstance().setState(RobotState.State.TEST);
	}
	
	/**
	 * testPeriodic - This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		Scheduler.getInstance().run();
	}
	
}
