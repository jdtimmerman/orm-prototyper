package nl.ru.jtimmerm.view.graph.shapes;

import java.awt.Point;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import nl.ru.jtimmerm.Lang;
import nl.ru.jtimmerm.Settings;
import nl.ru.jtimmerm.orm.types.IFactType;
import nl.ru.jtimmerm.orm.types.IObjectType;
import nl.ru.jtimmerm.orm.types.IType;
import nl.ru.jtimmerm.orm.types.UnicityConstraint;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

/**
 * Represent a fact type in SVG
 * 
 * @author joost
 * 
 */
public class FactTypeSVGShape extends AbstractBatikShapeElement<IFactType> {
	
	/**
	 * The width of a role
	 */
	private static final int	WIDTH		= Settings.integer("ft_shape_width");
	
	/**
	 * The height of a role
	 */
	private static final int	HEIGHT		= Settings.integer("ft_shape_height");
	
	/**
	 * Spacing between shape and verbalization
	 */
	private static final int	TEXTSPACING	= Settings.integer("shape_text_spacing");
	
	// ////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR
	// ////////////////////////////////////////////////////////////////////////
	
	/**
	 * Create a new shape
	 * 
	 * @param type
	 *            The type this shape represents
	 */
	public FactTypeSVGShape(IFactType type) {
		super(type);
	}
	
	/**
	 * Create a new shape
	 * 
	 * @param type
	 *            The type this shape represents
	 * @param grid
	 *            The initial position of the shape
	 */
	public FactTypeSVGShape(IFactType type, Point grid) {
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
		IFactType ft = getType();
		
		Element group = doc.createElementNS(SVG_NAMESPACE_URI, SVG_G_TAG);
		
		// Add unicity constraints
		if (ft.hasUnicity()) {
			int i = 0;
			for (UnicityConstraint constraint : ft.getUnicities().getConstraints()) {
				// Move constraint a little bit upwards
				int y = -4 - i++ * 4;
				
				group.appendChild(createUnicity(doc, constraint, y));
			}
			// for (int i = 0; i < ft.getUnicities().getNumberOfConstraints();
			// i++) {
			// int y = -4 - i * 4;
			// group.appendChild(createUnicity(doc,
			// ft.getUnicities().getConstraints().iterator().next(), y));
			// }
			// Or indicate something is missing
		} else {
			group.appendChild(createErrorMarker(doc));
		}
		
		// Add a block for each fact
		int n = ft.getObjectTypes().size();
		for (int i = 0; i < n; i++)
			group.appendChild(createRole(doc, i));
		
		group.appendChild(createText(ft.getCleanVerbalization(), doc, n));
		
		parent.appendChild(group);
		
	}
	
	// ////////////////////////////////////////////////////////////////////////
	// DOM MANIPULATION
	// ////////////////////////////////////////////////////////////////////////
	
	/**
	 * Create a 'glow' kind of indication that this fact type contains an error
	 * or is not complete
	 * 
	 * @param doc
	 *            The document
	 * @return An element
	 */
	private Element createErrorMarker(Document doc) {
		Element g = doc.createElementNS(SVG_NAMESPACE_URI, SVG_RECT_TAG);
		
		// Position
		g.setAttributeNS(null, SVG_X_ATTRIBUTE, "0");
		g.setAttributeNS(null, SVG_Y_ATTRIBUTE, "-1");
		// Size
		g.setAttributeNS(null, SVG_WIDTH_ATTRIBUTE, Integer.toString(getWidth() + 4));
		g.setAttributeNS(null, SVG_HEIGHT_ATTRIBUTE, Integer.toString(getHeight() + 4));
		// Style
		g.setAttributeNS(null, SVG_FILL_ATTRIBUTE, Settings.string("uc_missing"));
		
		return g;
	}
	
	/**
	 * Create the line for a unicity constraint
	 * 
	 * @param doc
	 *            The document
	 * @param roles
	 *            An unicity constraint
	 * @param y
	 *            A height spacing
	 * @return The element for this unicity constraint
	 */
	private Element createUnicity(Document doc, UnicityConstraint constraint, int y) {
		log.debug("Creating Shape for UC: " + Arrays.toString(constraint.getConstraintRoles()));
		
		Element g = doc.createElementNS(SVG_NAMESPACE_URI, SVG_G_TAG);
		
		// UC Constraint
		
		// Used to indicate a long, multi-role, constraint is open
		boolean lineIsOpen = false;
		Element line = null;
		
		boolean[] roles = constraint.getConstraintRoles();
		
		int x = LINE_WIDTH;
		for (int i = 0; i < roles.length; i++) {
			x = LINE_WIDTH + (i * (WIDTH - LINE_WIDTH));
			
			if (roles[i] && !lineIsOpen) {
				// This role is under a constraint and we should start a new
				// line
				line = startANewLine(doc, x, y);
				
				// Administration
				lineIsOpen = true;
			} else if (!roles[i] && lineIsOpen) {
				// This role is not under constraint and we should close the
				// open line
				closeLine(line, x, y);
				
				// Add this line to the shape
				g.appendChild(line);
				
				// Administration
				lineIsOpen = false;
				line = null;
			} else {
				// No action neccesary
			}
		}
		
		// If we still have a open line, close it now
		if (lineIsOpen) {
			closeLine(line, x + WIDTH, y);
			g.appendChild(line);
			lineIsOpen = false;
		}
		
		return g;
	}
	
	/**
	 * Create a new line element and set the start position
	 * 
	 * @param doc
	 * @param x
	 * @param y
	 * @return
	 */
	private static Element startANewLine(Document doc, int x, int y) {
		Element line = doc.createElementNS(SVG_NAMESPACE_URI, SVG_LINE_TAG);
		
		// Style
		line.setAttributeNS(null, SVG_STROKE_WIDTH_ATTRIBUTE, Settings.string("uc_width"));
		line.setAttributeNS(null, SVG_STROKE_ATTRIBUTE, Settings.string("uc_color"));
		
		// Start position
		line.setAttributeNS(null, SVG_X1_ATTRIBUTE, Integer.toString(x));
		line.setAttributeNS(null, SVG_Y1_ATTRIBUTE, Integer.toString(y));
		
		return line;
	}
	
	/**
	 * Set the end position of the line
	 * 
	 * @param line
	 * @param x
	 * @param y
	 */
	private static void closeLine(Element line, int x, int y) {
		// End position
		line.setAttributeNS(null, SVG_X2_ATTRIBUTE, Integer.toString(x + LINE_WIDTH));
		line.setAttributeNS(null, SVG_Y2_ATTRIBUTE, Integer.toString(y));
	}
	
	/**
	 * Create the shape for the i'th role in this fact type
	 * 
	 * @param doc
	 *            The document
	 * @param i
	 *            The i'th role
	 * @return The element for this role
	 */
	private Element createRole(Document doc, int i) {
		
		// The block
		Element f = doc.createElementNS(SVG_NAMESPACE_URI, SVG_RECT_TAG);
		
		// Position
		f.setAttributeNS(null, SVG_X_ATTRIBUTE, Integer.toString(LINE_WIDTH + i * (WIDTH - LINE_WIDTH)));
		f.setAttributeNS(null, SVG_Y_ATTRIBUTE, "0");
		// Size
		f.setAttributeNS(null, SVG_WIDTH_ATTRIBUTE, Integer.toString(WIDTH));
		f.setAttributeNS(null, SVG_HEIGHT_ATTRIBUTE, Integer.toString(HEIGHT));
		// Style
		f.setAttributeNS(null, SVG_FILL_ATTRIBUTE, "white");
		f.setAttributeNS(null, SVG_STROKE_ATTRIBUTE, "black");
		f.setAttributeNS(null, SVG_STROKE_WIDTH_ATTRIBUTE, Integer.toString(LINE_WIDTH));
		
		return f;
	}
	
	/**
	 * Create the verbalization text for under the shape
	 * 
	 * @param text
	 *            The text
	 * @param doc
	 *            The document
	 * @param nRoles
	 *            The number of roles in this fact type
	 * @return An element for the text
	 */
	private static Element createText(String text, Document doc, int nRoles) {
		Element t = doc.createElementNS(SVG_NAMESPACE_URI, SVG_TEXT_TAG);
		// Position
		t.setAttributeNS(null, SVG_TEXT_ANCHOR_ATTRIBUTE, "middle");
		t.setAttributeNS(null, SVG_X_ATTRIBUTE, Double.toString(.5 * nRoles * WIDTH));
		t.setAttributeNS(null, SVG_Y_ATTRIBUTE, Integer.toString(HEIGHT + FONT_SIZE + TEXTSPACING));
		// Style
		t.setAttributeNS(null, SVG_FILL_ATTRIBUTE, "black");
		t.setAttributeNS(null, SVG_FONT_SIZE_ATTRIBUTE, FONT_SIZE + "px");
		t.setTextContent(text);
		
		return t;
	}
	
	/**
	 * The radius of a totality constraint
	 */
	private static final int	TOTALITY_SIZE	= Settings.integer("tc_radius");
	
	/**
	 * Create a shape for a totality constraint
	 * 
	 * @param doc
	 *            The document
	 * @param p
	 *            The point of the constraint
	 * @return A shape for the constraint
	 */
	public static Element createTotality(SVGDocument doc, Point p) {
		
		Element f = doc.createElementNS(SVG_NAMESPACE_URI, SVG_CIRCLE_TAG);
		f.setAttributeNS(null, SVG_R_ATTRIBUTE, TOTALITY_SIZE + "");
		f.setAttributeNS(null, SVG_FILL_ATTRIBUTE, "black");
		f.setAttributeNS(null, SVG_CX_ATTRIBUTE, Integer.toString(p.x));
		f.setAttributeNS(null, SVG_CY_ATTRIBUTE, Integer.toString(p.y));
		
		return f;
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
		int blocks = getType().getObjectTypes().size();
		int wblocks = blocks * WIDTH;
		
		return wblocks - (blocks * LINE_WIDTH);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.view.graph.shapes.IShapeElement#getHeight()
	 */
	@Override
	public int getHeight() {
		
		return HEIGHT - LINE_WIDTH;
	}
	
	// ////////////////////////////////////////////////////////////////////////
	// PORTS
	// ////////////////////////////////////////////////////////////////////////
	
	/**
	 * Get the ports for the i'th role
	 * 
	 * @param i
	 *            The number of the role we are creating ports for
	 * @param single
	 *            This is a unary role, ports should surround the entire shape
	 * @return A collection of port locations
	 */
	private Set<Point> getPorts(int i, boolean single) {
		
		Set<Point> p = new HashSet<Point>();
		
		int x = (i * WIDTH) + (WIDTH / 2);
		p.add(new Point(x, 0));
		p.add(new Point(x, HEIGHT));
		
		if (single) {
			// Left
			p.add(new Point(0, HEIGHT / 2));
			p.add(new Point(0, 0));
			p.add(new Point(0, HEIGHT));
			// Top
			p.add(new Point(0, 0));
			// Bottom
			p.add(new Point(0, HEIGHT));
			// Right
			p.add(new Point(WIDTH, HEIGHT / 2));
			p.add(new Point(WIDTH, 0));
			p.add(new Point(WIDTH, HEIGHT));
		} else if (i == 0) {
			// First object will be allowed to connect to the side
			p.add(new Point(0, HEIGHT / 2));
			// And corners
			p.add(new Point(0, 0));
			p.add(new Point(0, HEIGHT));
		} else if (i == getType().getObjectTypes().size() - 1) {
			// Last object will be allowed to connect to the side
			p.add(new Point(WIDTH * (i + 1), HEIGHT / 2));
			// And corners
			p.add(new Point(WIDTH * (i + 1), 0));
			p.add(new Point(WIDTH * (i + 1), HEIGHT));
		}
		
		return p;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.ru.jtimmerm.view.graph.shapes.IShapeElement#getPorts(nl.ru.jtimmerm
	 * .orm.types.IType)
	 */
	@Override
	public Set<Point> getPorts(IType relation) {
		
		// Relation is objecttype
		if (relation instanceof IObjectType) {
			// TODO: Here lies the bug that a line is now drawn if you have two times the same object in a facttype
			// e.g. "Team plays against Team on Date"
			int i = getType().getObjectTypes().indexOf(relation);
			return getPorts(i, getType().getObjectTypes().size() == 1);
		}
		
		// Relation is undefined
		else {
			log.debug(Lang.text("ports_not_implemented", relation.getClass().getName()));
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
		
		int objs = getType().getObjectTypes().size();
		
		// Get ports for all object types
		if (getType().getObjectTypes().size() == 1)
			p.addAll(getPorts(0, true));
		for (int i = 0; i < objs; i++)
			p.addAll(getPorts(i, false));
		
		return p;
	}
	
	// ////////////////////////////////////////////////////////////////////////
	// LOG
	// ////////////////////////////////////////////////////////////////////////
	
	private static Logger		log					= Logger.getLogger(FactTypeSVGShape.class);
	
	private static final long	serialVersionUID	= 1865894436763090787L;
}
