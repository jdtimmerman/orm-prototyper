package nl.ru.jtimmerm.orm.types;

import java.awt.Point;
import java.io.Serializable;

public interface IType extends Serializable, Comparable<IType> {

	// ////////////////////////////////////////////////////////////////////////
	// REPRESENTATION
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * @return A representation of this type as a string
	 */
	String toString();

	// ////////////////////////////////////////////////////////////////////////
	// COMPARE
	// ////////////////////////////////////////////////////////////////////////

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	int compareTo(IType o);

	/*
	 * 
	 */
	@Override
	boolean equals(Object other);

	/*
	 * 
	 */
	@Override
	int hashCode();

	// ////////////////////////////////////////////////////////////////////////
	// GRID
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Move this type to the given column and row
	 * 
	 * @param column
	 *            The new column
	 * @param row
	 *            The new row
	 */
	void setGridPosition(int column, int row);

	/**
	 * Move this type to the grid position
	 * 
	 * @param point
	 *            A position where <code>x</code> represents a column and
	 *            <code>y</code> a row
	 */
	void setGridPosition(Point point);

	/**
	 * @return The position of this type
	 */
	Point getGridPosition();
}
