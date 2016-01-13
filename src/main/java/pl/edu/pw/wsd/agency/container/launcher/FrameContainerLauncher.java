package pl.edu.pw.wsd.agency.container.launcher;

import pl.edu.pw.wsd.agency.container.FrameContainer;

/**
 * @author <a href="mailto:adam.papros@gmail.com">Adam Papros</a>
 */
public class FrameContainerLauncher {
    public static void main(String[] args) {
        ContainerLauncher.runRemoteContainer(new FrameContainer());
    }

}
