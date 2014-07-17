package nl.ru.jtimmerm.view;

/**
 * Thrown when a method does not handle a certain instance
 * 
 * @author joost
 * 
 */
public class NotHandledException extends Exception {

	private static final long serialVersionUID = 3444938618845819472L;

	public NotHandledException() {
		super();
	}

	public NotHandledException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotHandledException(Throwable cause) {
		super(cause);
	}

	public NotHandledException(String string) {
		super(string);
	}

}
