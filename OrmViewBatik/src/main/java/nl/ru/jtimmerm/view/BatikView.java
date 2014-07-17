package nl.ru.jtimmerm.view;

import nl.ru.jtimmerm.orm.IModel;
import nl.ru.jtimmerm.view.graph.BatikGraph;
import nl.ru.jtimmerm.view.graph.IGraph;

/**
 * The BatikView allows the view to use a BatikGraph
 * 
 * @author joost
 * 
 */
public class BatikView extends View {

	/**
	 * Create the view with the given model
	 * 
	 * @param model
	 *            The model
	 */
	public BatikView(IModel model) {
		super(model);
	}

	/**
	 * @return A BatikGraph
	 */
	@Override
	public IGraph getGraphPanel() {
		return new BatikGraph();
	}

}
