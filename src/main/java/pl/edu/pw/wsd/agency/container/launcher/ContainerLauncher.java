package pl.edu.pw.wsd.agency.container.launcher;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import pl.edu.pw.wsd.agency.config.ContainerConfig;

/**
 * Base class for container launcher.
 * It starts new jade.Boot instance, based on container properties.
 * @author marcin.czerwinski
 *
 */
abstract class ContainerLauncher {

	private static final Logger log = LogManager.getLogger();
	
	protected static void runContainer(RunnableContainer container) {

		if(container == null) {
			throw new IllegalStateException("Could not run container. It's not initialized");
		}
		
		if(container.isMainContainer()) {
			runMainContainer(container);
		} else {
			runRemoteContainer(container);
		}

	}

	private static void runMainContainer(RunnableContainer container) {
		
		log.info("Attempting to init jade.Boot");
		StringBuilder arguments = new StringBuilder();
		arguments.append("-gui ");
		
        log.debug("Jade.Boot init arguments: " + arguments.toString());
        String[] jadeBootInitArguments = StringUtils.split(arguments.toString());
        jade.Boot.main(jadeBootInitArguments);
		log.info("Jade.Boot started");
	}
	
	private static void runRemoteContainer(RunnableContainer container) {
		jade.core.Runtime jadeRuntime = jade.core.Runtime.instance();
		String containerName = container.getClass().getSimpleName() + "-" + RandomStringUtils.randomAlphanumeric(8);		
		
		log.info("Creating container [" + containerName + "]");
		ProfileImpl profile = new ProfileImpl();
		profile.setParameter(Profile.CONTAINER_NAME, containerName);
		profile.setParameter(Profile.MAIN_HOST, ContainerConfig.getMainContainerHost());
		profile.setParameter(Profile.MAIN_PORT, ContainerConfig.getMainContainerPort());
		AgentContainer c = jadeRuntime.createAgentContainer(profile);
		log.info("Container [" + containerName + "] created");

		for(Agent agent: container.getAgentsToRun()) {
			try {
				String agentName = agent.getClass().getSimpleName() + "-" + RandomStringUtils.randomAlphanumeric(8);
				AgentController agentController = c.acceptNewAgent(agentName, agent);
				agentController.start();
				log.debug("Added agent [" + agentName + "]");
			} catch (StaleProxyException e) {
				log.error("Could not add agent to the container", e);
			}
		}
		log.info("Added all the agents to container [" + containerName + "]");
	}
}
