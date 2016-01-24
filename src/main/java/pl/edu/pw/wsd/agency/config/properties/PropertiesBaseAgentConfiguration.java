package pl.edu.pw.wsd.agency.config.properties;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class PropertiesBaseAgentConfiguration {

	protected final Configuration cfg;

	private static final String KEY_POSITION = "position";
	
	private static final String POSITION_SEPARATOR = ";";

	public PropertiesBaseAgentConfiguration(String propertiesFileName) {
		try {
			cfg = new PropertiesConfiguration(propertiesFileName);
		} catch (ConfigurationException e) {
			throw new IllegalStateException("Could not load configuration from file [" + propertiesFileName + "]");
		}
	}

}
