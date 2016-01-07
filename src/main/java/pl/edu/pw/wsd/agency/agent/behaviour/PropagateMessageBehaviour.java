package pl.edu.pw.wsd.agency.agent.behaviour;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import pl.edu.pw.wsd.agency.agent.TransmitterAgent;
import pl.edu.pw.wsd.agency.message.content.PropagateMyMessage;

public class PropagateMessageBehaviour extends TickerBehaviour{

    public PropagateMessageBehaviour(Agent a, long period) {
		super(a, period);
	}

	private static final long serialVersionUID = -4865095272921712993L;
    private static final Logger log = LogManager.getLogger();

    @Override
    public void onTick() {
        TransmitterAgent agent = (TransmitterAgent)getAgent();
        List<PropagateMyMessage> propagate = agent.getPropagateMyMessageList();
        if(!agent.getPropagateMyMessageList().isEmpty()) {
            PropagateMyMessage message = propagate.remove(0);
            ObjectMapper mapper = new ObjectMapper();
            try {
                String value = mapper.writeValueAsString(message);
                DFAgentDescription template = new DFAgentDescription();
                ServiceDescription sd = new ServiceDescription();
                sd.setType("transmitter");
                template.addServices(sd);
                DFAgentDescription[] result = DFService.search(agent, template);
                AID[] transmitters = new AID[result.length];
                for(int i = 0; i<result.length; i++) {
                    transmitters[i] = result[i].getName();
                }
                for(AID aid : transmitters) {
                    if(aid != agent.getAID()) {
                        ACLMessage msg = new ACLMessage(ACLMessage.PROPAGATE);
                        msg.addReceiver(aid);
                        msg.setContent(value);
                        agent.send(msg);
                        log.info("Wyslalem wiadomosc: " + value);
                        log.info("Do agenta: " + aid);
                        break;
                    }
                }
            } catch (JsonProcessingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (FIPAException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
    }

}
