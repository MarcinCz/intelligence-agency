package pl.edu.pw.wsd.agency.container;

import java.util.List;

import jade.core.Agent;

/**
 * Base class for all jade containers.
 * @author marcin.czerwinski
 *
 */
public abstract class BaseContainer {
	
	public abstract List<Agent> getAgentsToRun();
	
}
