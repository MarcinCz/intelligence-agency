package pl.edu.pw.wsd.agency.simulation;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import jade.core.Agent;
import javafx.geometry.Point2D;
import pl.edu.pw.wsd.agency.agent.TransmitterAgent;
import pl.edu.pw.wsd.agency.config.TransmitterConfiguration;
import pl.edu.pw.wsd.agency.config.properties.PropertiesTransmitterConfiguration;

public class TransmittersToRun {

	public static List<Agent> getTransmittersToRun() {
		List<Agent> agentsToRun = new ArrayList<>();
		
		agentsToRun.add(new TransmitterAgent(new PropertiesTransmitterConfiguration("TransmitterAgent1.properties")));
		agentsToRun.add(new TransmitterAgent(new PropertiesTransmitterConfiguration("TransmitterAgent2.properties")));
		
		Point2D[] transmitter3Path = new Point2D[]{new Point2D(-100, -50), new Point2D(-100, 50)};
		agentsToRun.add(new TransmitterAgent(getTransmitterConfiguration(transmitter3Path, 20)));
		
		Point2D[] transmitter4Path = new Point2D[]{new Point2D(100, 50)};
		agentsToRun.add(new TransmitterAgent(getTransmitterConfiguration(transmitter4Path, 0)));
		
		return agentsToRun;
	}
	
	private static TransmitterConfiguration getTransmitterConfiguration(Point2D[] path, double speed) {
		TransmitterConfiguration config = mock(TransmitterConfiguration.class);
		
		when(config.getCreateNewStatusPeriod()).thenReturn(1000);
		when(config.getMoveBehaviourPeriod()).thenReturn(1000);
		when(config.getPropagateStatusesPeriod()).thenReturn(1000);

		when(config.getStartingPositionIndex()).thenReturn(0);
		when(config.getSpeed()).thenReturn(speed);
		when(config.getSignalRange()).thenReturn(50.0);
		when(config.getAgentDirection()).thenReturn(true);
		when(config.getPath()).thenReturn(path);
		return config;
	}
}
