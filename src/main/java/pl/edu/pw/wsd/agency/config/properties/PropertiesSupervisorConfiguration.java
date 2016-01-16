package pl.edu.pw.wsd.agency.config.properties;

import pl.edu.pw.wsd.agency.config.SupervisorConfiguration;

public class PropertiesSupervisorConfiguration extends PropertiesMovingAgentConfiguration implements SupervisorConfiguration {

	public PropertiesSupervisorConfiguration(String propertiesFileName) {
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
