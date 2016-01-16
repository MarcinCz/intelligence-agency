package pl.edu.pw.wsd.agency.agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import jade.lang.acl.ACLMessage;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.pw.wsd.agency.agent.behaviour.PhysicalAgentBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.ReceiveAgentsLocationBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.RequestAgentsLocationBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.TransmitterReceiveMessageBehaviour;
import pl.edu.pw.wsd.agency.location.MessageId;
import pl.edu.pw.wsd.agency.message.content.ClientMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class TransmitterAgent extends PhysicalAgent {

    private static final long serialVersionUID = 4131616609061841238L;

    private static final Logger log = LogManager.getLogger();

    private List<ACLMessage> clientMessages = new ArrayList<>();

    private List<ACLMessage> agentStatusMessages = new ArrayList<>();

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public Set<MessageId> getStoredMessageId() {
        Set<MessageId> tmpSet = new HashSet<>();
        for (ACLMessage clientMessage : clientMessages) {
            // FIXME :: OMG
            String content = clientMessage.getContent();
            try {
                ClientMessage clientMessage1 = mapper.readValue(content, ClientMessage.class);
                MessageId messageId = clientMessage1.getMessageId();
                tmpSet.add(messageId);
            } catch (IOException e) {
                log.error("Could not read JSON=" + content, e);
                e.printStackTrace();
            }
        }
        return tmpSet;
    }

    public TransmitterAgent(String propertiesFileName) {
        super(propertiesFileName);

    }

    @Override
    protected void setup() {
        super.setup();
        addBehaviour(new PhysicalAgentBehaviour(this, moveBehaviourPeriod, true));
        addBehaviour(new TransmitterReceiveMessageBehaviour(this));
        addBehaviour(new ReceiveAgentsLocationBehaviour(this));
        addBehaviour(new RequestAgentsLocationBehaviour(this, moveBehaviourPeriod));

    }

    public void addClientMessage(ACLMessage cm) {
        clientMessages.add(cm);
    }

    public void addAgentStatusMessage(ACLMessage msg) {
        agentStatusMessages.add(msg);
    }

}
