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
		
		Point2D[] transmitter1Path = new Point2D[]{new Point2D(270, -70)};
		agentsToRun.add(new TransmitterAgent(getTransmitterConfiguration(transmitter1Path, 00)));
		
		Point2D[] transmitter2Path = new Point2D[]{new Point2D(360, 180)};
		agentsToRun.add(new TransmitterAgent(getTransmitterConfiguration(transmitter2Path, 0)));
		
		Point2D[] transmitter3Path = new Point2D[]{new Point2D(330, 120)};
		agentsToRun.add(new TransmitterAgent(getTransmitterConfiguration(transmitter3Path, 0)));
		
		Point2D[] transmitter4Path = new Point2D[]{new Point2D(240, 270)};
		agentsToRun.add(new TransmitterAgent(getTransmitterConfiguration(transmitter4Path, 0)));
		
		Point2D[] transmitter5Path = new Point2D[]{new Point2D(230, 200)};
		agentsToRun.add(new TransmitterAgent(getTransmitterConfiguration(transmitter5Path, 0)));
		
		Point2D[] transmitter6Path = new Point2D[]{new Point2D(240, 130)};
		agentsToRun.add(new TransmitterAgent(getTransmitterConfiguration(transmitter6Path, 0)));
		
		Point2D[] transmitter7Path = new Point2D[]{new Point2D(270, -20), new Point2D(270, 60)};
		agentsToRun.add(new TransmitterAgent(getTransmitterConfiguration(transmitter7Path, 20)));
		
		Point2D[] transmitter8Path = new Point2D[]{new Point2D(170, 130)};
		agentsToRun.add(new TransmitterAgent(getTransmitterConfiguration(transmitter8Path, 0)));
		
		Point2D[] transmitter9Path = new Point2D[]{new Point2D(100, 150)};
		agentsToRun.add(new TransmitterAgent(getTransmitterConfiguration(transmitter9Path, 0)));
		
		Point2D[] transmitter10Path = new Point2D[]{new Point2D(130, 90)};
		agentsToRun.add(new TransmitterAgent(getTransmitterConfiguration(transmitter10Path, 0)));
		
		Point2D[] transmitter11Path = new Point2D[]{new Point2D(200, 10)};
		agentsToRun.add(new TransmitterAgent(getTransmitterConfiguration(transmitter11Path, 0)));
		
		Point2D[] transmitter12Path = new Point2D[]{new Point2D(30,180)};
		agentsToRun.add(new TransmitterAgent(getTransmitterConfiguration(transmitter12Path, 0)));
		
		Point2D[] transmitter13Path = new Point2D[]{new Point2D(200, -70), new Point2D(110, 45)};
		agentsToRun.add(new TransmitterAgent(getTransmitterConfiguration(transmitter13Path, 20)));
		
		Point2D[] transmitter14Path = new Point2D[]{new Point2D(-10, 210), new Point2D(-50, 240)};
		agentsToRun.add(new TransmitterAgent(getTransmitterConfiguration(transmitter14Path, 20)));
		
		Point2D[] transmitter15Path = new Point2D[]{new Point2D(10,100)};
		agentsToRun.add(new TransmitterAgent(getTransmitterConfiguration(transmitter15Path, 0)));	
		
		Point2D[] transmitter16Path = new Point2D[]{new Point2D(25, 25)};
		agentsToRun.add(new TransmitterAgent(getTransmitterConfiguration(transmitter16Path, 0)));
		
		Point2D[] transmitter17Path = new Point2D[]{new Point2D(130, -70)};
		agentsToRun.add(new TransmitterAgent(getTransmitterConfiguration(transmitter17Path, 0)));
		
		Point2D[] transmitter18Path = new Point2D[]{new Point2D(70, -110)};
		agentsToRun.add(new TransmitterAgent(getTransmitterConfiguration(transmitter18Path, 0)));
		
		Point2D[] transmitter19Path = new Point2D[]{new Point2D(30, -110), new Point2D(-80, -110)};
		agentsToRun.add(new TransmitterAgent(getTransmitterConfiguration(transmitter19Path, 20)));
		
		Point2D[] transmitter20Path = new Point2D[]{new Point2D(-50, 0)};
		agentsToRun.add(new TransmitterAgent(getTransmitterConfiguration(transmitter20Path, 0)));
		
		Point2D[] transmitter21Path = new Point2D[]{new Point2D(-120, -30)};
		agentsToRun.add(new TransmitterAgent(getTransmitterConfiguration(transmitter21Path, 0)));
		
		Point2D[] transmitter22Path = new Point2D[]{new Point2D(-90, 50), new Point2D(-150, 150)};
		agentsToRun.add(new TransmitterAgent(getTransmitterConfiguration(transmitter22Path, 20)));
		
		Point2D[] transmitter23Path = new Point2D[]{new Point2D(-200, 200)};
		agentsToRun.add(new TransmitterAgent(getTransmitterConfiguration(transmitter23Path, 0)));
		
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
