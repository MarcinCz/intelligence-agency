package pl.edu.pw.wsd.agency.agent;

import com.google.common.collect.Sets;
import org.apache.commons.configuration.ConfigurationException;
import pl.edu.pw.wsd.agency.agent.behaviour.PhysicalAgentBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.ReceiveAgentsLocationBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.RequestAgentsLocationBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.SupervisorReceiveAgentStatuses;
import pl.edu.pw.wsd.agency.agent.behaviour.SupervisorRequestAgentStatuses;
import pl.edu.pw.wsd.agency.config.SupervisorConfiguration;
import pl.edu.pw.wsd.agency.location.MessageId;
import pl.edu.pw.wsd.agency.message.content.AgentStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SupervisorAgent extends PhysicalAgent {

    private static final long serialVersionUID = 8604486033349286351L;

    //map of agent statuses, agent id is the key
    private Map<String, AgentStatus> agentStatuses = new HashMap<>();
    private int agentHeartbeatMaxPeriod;
    private int requestAgentStatusesPeriod;

    @Override
    public Set<MessageId> getStoredMessageId() {
        return Sets.newHashSet();
    }

    public SupervisorAgent(String propertiesFileName) {
        super(propertiesFileName);
    }

    @Override
    protected void setup() {
        super.setup();
        addBehaviour(new ReceiveAgentsLocationBehaviour(this));
        addBehaviour(new RequestAgentsLocationBehaviour(null, moveBehaviourPeriod));
        addBehaviour(new SupervisorReceiveAgentStatuses(this));
        addBehaviour(new SupervisorRequestAgentStatuses(this, requestAgentStatusesPeriod));
        addBehaviour(new PhysicalAgentBehaviour(this, moveBehaviourPeriod, false));
    }

    @Override
    protected void loadConfiguration(String propertiesFileName) throws ConfigurationException {
        super.loadConfiguration(propertiesFileName);
        SupervisorConfiguration cfg = configProvider.geSupervisorAgentConfiguration(propertiesFileName);
        agentHeartbeatMaxPeriod = cfg.getAgentHeartbeatMaxPeriod();
        requestAgentStatusesPeriod = cfg.getRequestStatusesPeriod();
    }

    public int getAgentHeartbeatMaxPeriod() {
        return agentHeartbeatMaxPeriod;
    }

    public void setAgentHeartbeatMaxPeriod(int agentHeartbeatMaxPeriod) {
        this.agentHeartbeatMaxPeriod = agentHeartbeatMaxPeriod;
    }

    public void updateStatuses(List<AgentStatus> readAgentStatuses) {
        for (AgentStatus agentStatus : readAgentStatuses) {
            if (checkIfNewStatus(agentStatus)) {
                agentStatuses.put(agentStatus.getSenderId(), agentStatus);
            }
        }
    }

    public Map<String, AgentStatus> getAgentStatuses() {
        return agentStatuses;
    }

    private boolean checkIfNewStatus(AgentStatus status) {
        AgentStatus fromMap = agentStatuses.get(status.getSenderId());
        if (fromMap != null) {
            return status.getTimestamp().isAfter(fromMap.getTimestamp());
        }
        return true;
    }
}
