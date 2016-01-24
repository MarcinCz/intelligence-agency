package pl.edu.pw.wsd.agency.simulation;

import java.util.ArrayList;
import java.util.List;

import jade.core.Agent;
import pl.edu.pw.wsd.agency.agent.LocationRegistryAgent;
import pl.edu.pw.wsd.agency.agent.ViewAgent;
import pl.edu.pw.wsd.agency.container.launcher.ContainerLauncher;
import pl.edu.pw.wsd.agency.container.launcher.RunnableContainer;

public class SystemSimulation {

	public static void main(String[] args) throws InterruptedException {
		SystemSimulation simulation = new SystemSimulation();
		simulation.runSimulation();
	}
	
	private void runSimulation() throws InterruptedException {
		runPlatform(getAgentsToRun());
		wait();
	}

	private List<Agent> getAgentsToRun() {
		List<Agent> agentsToRun = new ArrayList<>();
		agentsToRun.add(new ViewAgent());
		agentsToRun.add(new LocationRegistryAgent());
		agentsToRun.addAll(ClientsToRun.getClientsToRun());
		agentsToRun.addAll(TransmittersToRun.getTransmittersToRun());
		agentsToRun.addAll(SupervisorsToRun.getSupervisorsToRun());
		return agentsToRun;
	}
	
	
	private void runPlatform(List<Agent> agentsToRun) {
        new Thread(() -> {
            ContainerLauncher.runMainContainer(false);
            ContainerLauncher.runRemoteContainer(new RunnableContainer() {

                @Override
                public List<Agent> getAgentsToRun() {
                    return agentsToRun;
                }
            });
        }).start();
    }
}
