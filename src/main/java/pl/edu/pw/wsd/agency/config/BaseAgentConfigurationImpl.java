package pl.edu.pw.wsd.agency.config;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import javafx.geometry.Point2D;

public class BaseAgentConfigurationImpl implements BaseAgentConfiguration {

	protected final Configuration cfg;

	private static final String KEY_POSITION = "position";
	
	private static final String POSITION_SEPARATOR = ";";

	public BaseAgentConfigurationImpl(String propertiesFileName) throws ConfigurationException {
		cfg = new PropertiesConfiguration(propertiesFileName);
	}
	@Override
	public Point2D getStartingPosition() {
		String[] position = cfg.getString(KEY_POSITION).split(POSITION_SEPARATOR);
		return new Point2D(Double.valueOf(position[0]), Double.valueOf(position[1]));
	}

}
