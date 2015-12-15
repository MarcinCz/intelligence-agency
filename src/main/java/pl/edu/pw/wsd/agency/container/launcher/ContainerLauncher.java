package pl.edu.pw.wsd.agency.container.launcher;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
		log.info("Attempting to init jade.Boot");
		String[] jadeBootInitArguments = getJadeBootInitArguments(container);
		jade.Boot.main(jadeBootInitArguments);
		log.info("Jade.Boot started");

	}

	private static String[] getJadeBootInitArguments(RunnableContainer container) {
		
		StringBuilder arguments = new StringBuilder();
		if(container.isMainContainer()) {
			arguments.append("-gui ");
		} else {
			arguments.append(String.format("-host %s -port %s -container ",
											ContainerConfig.getMainContainerHost(),
											ContainerConfig.getMainContainerPort()));
		}
		List<String> agentsToRun = container.getAgentsToRun()
				.stream()
				.map(d -> d.getLocalName() + ":" + d.getQualifiedName() + "(" + d.getPropertiesFileName() + ")")
				.collect(Collectors.toList());
			
		arguments.append("-agents " + StringUtils.join(agentsToRun, ";"));
		
        log.debug("Jade.Boot init arguments: " + arguments.toString());
        return StringUtils.split(arguments.toString());
	}
}
