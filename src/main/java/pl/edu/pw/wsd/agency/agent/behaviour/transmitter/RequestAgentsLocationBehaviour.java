package pl.edu.pw.wsd.agency.agent.behaviour.transmitter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import pl.edu.pw.wsd.agency.agent.LocationRegistryAgent;
import pl.edu.pw.wsd.agency.agent.PhysicalAgent;

/**
 * Detects Agents that are in range.
 *
 * @author Adrian Sidor
 */
public class RequestAgentsLocationBehaviour extends TickerBehaviour {

	public RequestAgentsLocationBehaviour(PhysicalAgent a, long period) {
		super(a, period);
	}

	private static final long serialVersionUID = -3739242813837955331L;

	private static final Logger log = LogManager.getLogger();

	@Override
	public void onTick() {
		try {
			DFAgentDescription template = LocationRegistryAgent.createDfAgentDescription();

			AID locationRegistry = null;

			DFAgentDescription[] result = DFService.search(myAgent, template);
			if (result.length == 1) {
				locationRegistry = result[0].getName();
			}

			if (locationRegistry != null) {
				ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
				msg.setConversationId(LocationRegistryAgent.LOCATION_CONVERSATION_ID);
				msg.addReceiver(locationRegistry);
				myAgent.send(msg);
			}
		} catch (FIPAException fe) {
			fe.printStackTrace();
			log.error("Could not resolve or send message to LocationRegistry!!!", fe);
		}
	}

}
