package nl.ru.jtimmerm.view.graph.shapes;

import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;
import java.util.Set;

import nl.ru.jtimmerm.orm.types.IType;

/**
 * A shape element represents a type in a graph.
 * 
 * @author joost
 * 
 * @param <T>
 */
public interface IShapeElement<T extends IType> extends Serializable {

	/**
	 * @return The type this shape is representing
	 */
	public T getType();

	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Set the grid position of this type
	 * 
	 * @param row
	 *            The row
	 * @param column
	 *            The column
	 */
	public void setGridPosition(int row, int column);

	/**
	 * Set the grid position of this type
	 * 
	 * @param p
	 *            A point in which the x-value represents the column, the
	 *            y-value the row
	 */
	public void setGridPosition(Point p);

	/**
	 * @return The position of this shape in the grid
	 */
	public Point getGridPosition();

	/**
	 * @return The pixel position of the shape in the graph
	 */
	public Point getXYPosition();

	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Lines between shapes can be drawn between ports.
	 * 
	 * @return Get all connection ports for this shape
	 */
	public Set<Point> getPorts();

	/**
	 * Lines between shapes can be drawn between ports.
	 * 
	 * @param relation
	 *            The type this shape has a relation with
	 * @return Get all connection ports to connect this shape to the given
	 *         relation
	 */
	public Set<Point> getPorts(IType relation);

	// ////////////////////////////////////////////////////////////////////////

	/**
	 * @return The color to draw the foreground (lines, text)
	 */
	public Color getFGColor();

	/**
	 * @return The color to draw the background
	 */
	public Color getBGColor();

	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Test whether this shape, in the graph, contains the given point
	 * 
	 * @param p
	 *            A XY-point in the graph
	 * @return Whether this shape exists at the given point
	 */
	public boolean containsPoint(Point p);

	// ////////////////////////////////////////////////////////////////////////

	/**
	 * @return Whether this shape is selected
	 */
	boolean isSelected();

	/**
	 * @return The width of this shape
	 */
	int getWidth();

	/**
	 * @return The height of this shape
	 */
	int getHeight();

}
