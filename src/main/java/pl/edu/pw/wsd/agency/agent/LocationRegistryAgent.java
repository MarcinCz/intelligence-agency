package pl.edu.pw.wsd.agency.agent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.pw.wsd.agency.common.PhysicalAgentId;
import pl.edu.pw.wsd.agency.config.Configuration;
import pl.edu.pw.wsd.agency.location.message.content.LocationRegistryData;
import pl.edu.pw.wsd.agency.message.content.AgentsLocationMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LocationRegistryAgent extends Agent {

	public static final String SERVICE_TYPE = "Registry";
	public static final String SERVICE_NAME = "LocationRegistry";

	public static final String LOCATION_CONVERSATION_ID = "Agents-Location";

	private static final long serialVersionUID = 6818961843348892572L;

	private static final Logger log = LogManager.getLogger();

	private Cache<PhysicalAgentId, LocationRegistryData> agentsLocation;

	@Override
	protected void setup() {
		try {
			this.agentsLocation = CacheBuilder.newBuilder().expireAfterAccess(5, TimeUnit.SECONDS).build();
			DFAgentDescription dfd = createDfAgentDescription();
			dfd.setName(getAID());

			DFService.register(this, dfd);
			addBehaviour(new AgentsLocationServiceBehaviour(this));
		} catch (FIPAException e) {
			log.error("Could not register agent. Agent terminating", e);
			doDelete();
		}
	}

	@Override
	protected void takeDown() {
		try {
			DFService.deregister(this);
		} catch (FIPAException e) {
			log.error("Could not deregister LocationRegistry!!!", e);
		}
	}

	public Map<PhysicalAgentId, LocationRegistryData> getAgentsLocationWithout() {
		Map<PhysicalAgentId, LocationRegistryData> agentsLocation = new HashMap<>();
		agentsLocation.putAll(this.agentsLocation.asMap());

		return agentsLocation;
	}

	public void updateAgentLocation(PhysicalAgentId aid, LocationRegistryData location) {
		agentsLocation.put(aid, location);
	}


	public static class AgentsLocationServiceBehaviour extends Behaviour {

		private static final long serialVersionUID = 6320047876997878496L;

		private static final Logger log = LogManager.getLogger();

		private ObjectMapper mapper = Configuration.getInstance().getObjectMapper();

		private final LocationRegistryAgent locationRegistryAgent;

		AgentsLocationServiceBehaviour(LocationRegistryAgent agent) {
			super(agent);
			this.locationRegistryAgent = agent;
		}

		@Override
		public void action() {

			MessageTemplate mt = MessageTemplate.MatchConversationId(LOCATION_CONVERSATION_ID);
			log.trace("Waiting for message");
			ACLMessage msg = myAgent.receive(mt);
			if (msg == null) {
				block();
			} else {

				if (msg.getPerformative() == ACLMessage.REQUEST) {
					// new location request
					try {
						ACLMessage reply = msg.createReply();
						ObjectMapper mapper = Configuration.getInstance().getObjectMapper();
						AgentsLocationMessage alm = new AgentsLocationMessage(locationRegistryAgent.getAgentsLocationWithout());

						String content = mapper.writeValueAsString(alm);
						reply.setContent(content);

						locationRegistryAgent.send(reply);
					} catch (JsonProcessingException e) {
						log.error("Could not process json", e);
					}

				} else if (msg.getPerformative() == ACLMessage.INFORM) {
					// new location data to store!

					String content = msg.getContent();
					try {
						LocationRegistryData position = mapper.readValue(content, LocationRegistryData.class);
						AID sender = msg.getSender();
						locationRegistryAgent.updateAgentLocation(new PhysicalAgentId(sender), position);
					} catch (IOException e) {
						log.error("Could not process json", e);
					}
				}
			}
		}

		@Override
		public boolean done() {
			return false;
		}

	}


	public static DFAgentDescription createDfAgentDescription() {
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(LocationRegistryAgent.SERVICE_TYPE);
		sd.setName(LocationRegistryAgent.SERVICE_NAME);
		template.addServices(sd);
		return template;
	}
}
