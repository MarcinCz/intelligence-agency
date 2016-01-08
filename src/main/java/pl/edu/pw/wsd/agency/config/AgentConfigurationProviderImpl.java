package pl.edu.pw.wsd.agency.config;

import org.apache.commons.configuration.ConfigurationException;

public class AgentConfigurationProviderImpl implements AgentConfigurationProvider {

	@Override
	public MovingAgentConfiguration getMovingAgentConfiguration(String propertiesFileName) throws ConfigurationException {
		return new MovingAgentConfigurationImpl(propertiesFileName);
	}

	@Override
	public BaseAgentConfiguration getBaseAgentConfiguration(String propertiesFileName) throws ConfigurationException {
		return new BaseAgentConfigurationImpl(propertiesFileName);
	}

}
