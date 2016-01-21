package pl.edu.pw.wsd.agency.agent.behaviour.physical;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.pw.wsd.agency.agent.LocationRegistryAgent;
import pl.edu.pw.wsd.agency.agent.PhysicalAgent;
import pl.edu.pw.wsd.agency.agent.ViewAgent;
import pl.edu.pw.wsd.agency.config.Configuration;
import pl.edu.pw.wsd.agency.location.message.content.LocationRegistryData;
import pl.edu.pw.wsd.agency.location.ViewEntity;

/**
 * Simulates Agents moving.
 *
 * @author Adrian Sidor
 * @author Adam Papros
 */
public class PhysicalAgentBehaviour extends TickerBehaviour {

	@Getter
	private final boolean isClient;

	@Getter
	private PhysicalAgent physicalAgent;

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
		updatePosition();
		sendInfoToLocationRegistry();

		sendInfoToEntityLocationRegistry();
		log.trace("Agent moved:" + physicalAgent.getLocation());
		log.trace("Agent target: " + physicalAgent.getCurrentTarget());
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
	 */
	private void sendInfoToLocationRegistry() {
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(LocationRegistryAgent.SERVICE_TYPE);
		sd.setName(LocationRegistryAgent.SERVICE_NAME);
		template.addServices(sd);
		AID locationRegistry = null;
		try {
			DFAgentDescription[] result = DFService.search(myAgent, template);
			if (result.length == 1) {
				locationRegistry = result[0].getName();
			}
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		if (locationRegistry != null) {
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.setConversationId(LocationRegistryAgent.LOCATION_CONVERSATION_ID);
			LocationRegistryData location = physicalAgent.getLocation();
			ObjectMapper mapper = Configuration.getInstance().getObjectMapper();
			String content;
			try {
				content = mapper.writeValueAsString(location);
				msg.setContent(content);
				msg.addReceiver(locationRegistry);
				physicalAgent.send(msg);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

		}
	}

	private void sendInfoToEntityLocationRegistry() {
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(ViewAgent.SERVICE_TYPE);
		sd.setName(ViewAgent.SERVICE_NAME);
		template.addServices(sd);
		AID locationRegistry = null;
		try {
			DFAgentDescription[] result = DFService.search(myAgent, template);
			if (result.length == 1) {
				locationRegistry = result[0].getName();
			}
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		if (locationRegistry != null) {
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.setConversationId(ViewAgent.CONVERSATION_ID);

			// create viewEntity
			ViewEntity viewEntity = new ViewEntity(physicalAgent.getLocation());
			viewEntity.setMessageIdList(physicalAgent.getStoredMessageId());

			ObjectMapper mapper = Configuration.getInstance().getObjectMapper();
			String content;
			try {
				content = mapper.writeValueAsString(viewEntity);
				msg.setContent(content);
				msg.addReceiver(locationRegistry);
				physicalAgent.send(msg);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}


}
