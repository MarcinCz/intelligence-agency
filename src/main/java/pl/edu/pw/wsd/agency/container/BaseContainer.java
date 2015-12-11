package pl.edu.pw.wsd.agency.container;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import jade.core.Agent;
import pl.edu.pw.wsd.agency.agent.meta.JadeAgentDescription;

/**
 * Base class for all jade containers.
 * @author marcin.czerwinski
 *
 */
public abstract class BaseContainer {

	protected List<JadeAgentDescription> createAgentDescriptions(Class<? extends Agent> agentClass, int instancesToCreate) {
		List<JadeAgentDescription> descriptions = new ArrayList<>();
		IntStream.rangeClosed(1, instancesToCreate).forEach(i -> descriptions.add(createAgentDescription(agentClass))); //maybe not the best approach, but I wanted to try IntStream
		return descriptions;
	}

	private JadeAgentDescription createAgentDescription(Class<? extends Agent> agentClass) {
		return new JadeAgentDescription(agentClass.getSimpleName() + UUID.randomUUID().toString(), agentClass.getName());
	}
	
	public abstract List<JadeAgentDescription> getAgentsToRun();
	
}
