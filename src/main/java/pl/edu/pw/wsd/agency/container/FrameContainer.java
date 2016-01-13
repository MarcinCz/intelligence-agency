package pl.edu.pw.wsd.agency.container;

import jade.core.Agent;
import pl.edu.pw.wsd.agency.agent.EntityLocationAgent;
import pl.edu.pw.wsd.agency.container.launcher.RunnableContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:adam.papros@gmail.com">Adam Papros</a>
 */
public class FrameContainer extends RunnableContainer {

    @Override
    public List<Agent> getAgentsToRun() {
        List<Agent> list = new ArrayList<>();
        list.add(new EntityLocationAgent());
        return list;
    }
}
