package nl.ru.jtimmerm.view.graph.shapes;

import java.awt.Point;
import java.util.List;

import nl.ru.jtimmerm.orm.types.IType;

import org.apache.batik.util.SVGConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * SVG implementation of a line
 * 
 * @author joost
 * 
 */
public class SVGLine extends Line {

	/**
	 * Start point
	 */
	private Point start;

	/**
	 * End point
	 */
	private Point end;

	/**
	 * Create a new line between two points
	 * 
	 * @param from
	 *            Start point
	 * @param to
	 *            End point
	 */
	public SVGLine(ISVGShape<? extends IType> from,
			ISVGShape<? extends IType> to) {
		super(from, to);

		List<Point> ports = getLine();
		start = ports.get(0);
		end = ports.get(1);

	}

	/**
	 * Create an element for this line
	 * 
	 * @param d
	 *            The document
	 * @return The element containing the line
	 */
	public Element getElement(Document d) {

		Element e = d.createElementNS(SVGConstants.SVG_NAMESPACE_URI, "line");

		e.setAttributeNS(null, "x1", Integer.toString(start.x));
		e.setAttributeNS(null, "y1", Integer.toString(start.y));
		e.setAttributeNS(null, "x2", Integer.toString(end.x));
		e.setAttributeNS(null, "y2", Integer.toString(end.y));
		e.setAttributeNS(null, "stroke-width", "1");
		e.setAttributeNS(null, "stroke", "black");

		return e;
	}

	/**
	 * @return the start
	 */
	public Point getStart() {
		return start;
	}

	/**
	 * @return the end point
	 */
	public Point getEnd() {
		return end;
	}

}
