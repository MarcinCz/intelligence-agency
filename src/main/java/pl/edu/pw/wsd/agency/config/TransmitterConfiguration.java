package pl.edu.pw.wsd.agency.config;

public interface TransmitterConfiguration extends MovingAgentConfiguration {

	int getCreateNewStatusPeriod();
	int getPropagateStatusesPeriod();
}
