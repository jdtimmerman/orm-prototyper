package nl.ru.jtimmerm.view.graph;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import nl.ru.jtimmerm.Lang;
import nl.ru.jtimmerm.Settings;
import nl.ru.jtimmerm.orm.types.IType;
import nl.ru.jtimmerm.view.NotHandledException;
import nl.ru.jtimmerm.view.graph.shapes.IShapeElement;

import org.apache.log4j.Logger;

/**
 * The shape mapper maps types to an implementation of shapes. Concrete
 * graph-views should extend this class
 * 
 * @author joost
 * 
 * @param <S>
 */
public abstract class AbstractShapeMapper<S extends IShapeElement<? extends IType>>
		implements IShapeMapper<S> {

	/**
	 * The actual mapping between type an shape
	 */
	private HashMap<IType, S> mMap = new HashMap<IType, S>();

	// ////////////////////////////////////////////////////////////////////////
	// SETTING
	// ////////////////////////////////////////////////////////////////////////

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.ru.jtimmerm.view.graph.IShapeMapper#setTypes(java.util.Collection)
	 */
	@Override
	public void setTypes(Collection<? extends IType> types)
			throws NotHandledException {
		HashMap<IType, S> newMap = new HashMap<IType, S>();

		for (IType iType : types)
			if (mMap.containsKey(iType))
				newMap.put(iType, mMap.get(iType));
			else
				newMap.put(iType, createShape(iType));

		mMap = newMap;

	}

	/**
	 * Find the next empty position in the grid
	 * 
	 * @return
	 */
	public Point getNextGridPosition() {

		boolean[][] array = buildGrid();

		int row = 0, column = 0;

		for (row = 0; row < array.length; row++) {
			for (column = 0; column < Math.max(array[row].length,
					Settings.integer("grid_wrap")); column++) {
				// Find an empty spot _within_ the existing grid
				try {
					if (array[row][column])
						continue;
				} catch (ArrayIndexOutOfBoundsException e) {
					// No need to catch, this spot is 'free'
				}
				return new Point(column, row);
			}
		}

		// Did not find and empty spot, then add a new row
		return new Point(0, row);

	}

	/**
	 * Analyze the positions of the elements in the graph and build a two
	 * dimensional grid of their positions
	 * 
	 * @return boolean[][] A 2 dimensional array indicating whether there is a
	 *         type on a given position:
	 *         <code>boolean[row][column] == true</code> when a type exist on
	 *         row/column position
	 */
	private boolean[][] buildGrid() {

		Point gridSize = new Point(0, 0);
		ArrayList<Integer> xes = new ArrayList<Integer>();
		ArrayList<Integer> ys = new ArrayList<Integer>();

		// Store values and get max size
		for (S shape : mMap.values()) {
			Point p = shape.getGridPosition();

			xes.add(p.x);
			ys.add(p.y);

			if (p.x > gridSize.x)
				gridSize.x = p.x;
			if (p.y > gridSize.y)
				gridSize.y = p.y;
		}

		// Some safety
		if (xes.size() != ys.size())
			throw new IllegalArgumentException(Lang.text("err_size_unequal"));

		// Create and fill the grid
		boolean[][] array = new boolean[gridSize.y + 1][gridSize.x + 1];

		Iterator<Integer> xiter = xes.iterator();
		Iterator<Integer> yiter = ys.iterator();

		while (xiter.hasNext()) {
			int x = xiter.next();
			int y = yiter.next();
			array[y][x] = true;
		}

		return array;
	}

	// ////////////////////////////////////////////////////////////////////////
	// ADDING
	// ////////////////////////////////////////////////////////////////////////

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.ru.jtimmerm.view.graph.IShapeMapper#addType(nl.ru.jtimmerm.orm.types
	 * .IType)
	 */
	@Override
	public void addType(IType type) throws NotHandledException {

		if (type == null)
			throw new NotHandledException(
					Lang.text("unsupported_class", "null"));

		if (!mMap.containsKey(type)) {
			log.trace(Lang.text("adding", type));
			mMap.put(type, createShape(type));
		} else
			log.trace(Lang.text("adding_already_exists", type));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.ru.jtimmerm.view.graph.IShapeMapper#addTypes(java.util.Collection)
	 */
	@Override
	public void addTypes(Collection<? extends IType> types)
			throws NotHandledException {
		for (IType type : types)
			addType(type);
	}

	// ////////////////////////////////////////////////////////////////////////
	// GETTING
	// ////////////////////////////////////////////////////////////////////////

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.ru.jtimmerm.view.graph.IShapeMapper#getShapeFor(nl.ru.jtimmerm.orm
	 * .types.IType)
	 */
	@Override
	public S getShapeFor(IType type) throws NotHandledException {
		if (!mMap.containsKey(type))
			addType(type);

		return mMap.get(type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.view.graph.IShapeMapper#getShapes()
	 */
	@Override
	public Collection<S> getShapes() {
		return mMap.values();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.ru.jtimmerm.view.graph.IShapeMapper#getShapeAtPoint(java.awt.Point)
	 */
	@Override
	public S getShapeAtPoint(Point p) {

		for (S s : getShapes())
			if (s.containsPoint(p))
				return s;

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.ru.jtimmerm.view.graph.IShapeMapper#hasMapped(nl.ru.jtimmerm.orm.types
	 * .IType)
	 */
	@Override
	public boolean hasMapped(IType type) {
		return mMap.containsKey(type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.ru.jtimmerm.view.graph.IShapeMapper#hasMapped(nl.ru.jtimmerm.view.
	 * graph.shapes.IShapeElement)
	 */
	@Override
	public boolean hasMapped(IShapeElement<? extends IType> shape) {
		return mMap.containsValue(shape);
	}

	// ////////////////////////////////////////////////////////////////////////
	// SHAPE CREATION
	// ////////////////////////////////////////////////////////////////////////

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.ru.jtimmerm.view.graph.IShapeMapper#handles(nl.ru.jtimmerm.orm.types
	 * .IType)
	 */
	@Override
	public abstract boolean handles(IType t);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.ru.jtimmerm.view.graph.IShapeMapper#createShape(nl.ru.jtimmerm.orm
	 * .types.IType)
	 */
	@Override
	public S createShape(IType type) throws NotHandledException {

		log.debug(Lang.text("creating_shape", type));

		if (type != null && handles(type))
			return _createTypeShape(type);

		throw new NotHandledException(Lang.text("unsupported_class",
				(type != null) ? type.getClass() : null));
	}

	/**
	 * Create a shape for the given type
	 * 
	 * @param t
	 *            The type
	 * @return A shape representing the type, suitable in this implementation
	 */
	protected abstract S _createTypeShape(IType t);

	// ////////////////////////////////////////////////////////////////////////
	// OUTPUT
	// ////////////////////////////////////////////////////////////////////////

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();

		b.append("Contents of mapper:\n");
		for (Entry<IType, S> e : mMap.entrySet()) {
			b.append("  ");
			b.append(e.getKey());
			b.append(" - ");
			b.append(e.getValue());
			b.append("\n");
		}

		return b.toString();
	}

	// ////////////////////////////////////////////////////////////////////////
	// LOG
	// ////////////////////////////////////////////////////////////////////////
	private final static Logger log = Logger
			.getLogger(AbstractShapeMapper.class);
}
