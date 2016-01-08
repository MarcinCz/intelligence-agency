package pl.edu.pw.wsd.agency.container;

import java.util.ArrayList;
import java.util.List;

import pl.edu.pw.wsd.agency.agent.SupervisorAgent;
import pl.edu.pw.wsd.agency.agent.meta.JadeAgentDescription;

public class SupervisorContainer extends BaseContainer {

	@Override
	public List<JadeAgentDescription> getAgentsToRun() {
		List<JadeAgentDescription> descriptions = new ArrayList<>();
		descriptions.add(createAgentDescription(SupervisorAgent.class, "SupervisorAgent.properties"));
		return descriptions;
	}

}
