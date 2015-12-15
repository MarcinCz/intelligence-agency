package pl.edu.pw.wsd.agency.agent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.StaleProxyException;
import pl.edu.pw.wsd.agency.config.ContainerConfig;

/**
 * Sample container running agent. It dynamically creates new container
 * @author marcin.czerwinski
 *
 */
public class ContainerRunnerAgent extends Agent {

	private static final long serialVersionUID = -9037610317545055037L;
	private static final Logger log = LogManager.getLogger();

	@Override
	protected void setup() {
		super.setup();
		log.debug("Main container agent here");
		jade.core.Runtime jadeRuntime = jade.core.Runtime.instance();
		ProfileImpl profile = new ProfileImpl();
		profile.setParameter(Profile.CONTAINER_NAME, "ContainerFromCode");
		profile.setParameter(Profile.MAIN_HOST, ContainerConfig.getMainContainerHost());
		profile.setParameter(Profile.MAIN_PORT, ContainerConfig.getMainContainerPort());
		AgentContainer c = jadeRuntime.createAgentContainer(profile);
		try {
			c.acceptNewAgent("testAgent", new AgentFromContainerRunner());
		} catch (StaleProxyException e) {
			log.error("Could not add agent to dynamically created container", e);
		}
	}
	
	@SuppressWarnings("serial")
	private static class AgentFromContainerRunner extends Agent {
		@Override
		protected void setup() {
			super.setup();
			log.debug("Agent from container runner");
		}
	}
}
