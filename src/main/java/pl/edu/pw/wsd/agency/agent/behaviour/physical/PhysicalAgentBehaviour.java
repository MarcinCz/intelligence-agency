package pl.edu.pw.wsd.agency.agent.behaviour.physical;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;

import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import lombok.Getter;
import pl.edu.pw.wsd.agency.agent.LocationRegistryAgent;
import pl.edu.pw.wsd.agency.agent.PhysicalAgent;
import pl.edu.pw.wsd.agency.agent.ViewAgent;
import pl.edu.pw.wsd.agency.config.Configuration;
import pl.edu.pw.wsd.agency.location.ViewEntity;
import pl.edu.pw.wsd.agency.location.message.content.LocationRegistryData;

/**
 * Simulates Agents moving. Sends location data to LocationRegistry and ViewAgent.
 *
 * @author Adrian Sidor
 * @author Adam Papros
 */
public class PhysicalAgentBehaviour extends TickerBehaviour {

	@Getter
	private final boolean isClient;

	@Getter
	private PhysicalAgent physicalAgent;

	private final ObjectMapper mapper = Configuration.getInstance().getObjectMapper();

	public PhysicalAgentBehaviour(PhysicalAgent physicalAgent, long period, boolean isClient) {
		super(physicalAgent, period);
		Preconditions.checkNotNull(physicalAgent);
		this.physicalAgent = physicalAgent;
		this.isClient = isClient;
	}

	private static final long serialVersionUID = -8221711531932126745L;

	private static final Logger log = LogManager.getLogger();

	@Override
	protected void onTick() {
		// update position
		updatePosition();

		// send data to LocationRegistry
		if(physicalAgent.isSendAgentLocationToRegistry()) {
			AID locationRegistry = findLocationRegistry();
			if (locationRegistry != null) {
				sendInfoToLocationRegistry(locationRegistry);
			}
		}

		// send data to ViewAgent
		AID viewRegistry = findViewRegistry();
		if (viewRegistry != null) {
			sendInfoToEntityLocationRegistry(viewRegistry);
		}

		if (log.isTraceEnabled()) {
			log.trace("Agent moved:" + physicalAgent.getLocation());
			log.trace("Agent target: " + physicalAgent.getCurrentTarget());
		}
	}

	private AID findViewRegistry() {
		DFAgentDescription template = ViewAgent.createDfAgentDescription();
		return findRegistry(template);
	}

	private AID findLocationRegistry() {
		DFAgentDescription template = LocationRegistryAgent.createDfAgentDescription();
		return findRegistry(template);
	}

	private AID findRegistry(DFAgentDescription template) {
		try {
			AID locationRegistry = null;
			DFAgentDescription[] result = DFService.search(myAgent, template);
			if (result.length == 1) {
				locationRegistry = result[0].getName();
			}
			return locationRegistry;

		} catch (FIPAException fe) {
			log.error("LocationRegistry could not be found!!!", fe);
			return null;
		}
	}

	/**
	 * Updates Agents Position.
	 * Agent is moving.
	 */
	private void updatePosition() {
		physicalAgent.updatePosition();
	}

	/**
	 * Agent sends information to LocationRegistry Agent about its new Position.
	 *
	 * @param locationRegistry
	 */
	private void sendInfoToLocationRegistry(AID locationRegistry) {
		try {
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.setConversationId(LocationRegistryAgent.LOCATION_CONVERSATION_ID);
			LocationRegistryData location = physicalAgent.getLocation();

			String content = mapper.writeValueAsString(location);

			msg.setContent(content);
			msg.addReceiver(locationRegistry);
			physicalAgent.send(msg);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			log.error("Could not send location data to LocationRegistry!!!", e);
		}

	}

	private void sendInfoToEntityLocationRegistry(AID viewAgent) {
		try {

			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.setConversationId(ViewAgent.CONVERSATION_ID);

			// create viewEntity
			ViewEntity viewEntity = new ViewEntity(physicalAgent.getLocation());
			viewEntity.setMessageIdList(physicalAgent.getStoredMessageId());

			String content = mapper.writeValueAsString(viewEntity);
			msg.setContent(content);
			msg.addReceiver(viewAgent);
			physicalAgent.send(msg);
		} catch (JsonProcessingException e) {
			log.error("Could not send location data to LocationRegistry!!!", e);
		}

	}
}
