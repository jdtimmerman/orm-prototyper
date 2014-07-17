package nl.ru.jtimmerm.view.graph.shapes;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import nl.ru.jtimmerm.Lang;
import nl.ru.jtimmerm.orm.types.IType;
import nl.ru.jtimmerm.view.graph.Grid;

import org.apache.log4j.Logger;

/**
 * Provides methods for storing and calculating where to draw a line between two
 * shapes
 * 
 * @author joost
 * 
 */
public class Line {

	/**
	 * The start point of the line
	 */
	protected IShapeElement<? extends IType> mFrom;

	/**
	 * The end point of the line
	 */
	protected IShapeElement<? extends IType> mTo;

	// ////////////////////////////////////////////////////////////////////////
	//
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Create a line between two shapes
	 * 
	 * @param from
	 *            Start
	 * @param to
	 *            End
	 */
	public Line(IShapeElement<? extends IType> from,
			IShapeElement<? extends IType> to) {
		mFrom = from;
		mTo = to;
	}

	/**
	 * @return The shortest available line between the stored shapes
	 */
	protected List<Point> getLine() {
		return shortestLine(mFrom, mTo);
	}

	// ////////////////////////////////////////////////////////////////////////
	// CALCULATIONS
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Calculate which ports are available to connect the two shapes and
	 * calculate the shortest line between them
	 * 
	 * @param source
	 *            The start shape
	 * @param dest
	 *            The end shape
	 * @return Two points (XY) that indicate the start and endpoint of the
	 *         shortest line between the two shapes
	 */
	private static List<Point> shortestLine(
			IShapeElement<? extends IType> source,
			IShapeElement<? extends IType> dest) {

		Set<Point> ps = Grid.addAllGridToRelative(source,
				source.getPorts(dest.getType()));
		Set<Point> pd = Grid.addAllGridToRelative(dest,
				dest.getPorts(source.getType()));

		if (ps.size() == 0 || pd.size() == 0)
			log.warn(Lang.text("no_ports_available", source, dest));

		return shortestLine(ps, pd);
	}

	/**
	 * Find the shortest line between the source and destination points
	 * 
	 * @param source
	 *            A collection of ports to connect the start-shape to the
	 *            end-shape
	 * @param dest
	 *            A collection of ports to connect the end-shape to the
	 *            start-shape
	 * @return Two points (XY) that indicate the start and endpoint of the
	 *         shortest line between the two collections of ports
	 */
	private static List<Point> shortestLine(Collection<Point> source,
			Collection<Point> dest) {

		double shortestDist = -1;
		ArrayList<Point> points = new ArrayList<Point>();

		// Connect all points with each other
		for (Point ps : source)
			for (Point pd : dest) {
				// Calculate distance
				double dist = ps.distance(pd);
				if (shortestDist == -1 || dist < shortestDist) {
					// Store shortest distance
					shortestDist = dist;
					points.clear();
					points.add(ps);
					points.add(pd);
				}
			}

		return points;
	}

	// ////////////////////////////////////////////////////////////////////////
	// LOG
	// ////////////////////////////////////////////////////////////////////////

	private static final Logger log = Logger.getLogger(Line.class);

}
