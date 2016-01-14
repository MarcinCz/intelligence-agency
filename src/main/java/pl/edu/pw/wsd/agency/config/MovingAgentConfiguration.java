package pl.edu.pw.wsd.agency.config;

import javafx.geometry.Point2D;

public interface MovingAgentConfiguration extends BaseAgentConfiguration {

	Point2D[] getPath();
	double getSpeed();
	boolean getAgentDirection();
	int getMoveBehaviourPeriod();
	int getStartingPositionIndex();

	double getSignalRange();
}
