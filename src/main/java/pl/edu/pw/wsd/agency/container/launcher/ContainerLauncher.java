package pl.edu.pw.wsd.agency.container.launcher;

import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.pw.wsd.agency.config.ContainerConfig;

/**
 * Base class for container launcher.
 * It creates new containers based on container properties.
 *
 * @author marcin.czerwinski
 */
public class ContainerLauncher {

    private static final Logger log = LogManager.getLogger();

    public static void runMainContainer() {

        log.info("Attempting to init jade.Boot");
        StringBuilder arguments = new StringBuilder();
        arguments.append("-gui ");

        log.debug("Jade.Boot init arguments: " + arguments.toString());
        String[] jadeBootInitArguments = StringUtils.split(arguments.toString());
        jade.Boot.main(jadeBootInitArguments);
        log.info("Jade.Boot started");
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
        int i = 1;
        for (Agent agent : container.getAgentsToRun()) {
            try {
                String agentName = agent.getClass().getSimpleName() + "_" + i++;
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
