package nl.ru.jtimmerm.view.graph;

import nl.ru.jtimmerm.orm.types.IType;
import nl.ru.jtimmerm.view.IComponentContainer;
import nl.ru.jtimmerm.view.graph.shapes.IShapeElement;

/**
 * @author joost
 * 
 */
public interface IGraph extends IComponentContainer {

	IShapeMapper<? extends IShapeElement<? extends IType>> getShapeMapper();

	/**
	 * Refresh when the contents of the model has changed. This cause the
	 * document to be rebuild
	 */
	void refreshGraph();

	/**
	 * Export the current image to a file
	 * 
	 * @throws Exception
	 */
	void exportToFile() throws Exception;
}
