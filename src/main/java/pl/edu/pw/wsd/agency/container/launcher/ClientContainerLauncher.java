package pl.edu.pw.wsd.agency.container.launcher;

import pl.edu.pw.wsd.agency.container.ClientContainer;

public class ClientContainerLauncher {

	public static void main(String[] args) {
		ContainerLauncher.runRemoteContainer(new ClientContainer());
	}
}
