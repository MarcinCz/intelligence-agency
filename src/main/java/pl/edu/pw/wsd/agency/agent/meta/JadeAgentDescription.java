package pl.edu.pw.wsd.agency.agent.meta;

/**
 * Jade agent description.
 * Contains all the information which are needed to specify agent as {@link jade.Boot} argument.
 * @author marcin.czerwinski
 *
 */
public class JadeAgentDescription {
	private final String localName;
	private final String qualifiedName;
	private final String propertiesFileName;
	
	public JadeAgentDescription(String localName, String qualifiedName, String propertiesFileName) {
		this.localName = localName;
		this.qualifiedName = qualifiedName;
		this.propertiesFileName = propertiesFileName;
	}
	
	public String getLocalName() {
		return localName;
	}

	public String getQualifiedName() {
		return qualifiedName;
	}
	
	public String getPropertiesFileName() {
	    return propertiesFileName;
	}
	
	@Override
	public String toString() {
		return localName + ";" + qualifiedName;
	}
}
