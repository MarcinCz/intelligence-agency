package pl.edu.pw.wsd.agency.config;

import java.util.List;

import org.apache.commons.configuration.ConfigurationException;

import javafx.geometry.Point2D;

public class MovingAgentConfigurationImpl extends BaseAgentConfigurationImpl implements MovingAgentConfiguration {

	private static final String KEY_PATH = "path";
	private static final String KEY_SPEED = "speed";
	private static final String KEY_AGENT_DIRECTION = "ad";
	private static final String KEY_MOVE_BEHAVIOUR_PERIOD = "mbp";
	private static final String KEY_STARTING_POSITION_INDEX = "spi";

	private static final String COORDINATE_SEPARATOR = ";";

	public MovingAgentConfigurationImpl(String propertiesFileName) throws ConfigurationException {
		super(propertiesFileName);
	}
	
	@Override
	public Point2D[] getPath() {
		List<Object> points = cfg.getList(KEY_PATH);
		Point2D[] path = new Point2D[points.size()];
		for (int i = 0; i < points.size(); i++) {
			String s = (String) points.get(i);
			String[] s2 = s.split(COORDINATE_SEPARATOR);
			Point2D _point = new Point2D(Double.valueOf(s2[0]), Double.valueOf(s2[1]));
			path[i] = _point;
		}
		return path;
	}

	@Override
	public double getSpeed() {
		return cfg.getDouble(KEY_SPEED);
	}

	@Override
	public boolean getAgentDirection() {
		return cfg.getBoolean(KEY_AGENT_DIRECTION);
	}

	@Override
	public int getMoveBehaviourPeriod() {
		return cfg.getInt(KEY_MOVE_BEHAVIOUR_PERIOD);
	}

	@Override
	public int getStartingPositionIndex() {
		return cfg.getInt(KEY_STARTING_POSITION_INDEX);
	}

	@Override
	public Point2D getStartingPosition() {
		return getPath()[getStartingPositionIndex()];
	}
}
