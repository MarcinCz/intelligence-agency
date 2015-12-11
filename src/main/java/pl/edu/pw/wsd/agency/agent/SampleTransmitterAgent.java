package pl.edu.pw.wsd.agency.agent;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jade.core.Agent;

public class SampleTransmitterAgent extends Agent {

	private static final long serialVersionUID = 7620776141345324567L;
	private static final Logger LOGGER = LogManager.getLogger();
	
	@Override
	protected void setup() {
		super.setup();
		LOGGER.info(getLocalName() + " says Hello World");
	}
}
