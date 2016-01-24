package pl.edu.pw.wsd.agency.container.launcher;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
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
 * It creates new containers based on container properties.
 *
 * @author marcin.czerwinski
 *
 */
public class ContainerLauncher {

	private static final Logger log = LogManager.getLogger();

	public static void runMainContainer(boolean withGUI) {
		jade.core.Runtime jadeRuntime = jade.core.Runtime.instance();
		ProfileImpl profile = new ProfileImpl();
		profile.setParameter(Profile.CONTAINER_NAME, "MainContainer");
		if(withGUI) {
			profile.setParameter(Profile.GUI, "true");
		}
		jadeRuntime.createMainContainer(profile);
	}

	public static void runMainContainer() {
		runMainContainer(true);
	}
	
	public static void runRemoteContainer(RunnableContainer container) {
		jade.core.Runtime jadeRuntime = jade.core.Runtime.instance();
		String containerName = container.getClass().getSimpleName() + "-" + RandomStringUtils.randomAlphanumeric(8);		
		
		log.info("Creating container [" + containerName + "]");
		ProfileImpl profile = new ProfileImpl();
		profile.setParameter(Profile.CONTAINER_NAME, containerName);
		profile.setParameter(Profile.MAIN_HOST, ContainerConfig.getMainContainerHost());
		profile.setParameter(Profile.MAIN_PORT, ContainerConfig.getMainContainerPort());
		AgentContainer c = jadeRuntime.createAgentContainer(profile);
		log.info("Container [" + containerName + "] created");

		Map<String, Integer> agentsNameCounter = new HashMap<>();
		for(Agent agent: container.getAgentsToRun()) {
			try {
				
				String agentName = getAgentName(agent, agentsNameCounter);
				AgentController agentController = c.acceptNewAgent(agentName, agent);
				agentController.start();
				log.debug("Added agent [" + agentName + "]");
			} catch (StaleProxyException e) {
				log.error("Could not add agent to the container", e);
			}
		}
		log.info("Added all the agents to container [" + containerName + "]");
	}
	
	private static String getAgentName(Agent agent, Map<String, Integer> agentsNameCounter) {
		String className = agent.getClass().getSimpleName();
		int currentCount;

		if(agentsNameCounter.containsKey(className)) {
			currentCount = agentsNameCounter.get(className) + 1;
		} else {
			currentCount = 1;
		}
		
		String klasa = null;
		if(className.equals("ClientAgent")){
			klasa="C"; 
		}
		if(className.equals("TransmitterAgent")){
			klasa="T"; 
		}
		if(className.equals("SupervisorAgent")){
			klasa="S"; 
		}
		agentsNameCounter.put(className, currentCount);
		return klasa + "_" + currentCount;
	}
}
