package pl.edu.pw.wsd.agency.agents.meta;

/**
 * Jade agent description.
 * Contains all the information which are needed to specify agent as {@link jade.Boot} argument.
 * @author marcin.czerwinski
 *
 */
public class JadeAgentDescription {
	private final String localName;
	private final String qualifiedName;
	
	public JadeAgentDescription(String localName, String qualifiedName) {
		this.localName = localName;
		this.qualifiedName = qualifiedName;
	}
	
	public String getLocalName() {
		return localName;
	}

	public String getQualifiedName() {
		return qualifiedName;
	}
	
	@Override
	public String toString() {
		return localName + ";" + qualifiedName;
	}
}
