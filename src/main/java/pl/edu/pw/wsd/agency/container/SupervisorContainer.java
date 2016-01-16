package pl.edu.pw.wsd.agency.container;

import java.util.ArrayList;
import java.util.List;

import jade.core.Agent;
import pl.edu.pw.wsd.agency.agent.SupervisorAgent;
import pl.edu.pw.wsd.agency.container.launcher.RunnableContainer;

public class SupervisorContainer extends RunnableContainer {

	@Override
	public List<Agent> getAgentsToRun() {
		List<Agent> descriptions = new ArrayList<>();
		descriptions.add(new SupervisorAgent("SupervisorAgent.properties"));
		return descriptions;
	}

}
