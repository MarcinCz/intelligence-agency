package pl.edu.pw.wsd.agency.container.launcher;

import pl.edu.pw.wsd.agency.container.TransmitterContainer;

public class TransmittalContainerLauncher {

	public static void main(String[] args) {
		ContainerLauncher.runRemoteContainer(new TransmitterContainer());
	}
}
