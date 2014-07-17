package nl.ru.jtimmerm.view.graph;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.io.FileWriter;

import nl.ru.jtimmerm.Lang;
import nl.ru.jtimmerm.Settings;
import nl.ru.jtimmerm.orm.types.IType;
import nl.ru.jtimmerm.view.graph.shapes.BatikShapeMapper;
import nl.ru.jtimmerm.view.graph.shapes.ISVGShape;
import nl.ru.jtimmerm.view.graph.shapes.IShapeElement;

import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.JSVGScrollPane;
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.apache.batik.swing.gvt.GVTTreeRendererListener;
import org.apache.log4j.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGDocument;

/**
 * The graph implemented using the Batik SVG Toolkit
 * 
 * @author joost
 * 
 */
public class BatikGraph extends AbstractGraph<ISVGShape<? extends IType>> {

	// ////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR / IMPLEMENTATIONS
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Create a new batik graph
	 */
	public BatikGraph() {
		super();
		mCanvas = new Canvas();
		mScroll = new ScrollPane(mCanvas);

		mCanvas.addMouseListener(this);
		mCanvas.addMouseMotionListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.view.IComponentContainer#getComponent()
	 */
	@Override
	public Component getComponent() {
		return mScroll;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.view.graph.AbstractGraph#createShapeMapper()
	 */
	@Override
	public IShapeMapper<? extends IShapeElement<? extends IType>> createShapeMapper() {
		return new BatikShapeMapper();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.ru.jtimmerm.view.graph.AbstractGraph#applyClickTransformation(java
	 * .awt.Point)
	 */
	@Override
	protected Point applyClickTransformation(Point e) {
		// Apply scrolling to the grid
		AffineTransform transform = mCanvas.getViewBoxTransform();

		double x = e.getX() - transform.getTranslateX();
		double y = e.getY() - transform.getTranslateY();
		return new Point((int) x, (int) y);
	}

	// ////////////////////////////////////////////////////////////////////////
	// UPDATES
	// ////////////////////////////////////////////////////////////////////////

	/*
	 * (non-Javadoc)
	 * @see nl.ru.jtimmerm.view.graph.IGraph#refreshGraph()
	 */
	@Override
	public void refreshGraph() {
		log.trace(Lang.text("refreshing_graph"));
		mCanvas.renewDocument(BatikUtil.createDocument(mShapeMapper));
	}

	// ////////////////////////////////////////////////////////////////////////
	// CONTAINERS
	// ////////////////////////////////////////////////////////////////////////

	// ////////////////////////////////////////////////////////////////////////
	// SCROLLPANE

	/**
	 * Holds the scrollpane that wraps the canvas
	 */
	private ScrollPane mScroll;

	/**
	 * A scrollpane for the SVG Canvas
	 * 
	 * @author joost
	 */
	private static class ScrollPane extends JSVGScrollPane {

		private static final long serialVersionUID = 8503740310531030234L;

		public ScrollPane(JSVGCanvas canvas) {
			super(canvas);
			setScrollbarsAlwaysVisible(true);
			
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	// CANVAS

	/**
	 * Holds the canvas
	 */
	private Canvas mCanvas;

	/**
	 * The canvas holds the batik shapes and draws a grid
	 * 
	 * @author joost
	 */
	private static class Canvas extends JSVGCanvas implements GVTTreeRendererListener {

		private static final long serialVersionUID = 8127463619856366779L;
		private final Color GRID_COLOR = new Color(
				Settings.integer("color_grid"));

		// CONSTRUCT //////////////////////////////////////////////////////////

		/**
		 * Create a new canvas with an empty document
		 */
		public Canvas() {
			super();
			setDocumentState(ALWAYS_DYNAMIC);

			// Style
			setOpaque(false);
			setBackground(new Color(0, 0, 0, 0));

			// Listener
			addGVTTreeRendererListener(this);

			// Initialize painting components
			setSVGDocument(BatikUtil.createEmptyDocument());
		}

		// MODIFICATION ///////////////////////////////////////////////////////

		/**
		 * Indicatates whether the canvas has finished its first render, the
		 * canvas is now ready for modification of the dom
		 */
		private boolean isReadyForModification = false;

		/**
		 * Lock this object
		 */
		private Object QUEUE_LOCK = new Object();

		/**
		 * Queue the last document that is to be displayed.
		 */
		private SVGDocument queuedDocument = null;

		/**
		 * Renew the document if the canvas is ready for modification, otherwise
		 * queue the change document
		 * 
		 * @param doc
		 *            The new Document
		 */
		public void renewDocument(SVGDocument doc) {

			if (isReadyForModification) {
				updateDocument(doc);
			} else {
				synchronized (QUEUE_LOCK) {
					queuedDocument = doc;
				}
			}
		}

		/**
		 * Renew the document by replacing the root node with the one of the new
		 * document
		 * 
		 * @param doc
		 *            The new document
		 */
		private void updateDocument(final SVGDocument doc) {
			getUpdateManager().getUpdateRunnableQueue().invokeLater(
					new Runnable() {
						@Override
						public void run() {
							// Get the root tags of the documents
							Node oldRoot = getSVGDocument().getFirstChild();
							Node newRoot = doc.getFirstChild();

							// Make the new node suitable for the old
							// document
							newRoot = getSVGDocument()
									.importNode(newRoot, true);

							// Replace the nodes
							getSVGDocument().replaceChild(newRoot, oldRoot);
						}
					});
		}

		// PAINTING ///////////////////////////////////////////////////////////

		/**
		 * First paint a grid, then let the super-implementation do its thing
		 * (draw the SVG)
		 */
		@Override
		public void paintComponent(Graphics g) {

			// Apply scrolling to the grid
			AffineTransform transform = getViewBoxTransform();

			g.setColor(GRID_COLOR);

			for (double w = transform.getTranslateX() % Grid.GRID_WIDTH; w < getWidth(); w += Grid.GRID_WIDTH)
				g.drawLine((int) w, 0, (int) w, getHeight());

			for (double h = transform.getTranslateY() % Grid.GRID_HEIGHT; h < getHeight(); h += Grid.GRID_HEIGHT)
				g.drawLine(0, (int) h, getWidth(), (int) h);

			super.paintComponent(g);
		}

		// LISTENING //////////////////////////////////////////////////////////

		/**
		 * Listen to the renderer to know when we can modify the document. If
		 * modification becomes available, apply the currently queued document
		 * immediatly
		 */
		@Override
		public void gvtRenderingCompleted(GVTTreeRendererEvent e) {

			synchronized (QUEUE_LOCK) {
				if (queuedDocument != null) {
					updateDocument(queuedDocument);
					queuedDocument = null;
				}
			}
			isReadyForModification = true;
		}

		/**
		 * Starting to render? Then we do not want new documents to interfere
		 * with this process
		 */
		@Override
		public void gvtRenderingPrepare(GVTTreeRendererEvent e) {
			isReadyForModification = false;
		}

		@Override
		public void gvtRenderingStarted(GVTTreeRendererEvent e) {
		}

		@Override
		public void gvtRenderingCancelled(GVTTreeRendererEvent e) {
		}

		@Override
		public void gvtRenderingFailed(GVTTreeRendererEvent e) {
		}

	}

	// ///////////////////////////////////////////////////////////////
	// WRITING OUT
	// ///////////////////////////////////////////////////////////////

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.view.graph.IGraph#exportToFile()
	 */
	@Override
	public void exportToFile() throws Exception {

		String filename = getFileName();

		try {
			FileWriter writer = new FileWriter(filename);
			String docString = BatikUtil.transformToString(BatikUtil
					.createDocument(mShapeMapper, false));
			// log.debug("Writing to file\n" + docString);
			writer.write(docString);
			writer.close();
			log.debug(Lang.text("written_to", filename));
		} catch (Exception e) {
			throw new Exception(Lang.text("err_written_to", filename), e);
		}

	}

	// ///////////////////////////////////////////////////////////////
	// LOG
	// ///////////////////////////////////////////////////////////////

	private final static Logger log = Logger.getLogger(BatikGraph.class);

}
