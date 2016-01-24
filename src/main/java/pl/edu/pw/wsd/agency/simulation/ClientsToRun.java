package pl.edu.pw.wsd.agency.simulation;

import java.util.ArrayList;
import java.util.List;

import jade.core.Agent;
import pl.edu.pw.wsd.agency.agent.ClientAgent;
import pl.edu.pw.wsd.agency.config.properties.PropertiesClientAgentConfiguration;

public class ClientsToRun {

	public static List<Agent> getClientsToRun() {
		List<Agent> agentsToRun = new ArrayList<>();
		agentsToRun.add(new ClientAgent(new PropertiesClientAgentConfiguration("ClientAgent1.properties")));
		agentsToRun.add(new ClientAgent(new PropertiesClientAgentConfiguration("ClientAgent2.properties")));
		agentsToRun.add(new ClientAgent(new PropertiesClientAgentConfiguration("ClientAgent3.properties")));
		
		return agentsToRun;
	}
}
