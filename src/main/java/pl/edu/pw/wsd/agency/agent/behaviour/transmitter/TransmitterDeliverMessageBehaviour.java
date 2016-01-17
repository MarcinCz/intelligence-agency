package pl.edu.pw.wsd.agency.agent.behaviour.transmitter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.pw.wsd.agency.agent.TransmitterAgent;
import pl.edu.pw.wsd.agency.common.TransmitterId;
import pl.edu.pw.wsd.agency.config.Configuration;
import pl.edu.pw.wsd.agency.message.content.ClientMessage;
import pl.edu.pw.wsd.agency.message.envelope.ConversationId;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
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
        MessageTemplate tm = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), MessageTemplate.MatchConversationId(ConversationId.DELIVER_CLIENT_MESSAGE.toString()));
        ACLMessage receivedMessage = transmitterAgent.receive(tm);

        if (receivedMessage != null) {
            AID sender = receivedMessage.getSender();
            Map<ACLMessage, Set<TransmitterId>> clientMessages = transmitterAgent.getClientMessages();
            List<Map.Entry<ACLMessage, Set<TransmitterId>>> collect = clientMessages.entrySet().stream().filter(new Predicate<Map.Entry<ACLMessage, Set<TransmitterId>>>() {
                @Override
                public boolean test(Map.Entry<ACLMessage, Set<TransmitterId>> aclMessageSetEntry) {
                    try {
                        ClientMessage clientMessage = objectMapper.readValue(aclMessageSetEntry.getKey().getContent(), ClientMessage.class);
                        return clientMessage.getEndClient().equals(sender.getLocalName());
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            }).collect(Collectors.toList());

            for (Map.Entry<ACLMessage, Set<TransmitterId>> aclMessageSetEntry : collect) {
                ACLMessage key = aclMessageSetEntry.getKey();
                key.clearAllReceiver();
                key.addReceiver(sender);
                transmitterAgent.send(key);
            }
        } else {
            block();
        }

    }

}
