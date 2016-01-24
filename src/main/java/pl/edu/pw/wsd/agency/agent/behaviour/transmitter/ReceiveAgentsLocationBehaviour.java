package pl.edu.pw.wsd.agency.agent.behaviour.transmitter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import pl.edu.pw.wsd.agency.agent.LocationRegistryAgent;
import pl.edu.pw.wsd.agency.agent.PhysicalAgent;
import pl.edu.pw.wsd.agency.common.PhysicalAgentId;
import pl.edu.pw.wsd.agency.config.Configuration;
import pl.edu.pw.wsd.agency.location.message.content.LocationRegistryData;
import pl.edu.pw.wsd.agency.message.content.AgentsLocationMessage;

/**
 * Behaviour that receive massage from LocationRegistry Agent about all Agents Positions.
 * Based on that information checks what Agents are in range.
 *
 * @author Adrian Sidor
 */
public class ReceiveAgentsLocationBehaviour extends Behaviour {

	private static final long serialVersionUID = 4463025011000946515L;

	private static final Logger log = LogManager.getLogger();

	private PhysicalAgent physicalAgent;

	public ReceiveAgentsLocationBehaviour(PhysicalAgent physicalAgent) {
		super(physicalAgent);
		this.physicalAgent = physicalAgent;
	}

	@Override
	public void action() {
		MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchReceiver(new AID[]{physicalAgent.getAID()}),
				MessageTemplate.MatchConversationId(LocationRegistryAgent.LOCATION_CONVERSATION_ID));
		ACLMessage msg = physicalAgent.receive(mt);
		if (msg != null) {
			try {
				String content = msg.getContent();
				ObjectMapper mapper = Configuration.getInstance().getObjectMapper();

				AgentsLocationMessage alm = mapper.readValue(content, AgentsLocationMessage.class);
				Map<PhysicalAgentId, LocationRegistryData> al = alm.getAgentsLocation();


				Set<PhysicalAgentId> clients = new HashSet<>();
				Set<PhysicalAgentId> transmitters = new HashSet<>();

				for (Entry<PhysicalAgentId, LocationRegistryData> entry : al.entrySet()) {
					if (entry.getKey().getLocalName().equals(physicalAgent.getLocalName())) {
						continue;
					}
					LocationRegistryData location = entry.getValue();
					if (isInMyRange(location)) {
						if (location.getIsClient()) {
							clients.add(entry.getKey());
						} else {
							transmitters.add(entry.getKey());
						}
					}
				}


				physicalAgent.setTransmittersInRange(transmitters);
				physicalAgent.setClientsInRange(clients);

				if (log.isDebugEnabled()) {
					log.debug(clients.size() + " clients in range: " + clients);
					log.debug(transmitters.size() + " transmitters in range: " + transmitters);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			block();
		}
	}

	@Override
	public boolean done() {
		return false;
	}

	/**
	 * Checks if two points are in range.
	 *
	 * @param location
	 */
	private boolean isInMyRange(LocationRegistryData location) {
		double distance = physicalAgent.getLocation().distance(location);
		return distance <= physicalAgent.getLocation().getSignalRange() + location.getSignalRange();
	}

}
