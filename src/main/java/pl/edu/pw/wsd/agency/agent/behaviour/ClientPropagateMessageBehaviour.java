package pl.edu.pw.wsd.agency.agent.behaviour;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.pw.wsd.agency.agent.ClientAgent;
import pl.edu.pw.wsd.agency.common.TransmitterId;
import pl.edu.pw.wsd.agency.config.Configuration;
import pl.edu.pw.wsd.agency.message.content.ClientMessage;
import pl.edu.pw.wsd.agency.message.envelope.ConversationId;
import pl.edu.pw.wsd.agency.message.envelope.Language;

import java.util.List;

public class ClientPropagateMessageBehaviour extends TickerBehaviour {

    private static final long serialVersionUID = -4865095272921712993L;

    private static final Logger log = LogManager.getLogger();

    private ClientAgent clientAgent;

    public ClientPropagateMessageBehaviour(ClientAgent clientAgent, long period) {
        super(clientAgent, period);
        this.clientAgent = clientAgent;
    }

    @Override
    public void onTick() {
        List<TransmitterId> transmitters = clientAgent.getAgentsInRange();

        for (TransmitterId transmitterId : transmitters) {
            List<ClientMessage> messages = clientAgent.getClientMessages();
            if (!messages.isEmpty()) {
                // TODO :: jedna czy wszystkie?
                ClientMessage message = messages.remove(0);
                ObjectMapper mapper = Configuration.getInstance().getObjectMapper();
                try {
                    String content = mapper.writeValueAsString(message);
                    ACLMessage msg = new ACLMessage(ACLMessage.PROPAGATE);
                    msg.addReceiver(transmitterId.toAID());
                    msg.setContent(content);
                    msg.setLanguage(Language.JSON);
                    msg.setConversationId(ConversationId.CLIENT_MESSAGE.generateId());
                    clientAgent.sendAndUpdateStatistics(msg);

                    log.info("Wyslalem wiadomosc do Transmitera,{}", transmitterId);
                } catch (JsonProcessingException e) {
                    log.error("Could not parse ClientMessage");
                    // we lost message this way because we removed it from the list
                    // we can add it again agent.queueClientMessage(message)
                    // but do we want message that we cant send?
                }
            }
        }
    }

}
