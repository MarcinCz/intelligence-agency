package pl.edu.pw.wsd.agency.container;

import java.util.ArrayList;
import java.util.List;

import pl.edu.pw.wsd.agency.agent.LocationRegistryAgent;
import pl.edu.pw.wsd.agency.agent.TransmitterAgent;
import pl.edu.pw.wsd.agency.agent.meta.JadeAgentDescription;
import pl.edu.pw.wsd.agency.container.launcher.RunnableContainer;

public class LocationRegistryContainer extends RunnableContainer{
    @Override
    public List<JadeAgentDescription> getAgentsToRun() {
        List<JadeAgentDescription> descriptions = new ArrayList<>();
        descriptions.add(createAgentDescription(LocationRegistryAgent.class, "TransmitterAgent2.properties"));
        //descriptions.add(createAgentDescription(TransmitterAgent.class, "TransmitterAgent2.properties"));
        return descriptions;
    }
}
