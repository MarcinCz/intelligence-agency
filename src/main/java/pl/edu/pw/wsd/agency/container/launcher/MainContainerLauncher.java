package pl.edu.pw.wsd.agency.container.launcher;

import pl.edu.pw.wsd.agency.container.MainContainer;

/**
 * Launches jade main container
 * @author marcin.czerwinski
 *
 */
public class MainContainerLauncher extends ContainerLauncher {
	
	public static void main(String[] args) {
		runContainer(new MainContainer());
	}
}
