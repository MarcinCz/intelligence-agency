package pl.edu.pw.wsd.agency.config;

import org.apache.commons.configuration.ConfigurationException;

public class SupervisorConfigurationImpl extends MovingAgentConfigurationImpl implements SupervisorConfiguration {

	public SupervisorConfigurationImpl(String propertiesFileName) throws ConfigurationException {
		super(propertiesFileName);
	}

	private static final String KEY_AGENT_HEARTBEAT_MAX_PERIOD = "period.max.agent.heartbeat";
	private static final String KEY_REQUEST_STATUSES_HEARTBEAT = "period.request.statuses";

	@Override
	public int getAgentHeartbeatMaxPeriod() {
		return cfg.getInt(KEY_AGENT_HEARTBEAT_MAX_PERIOD);
	}

	@Override
	public int getRequestStatusesPeriod() {
		return cfg.getInt(KEY_REQUEST_STATUSES_HEARTBEAT);
	}

}
