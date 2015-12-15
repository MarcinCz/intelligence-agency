package pl.edu.pw.wsd.agency.container;

import java.util.Arrays;
import java.util.List;

import pl.edu.pw.wsd.agency.agent.ContainerRunnerAgent;
import pl.edu.pw.wsd.agency.agent.meta.JadeAgentDescription;
import pl.edu.pw.wsd.agency.container.launcher.RunnableContainer;

/**
 * Represents jade main container
 * @author marcin.czerwinski
 *
 */
public class MainContainer extends RunnableContainer {

	public MainContainer() {
		setMainContainer(true);
	}

	@Override
	public List<JadeAgentDescription> getAgentsToRun() {
	    createAgentDescription(ContainerRunnerAgent.class, "container.properties");
		//return Arrays.asList(new JadeAgentDescription("containerRunnerAgent", ContainerRunnerAgent.class.getName(), "container.properties"));
	    return Arrays.asList(createAgentDescription(ContainerRunnerAgent.class, "container.properties"));
	}
}
