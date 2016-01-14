package pl.edu.pw.wsd.agency.container.launcher;

import java.util.List;

import jade.core.Agent;

/**
 * Base class for containers which can be launcher with {@link ContainerLauncher}
 * @author marcin.czerwinski
 *
 */
public abstract class RunnableContainer {

	public abstract List<Agent> getAgentsToRun();
}
