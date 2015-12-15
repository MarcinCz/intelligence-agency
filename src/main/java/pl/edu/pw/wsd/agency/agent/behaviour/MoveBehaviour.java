package pl.edu.pw.wsd.agency.agent.behaviour;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pl.edu.pw.wsd.agency.agent.BaseAgent;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

public class MoveBehaviour extends TickerBehaviour {
    private int[] borderx;

    public MoveBehaviour(Agent a, long period, int[] borderx) {
        super(a, period);
        this.borderx = borderx;
    }

    private static final long serialVersionUID = -8221711531932126745L;
    private static final Logger LOGGER = LogManager.getLogger();
    
    @Override
    protected void onTick() {
        BaseAgent agent = (BaseAgent)getAgent();
        agent.setPosx(agent.getPosx()+1);
        LOGGER.info("Agent moved. Current position X:" + agent.getPosx());
        
    }

}
