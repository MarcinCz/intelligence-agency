package pl.edu.pw.wsd.agency.config;

import org.apache.commons.configuration.ConfigurationException;

public class SupervisorConfigurationImpl extends MovingAgentConfigurationImpl implements SupervisorConfiguration {

	public SupervisorConfigurationImpl(String propertiesFileName) throws ConfigurationException {
		super(propertiesFileName);
	}

	private static final String KEY_AGENT_HEARTBEAT_MAX_PERIOD = "agent-heartbeat";

	@Override
	public int getAgentHeartbeatMaxPeriod() {
		return cfg.getInt(KEY_AGENT_HEARTBEAT_MAX_PERIOD);
	}

}
