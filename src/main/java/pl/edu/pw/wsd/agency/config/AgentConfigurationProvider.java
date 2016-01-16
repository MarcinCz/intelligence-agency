package pl.edu.pw.wsd.agency.config;

import org.apache.commons.configuration.ConfigurationException;

/**
 * Interface for a class providing specific agent configurations
 * @author marcin.czerwinski
 *
 */
public interface AgentConfigurationProvider {
	
	BaseAgentConfiguration getBaseAgentConfiguration(String propertiesFileName) throws ConfigurationException;
	MovingAgentConfiguration getMovingAgentConfiguration(String propertiesFileName) throws ConfigurationException;
	SupervisorConfiguration geSupervisorAgentConfiguration(String propertiesFileName) throws ConfigurationException;
	TransmitterAgentConfiguration getTransmitterAgentConfiguration(String propertiesFileName) throws ConfigurationException;
	ClientAgentConfiguration getClientAgentConfiguration(String propertiesFileName) throws ConfigurationException;
}
