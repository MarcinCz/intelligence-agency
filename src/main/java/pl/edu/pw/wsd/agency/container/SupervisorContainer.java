package pl.edu.pw.wsd.agency.container;

import java.util.ArrayList;
import java.util.List;

import jade.core.Agent;
import pl.edu.pw.wsd.agency.agent.SupervisorAgent;
import pl.edu.pw.wsd.agency.config.SupervisorConfiguration;
import pl.edu.pw.wsd.agency.config.properties.PropertiesSupervisorConfiguration;
import pl.edu.pw.wsd.agency.container.launcher.RunnableContainer;

public class SupervisorContainer extends RunnableContainer {

	@Override
	public List<Agent> getAgentsToRun() {
		List<Agent> agents = new ArrayList<>();
		addSupervisorFromProperties(agents, "SupervisorAgent.properties");
		return agents;
	}
	
	private void addSupervisorFromProperties(List<Agent> agents, String propertiesFileName) {
		SupervisorConfiguration	cfg = new PropertiesSupervisorConfiguration(propertiesFileName);
		agents.add(new SupervisorAgent(cfg));
	}

}
