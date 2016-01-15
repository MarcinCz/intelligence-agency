package pl.edu.pw.wsd.agency.config;

public interface TransmitterAgentConfiguration extends MovingAgentConfiguration {

	int getCreateNewStatusPeriod();
	int getPropagateStatusesPeriod();
}
