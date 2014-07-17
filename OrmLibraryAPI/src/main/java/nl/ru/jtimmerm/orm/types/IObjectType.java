package nl.ru.jtimmerm.orm.types;

import java.util.Collection;
import java.util.Set;

/**
 * @author joost
 */
public interface IObjectType extends IType {

	// ////////////////////////////////////////////////////////////////////////
	// CONTENT
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Get the name of this objecttype
	 * 
	 * @return
	 */
	String getContent();

	/**
	 * Set the name of this objecttype
	 * 
	 * @return
	 */
	void setContent(String content);

	// ////////////////////////////////////////////////////////////////////////
	// SAMPLE DATA
	// ////////////////////////////////////////////////////////////////////////
	/**
	 * Get the sample data provided for this object type
	 * 
	 * @return
	 */
	Set<String> getSamples();

	/**
	 * Add a sample value
	 * 
	 * @param string
	 */
	void addSample(String string);
	
	/**
	 * Add several samples
	 * @param strings
	 */
	void addSamples(String...strings);

	/**
	 * Set a collection of sample data
	 * 
	 * @param samples
	 */
	void setSamples(Collection<String> samples);

	// ////////////////////////////////////////////////////////////////////////
	// VALUETYPE
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Sets a flag whether this objecttype is considered a valuetype
	 * 
	 * @param isValueType
	 */
	void setValueType(boolean isValueType);

	/**
	 * Returns whether this objecttype is considered a valuetype
	 * 
	 * @return
	 */
	boolean isValueType();
}
