package nl.ru.jtimmerm.view.graph;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import nl.ru.jtimmerm.Settings;
import nl.ru.jtimmerm.orm.types.IType;
import nl.ru.jtimmerm.view.graph.shapes.IShapeElement;

/**
 * Class with static helper methods performing calculations on the grid
 * 
 * @author joost
 * 
 */
public class Grid {
	/**
	 * Width in pixels of a tile
	 */
	public static final float GRID_WIDTH = Settings.integer("grid_width");

	/**
	 * Height in pixels of a tile
	 */
	public static final float GRID_HEIGHT = Settings.integer("grid_height");

	/**
	 * Get the absolute position of the tile (top-left corner) in the graph
	 * 
	 * @param p
	 * @return
	 */
	public static Point getXYPosition(Point p) {
		return getXYPosition(p.x, p.y);
	}

	/**
	 * Get the absolute position of the tile (top-left corner) in the graph
	 * 
	 * @param col
	 * @param row
	 * @return
	 */
	public static Point getXYPosition(int col, int row) {

		int y = (int) (row * GRID_HEIGHT);
		int x = (int) (col * GRID_WIDTH);

		return new Point(x, y);
	}

	/**
	 * Get the tile in which contains the given coordinates
	 * 
	 * @param p
	 * @return
	 */
	public static Point getGridPosition(Point p) {
		return getGridPosition(p.x, p.y);
	}

	/**
	 * Get the tile in which contains the given coordinates
	 * 
	 * @param col
	 * @param row
	 * @return
	 */
	public static Point getGridPosition(int col, int row) {

		double c = Math.floor(col / GRID_WIDTH);
		double r = Math.floor(row / GRID_HEIGHT);

		return new Point((int) c, (int) r);

	}

	/**
	 * Move a relative position (within a tile) to an absolute one (within the
	 * entire graph)
	 * 
	 * @param element
	 * @param relative
	 * @return
	 */
	public static Point addGridToRelative(
			IShapeElement<? extends IType> element, Point relative) {

		Point abs = element.getXYPosition();
		abs.x += relative.x;
		abs.x += (int) ((GRID_WIDTH / 2.0) - (element.getWidth() / 2.0));
		abs.y += relative.y;
		abs.y += (int) ((GRID_HEIGHT / 2.0) - (element.getHeight() / 2.0));

		return abs;

	}

	/**
	 * Recalculate positions from relative (within tile) to absolute (within
	 * graph)
	 * 
	 * @param element
	 * @param rel
	 * @return
	 */
	public static Set<Point> addAllGridToRelative(
			IShapeElement<? extends IType> element, Set<Point> rel) {
		Set<Point> ret = new HashSet<Point>();

		for (Point point : rel)
			ret.add(addGridToRelative(element, point));

		return ret;
	}

	/**
	 * Get the highest filled x-grid
	 * 
	 * @return
	 */
	public static int getMaxXGrid() {
		return 0;
	}

	/**
	 * Get the highest filled y-grid
	 * 
	 * @return
	 */
	public static int getMaxYGrid() {
		return 0;
	}

}
