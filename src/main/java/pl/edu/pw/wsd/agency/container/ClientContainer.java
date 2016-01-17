package pl.edu.pw.wsd.agency.container;

import jade.core.Agent;
import pl.edu.pw.wsd.agency.agent.ClientAgent;
import pl.edu.pw.wsd.agency.container.launcher.RunnableContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents transmitter instance
 * @author marcin.czerwinski
 *
 */
public class ClientContainer extends RunnableContainer {

	@Override
	public List<Agent> getAgentsToRun() {
		List<Agent> descriptions = new ArrayList<>();
		descriptions.add(new ClientAgent("ClientAgent1.properties"));
		descriptions.add(new ClientAgent("ClientAgent2.properties"));
		return descriptions;
	}

}
