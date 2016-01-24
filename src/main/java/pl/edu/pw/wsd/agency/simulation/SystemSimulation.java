package pl.edu.pw.wsd.agency.simulation;

import java.util.ArrayList;
import java.util.List;

import jade.core.Agent;
import javafx.geometry.Point2D;
import pl.edu.pw.wsd.agency.agent.ClientAgent;
import pl.edu.pw.wsd.agency.agent.LocationRegistryAgent;
import pl.edu.pw.wsd.agency.agent.SupervisorAgent;
import pl.edu.pw.wsd.agency.agent.TransmitterAgent;
import pl.edu.pw.wsd.agency.agent.ViewAgent;
import pl.edu.pw.wsd.agency.config.SupervisorConfiguration;
import pl.edu.pw.wsd.agency.config.properties.PropertiesClientAgentConfiguration;
import pl.edu.pw.wsd.agency.config.properties.PropertiesTransmitterConfiguration;
import pl.edu.pw.wsd.agency.container.launcher.ContainerLauncher;
import pl.edu.pw.wsd.agency.container.launcher.RunnableContainer;

public class SystemSimulation {

	public static void main(String[] args) throws InterruptedException {
		SystemSimulation simulation = new SystemSimulation();
		simulation.runSimulation();
	}
	
	private void runSimulation() throws InterruptedException {
		runPlatform(getAgentsToRun());
		wait();
	}

	private List<Agent> getAgentsToRun() {
		List<Agent> agentsToRun = new ArrayList<>();
		agentsToRun.add(new ViewAgent());
		agentsToRun.add(new LocationRegistryAgent());
		agentsToRun.add(new ClientAgent(new PropertiesClientAgentConfiguration("ClientAgent1.properties")));
		agentsToRun.add(new TransmitterAgent(new PropertiesTransmitterConfiguration("TransmitterAgent1.properties")));
		agentsToRun.add(new TransmitterAgent(new PropertiesTransmitterConfiguration("TransmitterAgent2.properties")));
		agentsToRun.add(new SupervisorAgent(getSupervisorConfiguration()));
		return agentsToRun;
	}
	
	private SupervisorConfiguration getSupervisorConfiguration() {
		return new SupervisorConfiguration() {
			
			// @formatter:off
			@Override public Point2D getStartingPosition() {return new Point2D(10, 20);}
			@Override public int getStartingPositionIndex() {return 1;}
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
	
	private void runPlatform(List<Agent> agentsToRun) {
        new Thread(() -> {
            ContainerLauncher.runMainContainer(false);
            ContainerLauncher.runRemoteContainer(new RunnableContainer() {

                @Override
                public List<Agent> getAgentsToRun() {
                    return agentsToRun;
                }
            });
        }).start();
    }
}
