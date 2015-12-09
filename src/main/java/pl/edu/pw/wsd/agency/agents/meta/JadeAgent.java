package pl.edu.pw.wsd.agency.agents.meta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for classes which are supposed to be Jade Agents.
 * @author marcin.czerwinski
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface JadeAgent {

	/**
	 * Specifies how many instances of the agent we want to create
	 */
	int instances() default 1;
	/**
	 * Agent base local name (shown in Jade GUI)
	 */
	String localName();
}
