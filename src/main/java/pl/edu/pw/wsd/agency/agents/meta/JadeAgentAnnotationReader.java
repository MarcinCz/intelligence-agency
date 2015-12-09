package pl.edu.pw.wsd.agency.agents.meta;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;

/**
 * Reader for classes annotated with {@link JadeAgent} annotation.
 * @author marcin.czerwinski
 *
 */
public class JadeAgentAnnotationReader {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final String AGENTS_PACKAGE = "pl.edu.pw.wsd.agency.agents";

	public List<JadeAgentDescription> listJadeAgentDescriptions() {

		List<JadeAgentDescription> agentDescriptions = new ArrayList<>();
		Reflections reflections = new Reflections(AGENTS_PACKAGE);
		Set<Class<? extends Object>> annotatedClasses = reflections.getTypesAnnotatedWith(JadeAgent.class);
		for (Class<? extends Object> annotatedClass : annotatedClasses) {
			JadeAgent annotation = annotatedClass.getAnnotation(JadeAgent.class);
			if (annotation.instances() == 1) {
				JadeAgentDescription desc = new JadeAgentDescription(annotation.localName(), annotatedClass.getName());
				agentDescriptions.add(desc);
				LOGGER.debug("Listed new Jade agent description [" + desc + "]");
			} else {
				for (int i = 0; i < annotation.instances(); i++) {
					JadeAgentDescription desc = new JadeAgentDescription(annotation.localName() + i, annotatedClass.getName());
					agentDescriptions.add(desc);
					LOGGER.debug("Listed new Jade agent description [" + desc + "]");
				}
			}
		}
		return agentDescriptions;
	}
	
	
}
