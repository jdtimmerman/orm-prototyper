package nl.ru.jtimmerm.orm.grammar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This classed is used to pass information from the ANTLR generated parser to
 * the application
 * 
 * @author joost
 * 
 */
public class ParseResult {

	/**
	 * Constructor
	 * 
	 * @param sentence
	 *            The string (including wildcards) to represent the fact type
	 * @param objects
	 *            The names of the object types
	 */
	public ParseResult(String sentence, Collection<String> objects) {
		mSentence = sentence;
		mObjects = new ArrayList<String>(objects);
	}

	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Stores the string that represents the fact type, including wildcards
	 */
	private String mSentence;

	/**
	 * @return the sentence
	 */
	public String getSentence() {
		return mSentence;
	}
	
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Stores the names of the object types in this fact
	 */
	private List<String> mObjects;

	/**
	 * @return the objects
	 */
	public List<String> getObjects() {
		return mObjects;
	}

}