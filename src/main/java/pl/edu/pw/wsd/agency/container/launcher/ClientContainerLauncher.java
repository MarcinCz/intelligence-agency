package pl.edu.pw.wsd.agency.container.launcher;

import pl.edu.pw.wsd.agency.container.ClientContainer;

public class ClientContainerLauncher extends ContainerLauncher {

	public static void main(String[] args) {
		runContainer(new ClientContainer());
	}
}
