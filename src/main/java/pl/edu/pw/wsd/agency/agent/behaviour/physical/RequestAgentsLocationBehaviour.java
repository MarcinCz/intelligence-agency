package pl.edu.pw.wsd.agency.agent.behaviour.physical;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Detects Agents that are in range.
 *
 * @author Adrian Sidor
 */
public class RequestAgentsLocationBehaviour extends TickerBehaviour {

	public RequestAgentsLocationBehaviour(Agent a, long period) {
		super(a, period);
	}

	private static final long serialVersionUID = -3739242813837955331L;

	private static final Logger log = LogManager.getLogger();

	@Override
	public void onTick() {
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Registry");
		sd.setName("LocationRegistry");
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
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			msg.setConversationId("Agents-Location");
			msg.addReceiver(locationRegistry);
			myAgent.send(msg);
		}
	}
}
