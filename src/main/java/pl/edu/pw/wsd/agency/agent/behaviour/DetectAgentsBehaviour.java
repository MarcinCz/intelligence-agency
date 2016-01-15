package pl.edu.pw.wsd.agency.agent.behaviour;

import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.pw.wsd.agency.agent.MovingAgent;
import pl.edu.pw.wsd.agency.config.Configuration;
import pl.edu.pw.wsd.agency.location.PhisicalDeviceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Detects Agents that are in range.
 *
 * @author Adrian Sidor
 */
public class DetectAgentsBehaviour extends TickerBehaviour {

    private MovingAgent movingAgent;

    public DetectAgentsBehaviour(MovingAgent movingAgent, long period) {
        super(movingAgent, period);
        this.movingAgent = movingAgent;
    }

    private static final long serialVersionUID = 4463025011000946515L;

    private static final Logger log = LogManager.getLogger();

    @Override
    public void onTick() {
        Configuration conf = Configuration.getInstance();
        // FIXME tutaj nie Point2D tylko cos wiece
        Map<AID, PhisicalDeviceLocation> al = conf.getAgentsLocationWithout(movingAgent.getAID());
        List<AID> agentsInRange = new ArrayList<>();
        for (Entry<AID, PhisicalDeviceLocation> entry : al.entrySet()) {
            PhisicalDeviceLocation location = entry.getValue();
            if (amIInArange(location)) {
                agentsInRange.add(entry.getKey());
            }
        }
        movingAgent.setAgentsInRange(agentsInRange);
        log.debug("Agents in range: " + movingAgent.getAgentsInRange());
    }

    /**
     * Checks if two points are in range.
     *
     * @param other
     * @return
     */
    // FIXME DUPLIKAT ReceiveAgentsLocationBehaviour#amIInArange
    private boolean amIInArange(PhisicalDeviceLocation other) {
        double distance = movingAgent.getPosition().distance(other);

        // take transmiter shit
        return distance <= other.getSignalRange();
    }

}
