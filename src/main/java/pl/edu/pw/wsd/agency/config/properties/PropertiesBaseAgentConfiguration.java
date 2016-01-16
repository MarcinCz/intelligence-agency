package pl.edu.pw.wsd.agency.config.properties;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import javafx.geometry.Point2D;
import pl.edu.pw.wsd.agency.config.BaseAgentConfiguration;

public class PropertiesBaseAgentConfiguration implements BaseAgentConfiguration {

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
	
	@Override
	public Point2D getStartingPosition() {
		String[] position = cfg.getString(KEY_POSITION).split(POSITION_SEPARATOR);
		return new Point2D(Double.valueOf(position[0]), Double.valueOf(position[1]));
	}

}
