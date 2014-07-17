package nl.ru.jtimmerm.view.graph.shapes;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import nl.ru.jtimmerm.Lang;
import nl.ru.jtimmerm.Settings;
import nl.ru.jtimmerm.orm.types.IFactType;
import nl.ru.jtimmerm.orm.types.IObjectType;
import nl.ru.jtimmerm.orm.types.IType;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A SVG shape representing a fact type
 * 
 * @author joost
 * 
 */
public class ObjectTypeSVGShape extends AbstractBatikShapeElement<IObjectType>
		implements ISVGShape<IObjectType> {

	/**
	 * Width of the shape
	 */
	private static final int WIDTH = Settings.integer("ot_shape_width");
	/**
	 * Height of the shape
	 */
	private static final int HEIGHT = Settings.integer("ot_shape_height");
	/**
	 * Radius of the corners
	 */
	private static final int CORNER_RADIUS = Settings
			.integer("ot_shape_corner_radius");

	// ////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Create a new shape for the given type
	 * 
	 * @param type
	 *            The type
	 */
	public ObjectTypeSVGShape(IObjectType type) {
		super(type);
	}

	/**
	 * Create a new shape for the given type
	 * 
	 * @param type
	 *            The type
	 * @param grid
	 *            The initial position of the shape
	 */
	public ObjectTypeSVGShape(IObjectType type, Point grid) {
		super(type, grid);
	}

	// ////////////////////////////////////////////////////////////////////////
	// DOM CREATION
	// ////////////////////////////////////////////////////////////////////////

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.ru.jtimmerm.view.graph.shapes.AbstractBatikShapeElement#createShape
	 * (org.w3c.dom.Document, org.w3c.dom.Element)
	 */
	@Override
	protected void createShape(Document doc, Element parent) {
		parent.appendChild(createShape(doc, getType().isValueType()));
		parent.appendChild(createText(getType().getContent(), doc));
	}

	/**
	 * Create a XML element for this shape
	 * 
	 * @param doc
	 *            The document
	 * @param isValueType
	 *            Whether this shape represents a value type (otherwise it's an
	 *            object type)
	 * @return The element for the shape
	 */
	private static Element createShape(Document doc, boolean isValueType) {

		Element f = doc.createElementNS(SVG_NAMESPACE_URI, SVG_RECT_TAG);
		// Position
		f.setAttributeNS(null, SVG_X_ATTRIBUTE, "0");
		f.setAttributeNS(null, SVG_Y_ATTRIBUTE, "0");
		// Size
		f.setAttributeNS(null, SVG_WIDTH_ATTRIBUTE, Integer.toString(WIDTH));
		f.setAttributeNS(null, SVG_HEIGHT_ATTRIBUTE, Integer.toString(HEIGHT));
		// Rounded corner radius
		f.setAttributeNS(null, SVG_RX_ATTRIBUTE,
				Integer.toString(CORNER_RADIUS));
		f.setAttributeNS(null, SVG_RY_ATTRIBUTE,
				Integer.toString(CORNER_RADIUS));

		// Style
		f.setAttributeNS(null, SVG_FILL_ATTRIBUTE, "white");
		f.setAttributeNS(null, SVG_STROKE_ATTRIBUTE, "black");
		f.setAttributeNS(null, SVG_STROKE_WIDTH_ATTRIBUTE,
				Integer.toString(LINE_WIDTH));

		if (isValueType)
			f.setAttributeNS(null, SVG_STROKE_DASHARRAY_ATTRIBUTE,
					Settings.string("vt_dash_array"));

		return f;
	}

	/**
	 * Create the text for the shape
	 * 
	 * @param text
	 *            The text
	 * @param doc
	 *            The document
	 * @return The element containing the text
	 */
	private static Element createText(String text, Document doc) {
		Element t = doc.createElementNS(SVG_NAMESPACE_URI, SVG_TEXT_TAG);
		t.setAttributeNS(null, SVG_FILL_ATTRIBUTE, "black");
		t.setAttributeNS(null, SVG_TEXT_ANCHOR_ATTRIBUTE, SVG_MIDDLE_VALUE);
		t.setAttributeNS(null, SVG_X_ATTRIBUTE, Double.toString(.5 * WIDTH));
		t.setAttributeNS(null, SVG_Y_ATTRIBUTE,
				Double.toString((.5 * HEIGHT) + FONT_SIZE / 2.0));
		t.setAttributeNS(null, SVG_FONT_SIZE_ATTRIBUTE, FONT_SIZE + "px");
		t.setTextContent(text);
		return t;
	}

	// ////////////////////////////////////////////////////////////////////////
	// CALCULATIONS
	// ////////////////////////////////////////////////////////////////////////

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.view.graph.shapes.IShapeElement#getWidth()
	 */
	@Override
	public int getWidth() {
		return WIDTH;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.view.graph.shapes.IShapeElement#getHeight()
	 */
	@Override
	public int getHeight() {
		return HEIGHT;
	}

	// ////////////////////////////////////////////////////////////////////////
	// PORTS
	// ////////////////////////////////////////////////////////////////////////

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.ru.jtimmerm.view.graph.shapes.IShapeElement#getPorts(nl.ru.jtimmerm
	 * .orm.types.IType)
	 */
	@Override
	public Set<Point> getPorts(IType relation) {

		// For a fact type, all ports are valid
		if (relation instanceof IFactType)
			return getPorts();

		// Relation is undefined (TODO: constraints?)
		else {
			log.debug(Lang.text("ports_not_implemented", relation.getClass()
					.getName()));
			return new HashSet<Point>();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.view.graph.shapes.IShapeElement#getPorts()
	 */
	@Override
	public Set<Point> getPorts() {
		Set<Point> p = new HashSet<Point>();

		// Basic implementation, NSWE get a port
		// TODO include direction

		int cornerdelta = (int) (CORNER_RADIUS / Math.PI);

		p.add(new Point(getWidth() / 2, 0)); // N
		p.add(new Point(getWidth() - CORNER_RADIUS, 0)); // NNE
		p.add(new Point(getWidth() - cornerdelta, cornerdelta)); // NE
		p.add(new Point(getWidth(), getHeight() / 2)); // E
		p.add(new Point(getWidth() - CORNER_RADIUS, getHeight())); // SSE
		p.add(new Point(getWidth() - cornerdelta, getHeight() - cornerdelta)); // SE
		p.add(new Point(getWidth() / 2, getHeight())); // S
		p.add(new Point(cornerdelta, getHeight() - cornerdelta)); // SW
		p.add(new Point(CORNER_RADIUS, getHeight())); // SSW
		p.add(new Point(0, getHeight() / 2)); // W
		p.add(new Point(cornerdelta, cornerdelta)); // NW
		p.add(new Point(CORNER_RADIUS, 0)); // NNW

		return p;
	}

	// ////////////////////////////////////////////////////////////////////////
	// LOG
	// ////////////////////////////////////////////////////////////////////////

	private static Logger log = Logger.getLogger(ObjectTypeSVGShape.class);

	private static final long serialVersionUID = 1865894436763090787L;
}
