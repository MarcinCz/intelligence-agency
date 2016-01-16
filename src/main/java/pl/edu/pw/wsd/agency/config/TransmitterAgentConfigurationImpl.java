package pl.edu.pw.wsd.agency.config;

import org.apache.commons.configuration.ConfigurationException;

public class TransmitterAgentConfigurationImpl extends MovingAgentConfigurationImpl implements TransmitterAgentConfiguration {

	private static final String KEY_CREATE_STATUS_PERIOD = "period.create.status";
	private static final String KEY_PROPAGATE_STATUS_PERIOD = "period.propagate.statuses";

	public TransmitterAgentConfigurationImpl(String propertiesFileName) throws ConfigurationException {
		super(propertiesFileName);
	}
	
	@Override
	public int getCreateNewStatusPeriod() {
		return cfg.getInt(KEY_CREATE_STATUS_PERIOD);
	}

	@Override
	public int getPropagateStatusesPeriod() {
		return cfg.getInt(KEY_PROPAGATE_STATUS_PERIOD);
	}

}
