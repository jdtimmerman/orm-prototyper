package nl.ru.jtimmerm;

import org.apache.log4j.Logger;

/**
 * A class to exit the application and printing the stacktrace of the cause.
 * Hopefully this class will never used.
 * 
 * @author joost
 * 
 */
public class Error {

	/**
	 * Print a message, stacktrace and exit the application
	 * 
	 * @param message
	 *            A description of the error
	 * @param e
	 *            The cause of the fatal error
	 */
	public static final void fatalError(String message, Throwable e) {
		log.error(message);
		Error.fatalError(e);
	}

	/**
	 * Print a stacktrace and exit the application
	 * 
	 * @param e
	 *            The cause of the fatal error
	 */
	public static final void fatalError(Throwable e) {
		log.error(Lang.text("err_fatal"), e);
		System.exit(-1);
	}

	private final static Logger log = Logger.getLogger(Error.class);
}
