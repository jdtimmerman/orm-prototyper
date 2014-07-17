package nl.ru.jtimmerm.view.graph.shapes;

import java.awt.Color;
import java.awt.Point;

import nl.ru.jtimmerm.Settings;
import nl.ru.jtimmerm.orm.types.IType;
import nl.ru.jtimmerm.orm.types.Type;
import nl.ru.jtimmerm.view.Selection;
import nl.ru.jtimmerm.view.graph.Grid;

/**
 * Provide basic methods for types (positioning, color, ...). To be overwritten
 * by concrete view implementation
 * 
 * @author joost
 * 
 */
public abstract class AbstractShapeElement<T extends IType> extends Type implements
		IShapeElement<T>  {

	/**
	 * The type this shape represents
	 */
	private T mType;

	// ////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Construtor, set the type
	 * 
	 * @param type
	 *            The type this shape will represent
	 */
	public AbstractShapeElement(T type) {
		mType = type;
	}

	/**
	 * Constructor, set the type an position in grid
	 * 
	 * @param type
	 *            The type this shape will represent
	 * @param grid
	 *            The grid position of this shape as a <code>Point</code>
	 */
	public AbstractShapeElement(T type, Point grid) {
		this(type);
		setGridPosition(grid);
	}

	// ////////////////////////////////////////////////////////////////////////
	// GRID
	// ////////////////////////////////////////////////////////////////////////

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.view.graph.shapes.IShapeElement#getXYPosition()
	 */
	@Override
	public Point getXYPosition() {
		return Grid.getXYPosition(getGridPosition());
	}

	// ////////////////////////////////////////////////////////////////////////
	// COLOR
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * @return The color used for drawing ports
	 */
	public Color getPortColor() {
		return new Color(Settings.integer("color_port"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.view.graph.shapes.IShapeElement#getFGColor()
	 */
	@Override
	public Color getFGColor() {
		return new Color(Settings.integer("color_fg"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.view.graph.shapes.IShapeElement#getBGColor()
	 */
	@Override
	public Color getBGColor() {
		return new Color(Settings.integer("color_bg"));
	}

	// ////////////////////////////////////////////////////////////////////////
	// SELECTING
	// ////////////////////////////////////////////////////////////////////////
	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.view.graph.shapes.IShapeElement#isSelected()
	 */
	@Override
	public boolean isSelected() {
		if (Selection.getSingleton().hasSelectedShape())
			return Selection.getSingleton().getSelectedShape().equals(mType);

		return false;
	}

	// ////////////////////////////////////////////////////////////////////////
	// RETURN CONTENT
	// ////////////////////////////////////////////////////////////////////////

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.view.graph.shapes.IShapeElement#getType()
	 */
	@Override
	public T getType() {
		return mType;
	}

	// ////////////////////////////////////////////////////////////////////////
	// LOG
	// ////////////////////////////////////////////////////////////////////////

	// private static Logger log = Logger.getLogger(AbstractShapeElement.class);

	// ////////////////////////////////////////////////////////////////////////
	//
	// ////////////////////////////////////////////////////////////////////////

	private static final long serialVersionUID = -188979111292967694L;
}
