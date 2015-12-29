package pl.edu.pw.wsd.agency.container;

import java.util.ArrayList;
import java.util.List;

import pl.edu.pw.wsd.agency.agent.SampleTransmitterAgent;
import pl.edu.pw.wsd.agency.agent.TransmitterAgent;
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
		descriptions.add(createAgentDescription(TransmitterAgent.class, "TransmitterAgent1.properties"));
		//descriptions.add(createAgentDescription(TransmitterAgent.class, "TransmitterAgent2.properties"));
		return descriptions;
	}

}
