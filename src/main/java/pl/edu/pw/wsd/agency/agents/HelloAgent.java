package pl.edu.pw.wsd.agency.agents;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jade.core.Agent;
import pl.edu.pw.wsd.agency.agents.meta.JadeAgent;

@JadeAgent(localName="HelloAgent", instances=2)
public class HelloAgent extends Agent {

	private static final long serialVersionUID = 7620776141345324567L;
	private static final Logger LOGGER = LogManager.getLogger();
	
	@Override
	protected void setup() {
		super.setup();
		LOGGER.info(getLocalName() + " says Hello World");
	}
}
