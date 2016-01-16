package pl.edu.pw.wsd.agency.agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import jade.lang.acl.ACLMessage;
import lombok.Getter;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.pw.wsd.agency.agent.behaviour.PhysicalAgentBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.ReceiveAgentsLocationBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.RequestAgentsLocationBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.TransmitterCreateStatusBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.TransmitterPropagateAgentStatusBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.TransmitterReceiveAgentStatusesRequestBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.TransmitterReceiveMessageBehaviour;
import pl.edu.pw.wsd.agency.config.TransmitterAgentConfiguration;
import pl.edu.pw.wsd.agency.location.MessageId;
import pl.edu.pw.wsd.agency.message.content.ClientMessage;
import pl.edu.pw.wsd.agency.message.propagate.AgentStatusMessageQueue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class TransmitterAgent extends PhysicalAgent {

    private static final long serialVersionUID = 4131616609061841238L;

    private static final Logger log = LogManager.getLogger();

    private int createStatusPeriod;
    private int propagateStatusPeriod;

    private List<ACLMessage> clientMessages = new ArrayList<>();

    private AgentStatusMessageQueue agentStatusQueue = new AgentStatusMessageQueue();

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

        addStatusesBehaviours();
    }

    private void addStatusesBehaviours() {
        addBehaviour(new TransmitterPropagateAgentStatusBehaviour(this, propagateStatusPeriod));
        addBehaviour(new TransmitterCreateStatusBehaviour(this, createStatusPeriod));
        addBehaviour(new TransmitterReceiveAgentStatusesRequestBehaviour(this));
    }

    @Override
    protected void loadConfiguration(String propertiesFileName) throws ConfigurationException {
        super.loadConfiguration(propertiesFileName);
        TransmitterAgentConfiguration cfg = configProvider.getTransmitterAgentConfiguration(propertiesFileName);
        createStatusPeriod = cfg.getCreateNewStatusPeriod();
        propagateStatusPeriod = cfg.getPropagateStatusesPeriod();
    }

    public void addClientMessage(ACLMessage cm) {
        clientMessages.add(cm);
    }

    public void addAgentStatusMessage(ACLMessage msg) {
        agentStatusQueue.queueMessage(msg);
    }

    public AgentStatusMessageQueue getAgentStatusQueue() {
        return agentStatusQueue;
    }
}
