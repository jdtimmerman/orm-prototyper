package nl.ru.jtimmerm.orm.types;

import java.awt.Point;

/**
 * An abstract class to provide positioning functions for types
 * 
 * @author joost
 * 
 */
public abstract class Type implements IType {

	private static final long serialVersionUID = -1868271697434878966L;

	// ////////////////////////////////////////////////////////////////////////
	// GRID
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Holds the position in the grid
	 */
	private Point mGridPosition = new Point(0, 0);

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.orm.types.IType#setGridPosition(int, int)
	 */
	@Override
	public void setGridPosition(int column, int row) {
		mGridPosition = new Point(column, row);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.orm.types.IType#setGridPosition(java.awt.Point)
	 */
	@Override
	public void setGridPosition(Point point) {
		mGridPosition = point;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.orm.types.IType#getGridPosition()
	 */
	public Point getGridPosition() {
		return mGridPosition;
	};

}
