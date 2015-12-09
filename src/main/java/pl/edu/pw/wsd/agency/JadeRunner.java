package pl.edu.pw.wsd.agency;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pl.edu.pw.wsd.agency.agents.meta.JadeAgentAnnotationReader;

/**
 * Runs new {@link jade.Boot} instance.
 * @author marcin.czerwinski
 *
 */
public class JadeRunner {
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static void main( String[] args ) {
		LOGGER.info("Attempting to init Jade.Boot");
        String[] jadeBootInitArguments = getJadeBootInitArguments();
        LOGGER.debug("Jade.Boot init arguments: " + Arrays.toString(jadeBootInitArguments));
		jade.Boot.main(jadeBootInitArguments);
		LOGGER.info("Jade.Boot started");
    }
	
	private static String[] getJadeBootInitArguments() {
		JadeAgentAnnotationReader reader = new JadeAgentAnnotationReader();
		List<String> agentDescriptions= reader.listJadeAgentDescriptions()
			.stream()
			.map(d -> d.getLocalName() + ":" + d.getQualifiedName())
			.collect(Collectors.toList());
		
        String[] arguments = new String[] {
        		"-gui",
        		"-agents",
        		StringUtils.join(agentDescriptions, ";")
        };
        return arguments;
	}
    
}
