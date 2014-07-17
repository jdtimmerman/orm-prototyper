package nl.ru.jtimmerm.view;

/**
 * Provides methods to add and remove view listeners
 * 
 * @author joost
 * 
 */
public interface IViewObservable {

	/**
	 * Set a view listener on this object
	 * 
	 * @param vl
	 *            The viewlistener
	 */
	void setViewListener(IViewListener vl);

	/**
	 * Remove the view listener
	 */
	void removeViewListener();
}
