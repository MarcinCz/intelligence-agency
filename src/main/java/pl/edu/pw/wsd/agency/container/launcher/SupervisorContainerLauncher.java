package pl.edu.pw.wsd.agency.container.launcher;

import pl.edu.pw.wsd.agency.container.SupervisorContainer;

public class SupervisorContainerLauncher {

	public static void main(String[] args) {
		ContainerLauncher.runRemoteContainer(new SupervisorContainer());
	}
}
