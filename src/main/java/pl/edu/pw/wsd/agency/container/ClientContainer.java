package pl.edu.pw.wsd.agency.container;

import java.util.ArrayList;
import java.util.List;

import pl.edu.pw.wsd.agency.agent.ClientAgent;
import pl.edu.pw.wsd.agency.agent.meta.JadeAgentDescription;
import pl.edu.pw.wsd.agency.container.launcher.RunnableContainer;

/**
 * Represents transmitter instance
 * @author marcin.czerwinski
 *
 */
public class ClientContainer extends RunnableContainer {

	@Override
	public List<JadeAgentDescription> getAgentsToRun() {
		List<JadeAgentDescription> descriptions = new ArrayList<>();
		descriptions.add(createAgentDescription(ClientAgent.class, "ClientAgent1.properties"));
		return descriptions;
	}

}
