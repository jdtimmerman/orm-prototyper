package nl.ru.jtimmerm.view.graph.shapes;

import java.awt.Point;
import java.util.Collection;

import nl.ru.jtimmerm.Settings;
import nl.ru.jtimmerm.orm.types.IType;
import nl.ru.jtimmerm.view.Selection;
import nl.ru.jtimmerm.view.graph.Grid;

import org.apache.batik.util.SVGConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Provides the basics for a SVG Shape
 * 
 * @author joost
 * 
 * @param <T>
 */
public abstract class AbstractBatikShapeElement<T extends IType> extends
		AbstractShapeElement<T> implements ISVGShape<T>, SVGConstants {

	/**
	 * The class used for tiles
	 */
	private static final String TILE_CLASS = "tile";

	/**
	 * The class used file ports
	 */
	private static final String PORTS_CLASS = "ports";

	/**
	 * The radius of ports
	 */
	private static final String PORTS_SIZE = Settings.string("ports_size");

	/**
	 * Create a new shape
	 * 
	 * @param type
	 *            The type to create a new shape for
	 */
	public AbstractBatikShapeElement(T type) {
		super(type);
	}

	/**
	 * Create a new shape
	 * 
	 * @param type
	 *            The type to create a new shape for
	 * @param grid
	 *            The initial grid position of the shape
	 */
	public AbstractBatikShapeElement(T type, Point grid) {
		super(type, grid);
	}

	// ////////////////////////////////////////////////////////////////////////
	// MANIPULATION
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Translate the given shape a certain amount of pixels
	 * 
	 * @param e
	 *            The element to be translated
	 * @param x
	 *            Translation in x direction
	 * @param y
	 *            Translation in y direction
	 */
	private static void translateElement(Element e, int x, int y) {
		e.setAttribute("transform", "translate(" + x + "," + y + ")");
	}

	/**
	 * Translate the given shape a certain amount of pixels
	 * 
	 * @param e
	 *            The element to be translated
	 * @param p
	 *            A point containing the translations values. This method calls
	 *            {@link #translateElement(Element, int, int)} If, for example,
	 *            the point contains <code>[x=10, y=20]</code> the shape will be
	 *            translated 10px in the x direction and 20px in the y direction
	 */
	private static void translateElement(Element e, Point p) {
		translateElement(e, p.x, p.y);
	}

	// ////////////////////////////////////////////////////////////////////////
	// CREATION
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Create a tile for this shape and fill it with the actual shape
	 * 
	 * @param doc
	 *            The document for which this tile should be available
	 * @param drawSelection
	 *            Whether to draw selection hints
	 * @return The element for the tile
	 */
	private Element createTile(Document doc, boolean drawSelection) {

		T type = getType();

		// Create root elements
		Element tile = createGroup(doc, TILE_CLASS, "tile-" + type.hashCode());
		Element group = createGroup(doc, type.getClass().toString(),
				"" + type.hashCode());

		// Setup
		tile.setAttribute("width", Grid.GRID_WIDTH + "");
		tile.setAttribute("height", Grid.GRID_HEIGHT + "");

		// Add the content
		createShape(doc, group);

		// Move the content to the center
		int dx = (int) ((Grid.GRID_WIDTH / 2.0) - (getWidth() / 2.0));
		int dy = (int) ((Grid.GRID_HEIGHT / 2.0) - (getHeight() / 2.0));
		translateElement(group, dx, dy);

		tile.appendChild(group);

		// Translate tile
		translateElement(tile, getXYPosition());

		// Draw selection information
		if (drawSelection && Selection.getSingleton().isSelected(getType())) {

			// Draw ports only is enabled in settings
			if (Settings.bool("draw_ports"))
				group.appendChild(createPorts(doc, getPorts()));

			tile.appendChild(createBoundingBox(doc));
		}

		return tile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.ru.jtimmerm.view.graph.shapes.IShapeElement#containsPoint(java.awt
	 * .Point)
	 * 
	 * 
	 * TODO: Maybe not only base on the grid but also calculate the actual shape
	 */
	@Override
	public boolean containsPoint(Point p) {

		Point shapeGrid = getGridPosition();
		Point clickedGrid = Grid.getGridPosition(p);

		return shapeGrid.x == clickedGrid.x && shapeGrid.y == clickedGrid.y;
	}

	/**
	 * Color for the selectionbox
	 */
	private static final String SELECTION_BOX_STROKE = "red";

	/**
	 * Create a box with the size of the tile, for example to highlight
	 * selection
	 * 
	 * @param doc
	 *            The document for which the box should be available
	 * @return The element for the bounding box
	 */
	private Element createBoundingBox(Document doc) {
		Element r = doc.createElementNS(SVG_NAMESPACE_URI, SVG_RECT_TAG);
		r.setAttributeNS(null, SVG_X_ATTRIBUTE, "0");
		r.setAttributeNS(null, SVG_Y_ATTRIBUTE, "0");
		r.setAttributeNS(null, SVG_WIDTH_ATTRIBUTE,
				Float.toString(Grid.GRID_WIDTH));
		r.setAttributeNS(null, SVG_HEIGHT_ATTRIBUTE,
				Float.toString(Grid.GRID_HEIGHT));
		r.setAttributeNS(null, SVG_FILL_ATTRIBUTE, SVG_NONE_VALUE);
		r.setAttributeNS(null, SVG_STROKE_ATTRIBUTE, SELECTION_BOX_STROKE);
		return r;
	}

	/**
	 * A group containing shapes for the given ports
	 * 
	 * @param doc
	 *            The document for which the group should be availablea
	 * @return A group containing port shapes
	 */
	private Element createPorts(Document doc, Collection<Point> ports) {
		Element g = createGroup(doc, PORTS_CLASS, "a");

		for (Point p : ports) {
			Element portShape = doc.createElementNS(SVG_NAMESPACE_URI,
					SVG_CIRCLE_TAG);
			portShape.setAttributeNS(null, SVG_CX_ATTRIBUTE, p.x + "");
			portShape.setAttributeNS(null, SVG_CY_ATTRIBUTE, p.y + "");
			portShape.setAttributeNS(null, SVG_R_ATTRIBUTE, PORTS_SIZE);
			portShape.setAttributeNS(null, SVG_FILL_ATTRIBUTE, SVG_NONE_VALUE);
			portShape.setAttributeNS(null, SVG_STROKE_ATTRIBUTE, "red");
			g.appendChild(portShape);
		}

		return g;
	}

	/**
	 * Create a group for the given document
	 * 
	 * @param doc
	 *            The document for which this group should be available
	 * @param clazz
	 *            The class of the group
	 * @param id
	 *            The ID of the group
	 * @return The group element
	 */
	public static Element createGroup(Document doc, String clazz, String id) {
		Element e = doc.createElementNS(SVG_NAMESPACE_URI, SVG_G_TAG);

		e.setAttributeNS(null, SVG_CLASS_ATTRIBUTE, clazz);
		e.setAttributeNS(null, SVG_ID_ATTRIBUTE, id);
		return e;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.ru.jtimmerm.view.graph.shapes.ISVGShape#getElement(org.w3c.dom.Document
	 * )
	 */
	@Override
	public Element getElement(Document doc) {
		return getElement(doc, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.ru.jtimmerm.view.graph.shapes.ISVGShape#getElement(org.w3c.dom.Document
	 * , boolean)
	 */
	@Override
	public Element getElement(Document doc, boolean drawSelection) {
		return createTile(doc, drawSelection);
	}

	/**
	 * Creates the shape and append it to the given parent
	 * 
	 * @param doc
	 *            The document for which the elements should be available
	 * @param parent
	 *            The parent of the to be created element
	 */
	abstract void createShape(Document doc, Element parent);

	/*
	 * (non-Javadoc)
	 * @see nl.ru.jtimmerm.orm.types.IType#compareTo(nl.ru.jtimmerm.orm.types.IType)
	 */
	@Override
	public int compareTo(IType o) {
		return getType().compareTo(o);
	}
	
	// ////////////////////////////////////////////////////////////////////////

	private static final long serialVersionUID = 6872388405214855205L;
}
