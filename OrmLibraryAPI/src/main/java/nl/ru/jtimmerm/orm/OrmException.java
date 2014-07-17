package nl.ru.jtimmerm.orm;

/**
 * This exception can be thrown when handling ORM model data
 * 
 * @author joost
 * 
 */
public class OrmException extends Exception {

	/**
	 * Constructor
	 * 
	 * @param e
	 *            A description of or the cause of the exception as a string
	 */
	public OrmException(String e) {
		super(e);
	}

	/**
	 * Constructor
	 * 
	 * @param e
	 *            The cause of the exception as an exception
	 */
	public OrmException(Exception e) {
		super(e);
	}

	/**
	 * Constructor
	 * 
	 * @param s
	 *            A description of the exception
	 * @param e
	 *            The cause of the exception
	 */
	public OrmException(String s, Exception e) {
		super(s, e);
	}

	private static final long serialVersionUID = -489849230950507318L;

}
