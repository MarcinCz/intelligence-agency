package pl.edu.pw.wsd.agency.config;

public interface ClientAgentConfiguration extends MovingAgentConfiguration {

	int getCreateNewStatusPeriod();

	long getRequestCertificatesPeriod();
}
