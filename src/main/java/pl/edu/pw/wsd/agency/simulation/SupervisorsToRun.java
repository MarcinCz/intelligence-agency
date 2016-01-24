package pl.edu.pw.wsd.agency.simulation;

import java.util.ArrayList;
import java.util.List;

import jade.core.Agent;
import javafx.geometry.Point2D;
import pl.edu.pw.wsd.agency.agent.SupervisorAgent;
import pl.edu.pw.wsd.agency.config.SupervisorConfiguration;

public class SupervisorsToRun {

	public static List<Agent> getSupervisorsToRun() {
		List<Agent> agentsToRun = new ArrayList<>();
		agentsToRun.add(new SupervisorAgent(getSupervisorConfiguration()));
		return agentsToRun;
	}
	
	private static SupervisorConfiguration getSupervisorConfiguration() {
		return new SupervisorConfiguration() {
			
			// @formatter:off
			@Override public int getStartingPositionIndex() {return 0;}
			@Override public double getSpeed() {return 0;}
			@Override public double getSignalRange() {return 100;}
			@Override public Point2D[] getPath() { return new Point2D[]{new Point2D(10, 20)};}
			@Override public int getMoveBehaviourPeriod() {return 1000;}
			@Override public boolean getAgentDirection() { return false;}
			@Override public int getRequestStatusesPeriod() {return 1000;}
			@Override public int getAgentHeartbeatMaxPeriod() {	return 1000;}
			// @formatter:on
		};
	}
}
