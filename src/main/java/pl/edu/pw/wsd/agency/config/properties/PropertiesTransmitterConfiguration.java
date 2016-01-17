package pl.edu.pw.wsd.agency.config.properties;

import pl.edu.pw.wsd.agency.config.TransmitterConfiguration;

public class PropertiesTransmitterConfiguration extends PropertiesMovingAgentConfiguration implements TransmitterConfiguration {

	private static final String KEY_CREATE_STATUS_PERIOD = "period.create.status";
	private static final String KEY_PROPAGATE_STATUS_PERIOD = "period.propagate.statuses";

	public PropertiesTransmitterConfiguration(String propertiesFileName) {
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
