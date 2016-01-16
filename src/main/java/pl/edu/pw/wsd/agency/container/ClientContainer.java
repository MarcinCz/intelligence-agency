package pl.edu.pw.wsd.agency.container;

import java.util.ArrayList;
import java.util.List;

import jade.core.Agent;
import pl.edu.pw.wsd.agency.agent.ClientAgent;
import pl.edu.pw.wsd.agency.config.ClientAgentConfiguration;
import pl.edu.pw.wsd.agency.config.properties.PropertiesClientAgentConfiguration;
import pl.edu.pw.wsd.agency.container.launcher.RunnableContainer;

/**
 * Represents transmitter instance
 * @author marcin.czerwinski
 *
 */
public class ClientContainer extends RunnableContainer {

	@Override
	public List<Agent> getAgentsToRun() {
		List<Agent> agents = new ArrayList<>();
		addClientFromProperties(agents, "ClientAgent1.properties");
		return agents;
	}
	
	private void addClientFromProperties(List<Agent> agents, String propertiesFileName) {
		ClientAgentConfiguration cfg = new PropertiesClientAgentConfiguration(propertiesFileName);
		agents.add(new ClientAgent(cfg));
	}

}
