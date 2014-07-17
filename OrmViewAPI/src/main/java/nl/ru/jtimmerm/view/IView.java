package nl.ru.jtimmerm.view;

import javax.swing.JOptionPane;

import nl.ru.jtimmerm.orm.IModelListener;
import nl.ru.jtimmerm.view.graph.IGraph;

/**
 * The view build the interface an ties the various pieces together
 * 
 * @author joost
 * 
 */
public interface IView extends IViewObservable, IModelListener, ISelectListener {

	// ////////////////////////////////////////////////////////////////////////
	// CREATING PANELS
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * @return The component that holds a list of objects
	 */
	IComponentContainer getListPanel();

	/**
	 * @return The component that holds a list of objects
	 */
	IComponentContainer getGraphPanel();

	/**
	 * @return The component that holds the input (dialog panel)
	 */
	IComponentContainer getInputPanel();

	/**
	 * @return Get the graph
	 */
	IGraph getGraph();

	// ////////////////////////////////////////////////////////////////////////
	// CREATING PANELS
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Show an exception dialog
	 * 
	 * @param title
	 *            The title of the message box
	 * @param text
	 *            The content of the message
	 * @param messageType
	 *            The type of the message box ({@link JOptionPane} static
	 *            values)
	 */
	void showError(String title, String text, int messageType);

}
