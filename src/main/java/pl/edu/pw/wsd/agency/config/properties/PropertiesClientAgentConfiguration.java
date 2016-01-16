package pl.edu.pw.wsd.agency.config.properties;

import pl.edu.pw.wsd.agency.config.ClientAgentConfiguration;

public class PropertiesClientAgentConfiguration extends PropertiesMovingAgentConfiguration implements ClientAgentConfiguration {

	private static final String KEY_CREATE_STATUS_PERIOD = "period.create.status";

	public PropertiesClientAgentConfiguration(String propertiesFileName) {
		super(propertiesFileName);
	}
	
	@Override
	public int getCreateNewStatusPeriod() {
		return cfg.getInt(KEY_CREATE_STATUS_PERIOD);
	}

}
