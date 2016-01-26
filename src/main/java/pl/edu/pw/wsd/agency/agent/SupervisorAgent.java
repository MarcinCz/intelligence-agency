package pl.edu.pw.wsd.agency.agent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pl.edu.pw.wsd.agency.agent.behaviour.SupervisorReceiveAgentCertificates;
import pl.edu.pw.wsd.agency.agent.behaviour.SupervisorRequestAgentCertificates;
import pl.edu.pw.wsd.agency.agent.behaviour.supervisor.SupervisorReceiveAgentStatuses;
import pl.edu.pw.wsd.agency.agent.behaviour.supervisor.SupervisorRequestAgentStatuses;
import pl.edu.pw.wsd.agency.agent.behaviour.transmitter.ReceiveAgentsLocationBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.transmitter.RequestAgentsLocationBehaviour;
import pl.edu.pw.wsd.agency.charts.ChartsManager;
import pl.edu.pw.wsd.agency.config.SupervisorConfiguration;
import pl.edu.pw.wsd.agency.location.MessageId;
import pl.edu.pw.wsd.agency.message.content.AgentCertificate;
import pl.edu.pw.wsd.agency.message.content.AgentStatus;

public class SupervisorAgent extends PhysicalAgent {

	private static final long serialVersionUID = 8604486033349286351L;

	//map of agent statuses, agent id is the key
	private Map<String, AgentStatus> agentStatuses = new HashMap<>();
	private int agentHeartbeatMaxPeriod;
	private int requestAgentStatusesPeriod;
	private int requestAgentCertificatesPeriod = 10000;
	private ChartsManager chartsManager = new ChartsManager();

	public SupervisorAgent(SupervisorConfiguration config) {
		super(config, false);
		loadConfiguration(config);
	}

	@Override
	public Set<MessageId> getStoredMessageId() {
		return null;
	}

	@Override
	protected void setup() {
		super.setup();

		setSendAgentLocationToRegistry(false);
		
		addBehaviour(new RequestAgentsLocationBehaviour(this, moveBehaviourPeriod));
		addBehaviour(new ReceiveAgentsLocationBehaviour(this));
		
		addBehaviour(new SupervisorReceiveAgentStatuses(this));
		addBehaviour(new SupervisorRequestAgentStatuses(this, requestAgentStatusesPeriod));
		
		addBehaviour(new SupervisorReceiveAgentCertificates(this));
		addBehaviour(new SupervisorRequestAgentCertificates(this, requestAgentCertificatesPeriod));
	}

	protected void loadConfiguration(SupervisorConfiguration config) {
		super.loadConfiguration(config);
		agentHeartbeatMaxPeriod = config.getAgentHeartbeatMaxPeriod();
		requestAgentStatusesPeriod = config.getRequestStatusesPeriod();
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
				chartsManager.handleNewStatus(agentStatus);
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

	public boolean updateCertificates(List<AgentCertificate> agentCertificates) {
		boolean certificatesChanged = false;
		for (AgentCertificate cert : agentCertificates){
			certificatesChanged = certificatesChanged || getAgentCertificates().add(cert);   
		}
		return certificatesChanged;
	}
}
