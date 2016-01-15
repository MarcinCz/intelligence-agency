package pl.edu.pw.wsd.agency.agent;

import com.google.common.collect.Maps;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.pw.wsd.agency.agent.behaviour.AgentsLocationServiceBehaviour;
import pl.edu.pw.wsd.agency.location.PhisicalDeviceLocation;

import java.util.HashMap;
import java.util.Map;

public class LocationRegistryAgent extends Agent {

    private static final long serialVersionUID = 6818961843348892572L;

    private static final Logger log = LogManager.getLogger();

    private Map<AID, PhisicalDeviceLocation> agentsLocation;

    @Override
    protected void setup() {
        this.agentsLocation = Maps.newHashMap();
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Registry");
        sd.setName("LocationRegistry");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
            log.error("Could not register agent. Agent terminating");
            doDelete();
        }

        addBehaviour(new AgentsLocationServiceBehaviour());
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Returns Agents Location information without information about Agent it ask for.
     * Agent doesn't need information about its  own location.
     *
     * @param aid
     * @return
     */
    public Map<AID, PhisicalDeviceLocation> getAgentsLocationWithout(AID aid) {
        Map<AID, PhisicalDeviceLocation> agentsLocation = new HashMap<>();
        agentsLocation.putAll(this.agentsLocation);
        agentsLocation.remove(aid);

        return agentsLocation;
    }

    public void updateAgentLocation(AID aid, PhisicalDeviceLocation location) {
        agentsLocation.put(aid, location);
    }


}
