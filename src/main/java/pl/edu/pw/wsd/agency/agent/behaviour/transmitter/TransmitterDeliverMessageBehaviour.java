package pl.edu.pw.wsd.agency.agent.behaviour.transmitter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.pw.wsd.agency.agent.TransmitterAgent;
import pl.edu.pw.wsd.agency.common.PhysicalAgentId;
import pl.edu.pw.wsd.agency.config.Configuration;
import pl.edu.pw.wsd.agency.message.content.ClientMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author apapros
 */
public class TransmitterDeliverMessageBehaviour extends TickerBehaviour {

	private static final long serialVersionUID = -4865095272921712993L;

	private static final Logger log = LogManager.getLogger();

	private TransmitterAgent transmitterAgent;

	private final ObjectMapper objectMapper = Configuration.getInstance().getObjectMapper();

	public TransmitterDeliverMessageBehaviour(TransmitterAgent transmitterAgent, long period) {
		super(transmitterAgent, period);
		this.transmitterAgent = transmitterAgent;
	}

	@Override
	public void onTick() {
		// get clients
		Set<PhysicalAgentId> clientsInRange = transmitterAgent.getClientsInRange();

		Map<String, AID> clientsMap = clientsInRange.stream().collect(Collectors.toMap(PhysicalAgentId::getLocalName, PhysicalAgentId::toAID));

		// get if there is message for them
		Map<ACLMessage, Set<PhysicalAgentId>> clientMessages = transmitterAgent.getClientMessages();

		Map<AID, List<ACLMessage>> messagesThatCanBeSent = new HashMap<>();

		clientMessages.entrySet().stream().forEach(aclMessageSetEntry -> {
			try {
				ClientMessage clientMessage = objectMapper.readValue(aclMessageSetEntry.getKey().getContent(), ClientMessage.class);

				// get these messages that can be send now
				if (clientsMap.containsKey(clientMessage.getEndClient())) {
					AID aid = clientsMap.get(clientMessage.getEndClient());
					if (messagesThatCanBeSent.containsKey(aid)) {
						// add new element to existing list
						List<ACLMessage> msgs = messagesThatCanBeSent.get(aid);
						msgs.add(aclMessageSetEntry.getKey());
					} else {
						// add new list with one element
						messagesThatCanBeSent.put(aid, Lists.newArrayList(aclMessageSetEntry.getKey()));
					}
				}

			} catch (IOException e) {
				log.error("Error occurred", e);
				e.printStackTrace();

			}
		});

		messagesThatCanBeSent.forEach((aid, aclMessages) -> {
			for (ACLMessage msg : aclMessages) {
				msg.clearAllReceiver();
				msg.addReceiver(aid);
				// FIXME :: stats?
				transmitterAgent.send(msg);
			}
		});

	}

}
