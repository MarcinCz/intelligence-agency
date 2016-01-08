package pl.edu.pw.wsd.agency.container.launcher;

import pl.edu.pw.wsd.agency.container.LocationRegistryContainer;

public class LocationRegistryContainerLauncher {

	public static void main(String[] args) {
		ContainerLauncher.runRemoteContainer(new LocationRegistryContainer());
	}
}
