package pl.edu.pw.wsd.agency.config;

import org.apache.commons.configuration.ConfigurationException;

public class ClientAgentConfigurationImpl extends MovingAgentConfigurationImpl implements ClientAgentConfiguration {

	private static final String KEY_CREATE_STATUS_PERIOD = "period.create.status";

	public ClientAgentConfigurationImpl(String propertiesFileName) throws ConfigurationException {
		super(propertiesFileName);
	}
	
	@Override
	public int getCreateNewStatusPeriod() {
		return cfg.getInt(KEY_CREATE_STATUS_PERIOD);
	}

}
