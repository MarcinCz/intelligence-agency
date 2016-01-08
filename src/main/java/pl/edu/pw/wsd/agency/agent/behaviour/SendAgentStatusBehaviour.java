package pl.edu.pw.wsd.agency.agent.behaviour;

import jade.core.behaviours.TickerBehaviour;
import pl.edu.pw.wsd.agency.agent.MovingAgent;

public class SendAgentStatusBehaviour extends TickerBehaviour {

	private static final long serialVersionUID = -2519821743610253964L;

	public SendAgentStatusBehaviour(MovingAgent a, long period) {
		super(a, period);
	}

	@Override
	protected void onTick() {
		// TODO Auto-generated method stub
		
	}
}
