package nl.ru.jtimmerm.view;

/**
 * Handles selecting object
 * 
 * @author joost
 * 
 * @param <T>
 *            A Type
 */
public interface ISelection<T> {

	/**
	 * Get the selected shape, or <code>null</code> if it is not set
	 * 
	 * @return The selected shape or <code>null</code>
	 */
	T getSelectedShape();

	/**
	 * @param shape
	 *            The shape
	 * @return Whether the shape is selected
	 */
	boolean isSelected(T shape);

	/**
	 * Indicates that there is a selected shape
	 * 
	 * @return
	 */
	boolean hasSelectedShape();

	/**
	 * Set the selected shape. Set <code>null</code> to deselect
	 * 
	 * @param shape
	 *            The selected shape
	 */
	void select(T shape);

	/**
	 * Add a listener for shape selections
	 * 
	 * @param listener
	 *            The listener to be notified of changes
	 */
	void addSelectListener(ISelectListener listener);

}
