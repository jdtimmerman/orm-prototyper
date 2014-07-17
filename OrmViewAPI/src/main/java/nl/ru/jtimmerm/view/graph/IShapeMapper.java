package nl.ru.jtimmerm.view.graph;

import java.awt.Point;
import java.util.Collection;

import nl.ru.jtimmerm.orm.types.IType;
import nl.ru.jtimmerm.view.NotHandledException;
import nl.ru.jtimmerm.view.graph.shapes.IShapeElement;

/**
 * @author joost
 */
public interface IShapeMapper<S extends IShapeElement<? extends IType>> {

	// ////////////////////////////////////////////////////////////////////////
	// SETTING
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Replaces all types with the given types. Reuses shapes where possible
	 * 
	 * @param types
	 *            A complete collection of types to be displayed
	 * @throws NotHandledException
	 */
	void setTypes(Collection<? extends IType> types) throws NotHandledException;

	// ////////////////////////////////////////////////////////////////////////
	// ADDING
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Create a shape (of type S) and mapping for the given type
	 * 
	 * @param type
	 * @throws NotHandledException
	 */
	void addType(IType type) throws NotHandledException;

	/**
	 * Create a shape (of type S) and mapping for the given types
	 * 
	 * @param types
	 * @throws NotHandledException
	 */
	void addTypes(Collection<? extends IType> types) throws NotHandledException;

	// ////////////////////////////////////////////////////////////////////////
	// INFORMATION RETRIEVAL
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Get the mapped shape for the given type
	 * 
	 * @param type
	 * @return
	 * @throws NotHandledException
	 */
	S getShapeFor(IType type) throws NotHandledException;

	/**
	 * Get all the stored shapes
	 * 
	 * @return
	 */
	Collection<S> getShapes();

	/**
	 * Get the shape at the given XY point
	 * 
	 * @param p
	 * @return
	 */
	S getShapeAtPoint(Point p);

	/**
	 * Check whether the type is already mapped
	 * 
	 * @param type
	 * @return
	 */
	boolean hasMapped(IType type);

	/**
	 * Check whether the shape is already mapped
	 * 
	 * @param type
	 * @return
	 */
	boolean hasMapped(IShapeElement<? extends IType> type);

	// ////////////////////////////////////////////////////////////////////////
	// CREATE SHAPES
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Implementation specific creation of shape elements
	 * 
	 * @param type
	 * @return
	 * @throws NotHandledException
	 */
	S createShape(IType type) throws NotHandledException;

	/**
	 * Indicates whether the implementation handles this kind of object
	 * 
	 * @param t
	 * @return
	 */
	boolean handles(IType t);
}
