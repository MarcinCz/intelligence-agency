package pl.edu.pw.wsd.agency.container;

import java.util.ArrayList;
import java.util.List;

import jade.core.Agent;
import pl.edu.pw.wsd.agency.agent.LocationRegistryAgent;
import pl.edu.pw.wsd.agency.container.launcher.RunnableContainer;

public class LocationRegistryContainer extends RunnableContainer{
    
	@Override
    public List<Agent> getAgentsToRun() {
        List<Agent> descriptions = new ArrayList<>();
        descriptions.add(new LocationRegistryAgent());
        //descriptions.add(createAgentDescription(TransmitterAgent.class, "TransmitterAgent2.properties"));
        return descriptions;
    }
}
