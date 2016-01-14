package pl.edu.pw.wsd.agency.agent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;

import pl.edu.pw.wsd.agency.agent.behaviour.MoveBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.ReceiveAgentsLocationBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.RequestAgentsLocationBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.SupervisorReceiveAgentStatuses;
import pl.edu.pw.wsd.agency.agent.behaviour.SupervisorRequestAgentStatuses;
import pl.edu.pw.wsd.agency.config.SupervisorConfiguration;
import pl.edu.pw.wsd.agency.message.content.AgentStatus;

public class SupervisorAgent extends MovingAgent {

	private static final long serialVersionUID = 8604486033349286351L;
	
	//map of agent statuses, agent id is the key
	private Map<String, AgentStatus> agentStatuses = new HashMap<>();
	private int agentHeartbeatMaxPeriod;
	
	public SupervisorAgent(String propertiesFileName) {
		super(propertiesFileName);
	}
	
	@Override
	protected void setup() {
		super.setup();
		addBehaviour(new ReceiveAgentsLocationBehaviour());
	    addBehaviour(new RequestAgentsLocationBehaviour(null, mbp));
		addBehaviour(new SupervisorReceiveAgentStatuses(this));
		addBehaviour(new SupervisorRequestAgentStatuses(this, 1000));
		addBehaviour(new MoveBehaviour(this, mbp, false));
	}

	@Override
	protected void loadConfiguration(String propertiesFileName) throws ConfigurationException {
		super.loadConfiguration(propertiesFileName);
		SupervisorConfiguration cfg = configProvider.geSupervisorAgentConfiguration(propertiesFileName);
		agentHeartbeatMaxPeriod = cfg.getAgentHeartbeatMaxPeriod();
	}

	public int getAgentHeartbeatMaxPeriod() {
		return agentHeartbeatMaxPeriod;
	}

	public void setAgentHeartbeatMaxPeriod(int agentHeartbeatMaxPeriod) {
		this.agentHeartbeatMaxPeriod = agentHeartbeatMaxPeriod;
	}

	public void updateStatuses(List<AgentStatus> readAgentStatuses) {
		for (AgentStatus agentStatus : readAgentStatuses) {
			if(checkIfNewStatus(agentStatus)) {
				agentStatuses.put(agentStatus.getSenderId(), agentStatus);
			}
		}
	}
	
	public Map<String, AgentStatus> getAgentStatuses() {
		return agentStatuses;
	}

	private boolean checkIfNewStatus(AgentStatus status) {
		AgentStatus fromMap = agentStatuses.get(status.getSenderId());
		if(fromMap != null) {
			return status.getTimestamp().isAfter(fromMap.getTimestamp());
		}
		return true;
	}
}
