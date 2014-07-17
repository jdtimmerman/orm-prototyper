package nl.ru.jtimmerm.view.graph;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputListener;

import nl.ru.jtimmerm.Settings;
import nl.ru.jtimmerm.orm.types.IType;
import nl.ru.jtimmerm.view.NotHandledException;
import nl.ru.jtimmerm.view.Selection;
import nl.ru.jtimmerm.view.graph.shapes.IShapeElement;

/**
 * This methods holds some graph-implementation independent methods
 * 
 * @author joost
 * 
 * @param <S>
 */
public abstract class AbstractGraph<S> implements IGraph, MouseInputListener {

	/**
	 * Create the graph with a new shape mapper
	 */
	public AbstractGraph() {
		mShapeMapper = createShapeMapper();
	}

	// ////////////////////////////////////////////////////////////////////////
	// SHAPE MAPPER
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Holds the shape mapper
	 */
	protected IShapeMapper<? extends IShapeElement<? extends IType>> mShapeMapper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.view.graph.IGraph#getShapeMapper()
	 */
	@Override
	public IShapeMapper<? extends IShapeElement<? extends IType>> getShapeMapper() {
		return mShapeMapper;
	}

	/**
	 * To be overwritten to provide a concrete implementation
	 * 
	 * @return A concrete implementation of the shapemapper
	 */
	protected abstract IShapeMapper<? extends IShapeElement<? extends IType>> createShapeMapper();

	// ////////////////////////////////////////////////////////////////////////
	// MOUSE INTERACTION
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Apply a transformation of the click, like scrolling and zooming
	 * 
	 * @param e
	 * @return
	 */
	protected abstract Point applyClickTransformation(Point e);

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent e) {

		Point clicked = applyClickTransformation(e.getPoint());

		IShapeElement<? extends IType> s = mShapeMapper
				.getShapeAtPoint(clicked);

		IType t = (s == null) ? null : s.getType();
		Selection.getSingleton().select(t);

	}

	/**
	 * A temporary flag whether the user is dragger
	 */
	boolean dragged = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e) {

		Point clicked = applyClickTransformation(e.getPoint());

		// If we were dragging
		if (dragged) {

			// And we had selected a shape
			IType type = Selection.getSingleton().getSelectedShape();

			if (type != null) {
				// Move this shape to the new position
				try {
					mShapeMapper.getShapeFor(type).setGridPosition(
							Grid.getGridPosition(clicked));
				} catch (NotHandledException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				// Trigger change in selection so the shape will be redrawn
				Selection.getSingleton().select(type);
			}

			dragged = false;
			refreshGraph();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent
	 * )
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		dragged = true;
	}

	// Ignore these

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	// ////////////////////////////////////////////////////////////////////////
	// EXPORTING
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * TODO: Ask for a filename
	 * 
	 * @return
	 */
	protected String getFileName() {
		return Settings.string("default_output_filename");
	}

	// ///////////////////////////////////////////////////////////////
	// LOG
	// ///////////////////////////////////////////////////////////////

	// private final static Logger log = Logger.getLogger(AbstractGraph.class);
}
