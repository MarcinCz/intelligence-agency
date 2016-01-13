package pl.edu.pw.wsd.agency.container.launcher;

import pl.edu.pw.wsd.agency.container.TransmitterContainer;

public class TransmitterContainerLauncher {

	public static void main(String[] args) {
		ContainerLauncher.runRemoteContainer(new TransmitterContainer());
	}
}
