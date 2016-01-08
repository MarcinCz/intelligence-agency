package pl.edu.pw.wsd.agency.container;

import java.util.List;

import jade.core.Agent;
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
	public List<Agent> getAgentsToRun() {
	    return null;
	}
}
