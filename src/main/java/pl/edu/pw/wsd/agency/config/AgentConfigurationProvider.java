package pl.edu.pw.wsd.agency.config;

import org.apache.commons.configuration.ConfigurationException;

public interface AgentConfigurationProvider {
	
	BaseAgentConfiguration getBaseAgentConfiguration(String propertiesFileName) throws ConfigurationException;
	MovingAgentConfiguration getMovingAgentConfiguration(String propertiesFileName) throws ConfigurationException;
}
