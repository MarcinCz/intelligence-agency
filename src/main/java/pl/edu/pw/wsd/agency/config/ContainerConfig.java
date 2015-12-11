package pl.edu.pw.wsd.agency.config;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * Configuration specific to agent containers
 * @author marcin.czerwinski
 *
 */
public class ContainerConfig {
	
	private static final Configuration CFG;
	private static final String KEY_MAIN_CONTAINER_HOST = "container.main.host";
	private static final String KEY_MAIN_CONTAINER_PORT = "container.main.port";
	
	static {
		try {
			CFG = new PropertiesConfiguration("container.properties");
		} catch (ConfigurationException e) {
			throw new IllegalStateException("Could not creat container configuration", e);
		}
	}

	public static String getMainContainerHost() {
		return CFG.getString(KEY_MAIN_CONTAINER_HOST);
	}
	
	public static String getMainContainerPort() {
		return CFG.getString(KEY_MAIN_CONTAINER_PORT);
	}
}
