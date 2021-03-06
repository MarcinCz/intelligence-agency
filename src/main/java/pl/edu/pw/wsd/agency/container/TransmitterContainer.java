package pl.edu.pw.wsd.agency.container;

import jade.core.Agent;
import pl.edu.pw.wsd.agency.agent.TransmitterAgent;
import pl.edu.pw.wsd.agency.config.TransmitterConfiguration;
import pl.edu.pw.wsd.agency.config.properties.PropertiesTransmitterConfiguration;
import pl.edu.pw.wsd.agency.container.launcher.RunnableContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents transmitter instance
 *
 * @author marcin.czerwinski
 */
public class TransmitterContainer extends RunnableContainer {

	@Override
	public List<Agent> getAgentsToRun() {
		List<Agent> agents = new ArrayList<>();
		addTransmitterFromProperties(agents, "TransmitterAgent1.properties");
		addTransmitterFromProperties(agents, "TransmitterAgent2.properties");
		return agents;
	}

	private void addTransmitterFromProperties(List<Agent> agents, String propertiesFileName) {
		TransmitterConfiguration cfg = new PropertiesTransmitterConfiguration(propertiesFileName);
		agents.add(new TransmitterAgent(cfg));
	}
}
