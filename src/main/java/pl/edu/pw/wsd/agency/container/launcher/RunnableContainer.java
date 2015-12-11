package pl.edu.pw.wsd.agency.container.launcher;

import pl.edu.pw.wsd.agency.container.BaseContainer;

/**
 * Base class for containers which can be launcher with {@link ContainerLauncher}
 * @author marcin.czerwinski
 *
 */
public abstract class RunnableContainer extends BaseContainer {

	private boolean mainContainer = false;

	public boolean isMainContainer() {
		return mainContainer;
	}
	
	public void setMainContainer(boolean mainContainer) {
		this.mainContainer = mainContainer;
	}
}
