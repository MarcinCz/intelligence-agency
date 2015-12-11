package pl.edu.pw.wsd.agency.container;

import java.util.ArrayList;
import java.util.List;

import pl.edu.pw.wsd.agency.agent.SampleTransmitterAgent;
import pl.edu.pw.wsd.agency.agent.meta.JadeAgentDescription;
import pl.edu.pw.wsd.agency.container.launcher.RunnableContainer;

/**
 * Represents transmitter instance
 * @author marcin.czerwinski
 *
 */
public class TransmitterContainer extends RunnableContainer {

	@Override
	public List<JadeAgentDescription> getAgentsToRun() {
		List<JadeAgentDescription> descriptions = new ArrayList<>();
		descriptions.addAll(createAgentDescriptions(SampleTransmitterAgent.class, 2));
		return descriptions;
	}

}
