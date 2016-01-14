package pl.edu.pw.wsd.agency.agent;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import javafx.geometry.Point2D;
import pl.edu.pw.wsd.agency.agent.behaviour.AgentsLocationServiceBehaviour;

public class LocationRegistryAgent extends Agent {

    private static final long serialVersionUID = 6818961843348892572L;

    private static final Logger log = LogManager.getLogger();

    private Map<AID, Point2D> agentsLocation;

    @Override
    protected void setup() {
        setAgentsLocation(new HashMap<AID, Point2D>());
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

    public Map<AID, Point2D> getAgentsLocation() {
        return agentsLocation;
    }

    /**
     * Returns Agents Location information without information about Agent it ask for.
     * Agent doesn't need information about its  own location.
     *
     * @param aid
     * @return
     */
    public HashMap<AID, java.awt.geom.Point2D> getAgentsLocationWithout(AID aid) {
    	HashMap<AID, java.awt.geom.Point2D> agentsLocation = new HashMap<>();
    	for (Entry<AID, Point2D> location : getAgentsLocation().entrySet()) {
			Point2D value = location.getValue();
			agentsLocation.put(location.getKey(), new java.awt.geom.Point2D.Double(value.getX(), value.getY()));
		}
        agentsLocation.remove(aid);

        return agentsLocation;
    }

    public void setAgentsLocation(Map<AID, Point2D> agentsLocation) {
        this.agentsLocation = agentsLocation;
    }

    public void updateAgentLocation(AID aid, Point2D location) {
        agentsLocation.put(aid, location);
    }


}
