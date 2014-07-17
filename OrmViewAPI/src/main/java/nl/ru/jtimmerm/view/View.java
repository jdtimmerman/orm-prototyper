package nl.ru.jtimmerm.view;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

import nl.ru.jtimmerm.Lang;
import nl.ru.jtimmerm.orm.IModel;
import nl.ru.jtimmerm.orm.OrmException;
import nl.ru.jtimmerm.orm.types.IType;
import nl.ru.jtimmerm.view.graph.IGraph;
import nl.ru.jtimmerm.view.gui.DialogPanel;
import nl.ru.jtimmerm.view.gui.ListPanel;
import nl.ru.jtimmerm.view.gui.MainFrame;

import org.apache.log4j.Logger;

/**
 * The view represents the model and provides interaction controls
 * 
 * @author joost
 */
public abstract class View implements IView {

	// ////////////////////////////////////////////////////////////////////////
	// CONSTRUCT
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Holds the model
	 */
	private IModel mModel;

	/**
	 * Holds the outer frame
	 */
	private MainFrame mFrame;

	/**
	 * Initiate the view with the given model
	 * 
	 * @param model
	 *            The model this view will represent
	 */
	public View(IModel model) {

		// Set the model and listener
		mModel = model;

		// Load the panes
		mInputPane = getInputPanel();
		mListPane = getListPanel();
		mGraph = getGraphPanel();

		// Listen to the model
		mModel.addModelListener(mListPane);
		Selection.getSingleton().addSelectListener(this);

		// Create the frame
		mFrame = new MainFrame(mGraph.getComponent(), mListPane.getComponent(),
				mInputPane.getComponent());

		KeyboardFocusManager manager = KeyboardFocusManager
				.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(new MyDispatcher());

		// Empty selection for input panel
		Selection.getSingleton().select(null);

		// And go!
		mFrame.launchFrame();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.view.IView#getGraph()
	 */
	@Override
	public IGraph getGraph() {
		return mGraph;
	}

	// ////////////////////////////////////////////////////////////////////////
	// PANELS
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Holds the input (dialog) panel
	 */
	private DialogPanel mInputPane;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.view.IView#getInputPanel()
	 */
	@Override
	public DialogPanel getInputPanel() {
		return new DialogPanel();
	}

	/**
	 * Holds the list panel
	 */
	private ListPanel mListPane;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.view.IView#getListPanel()
	 */
	@Override
	public ListPanel getListPanel() {
		// TODO remove model dependancy
		return new ListPanel(mModel);
	}

	/**
	 * Holds the graph
	 */
	private IGraph mGraph;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.view.IView#getGraphPanel()
	 */
	@Override
	public abstract IGraph getGraphPanel();

	// ////////////////////////////////////////////////////////////////////////
	// KEY INTERACTION
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * This dispatcher catches delete keys, used to ... delete types
	 * 
	 * @author joost
	 * 
	 */
	private class MyDispatcher implements KeyEventDispatcher {
		@Override
		public boolean dispatchKeyEvent(KeyEvent e) {
			if (e.getID() == KeyEvent.KEY_PRESSED) {
				if (e.getKeyCode() == KeyEvent.VK_DELETE)
					if (Selection.getSingleton().hasSelectedShape())
						mViewListener.remove(Selection.getSingleton()
								.getSelectedShape());
			}
			// else if (e.getID() == KeyEvent.KEY_RELEASED) {
			// } else if (e.getID() == KeyEvent.KEY_TYPED) {
			// }
			return false;
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	// UPDATING
	// ////////////////////////////////////////////////////////////////////////

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.orm.IModelListener#modelChanged()
	 */
	@Override
	public void modelChanged() {
		log.trace(Lang.text("model_changed"));
		updateTypes();
		updateInputPanel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.view.ISelectListener#selectionChanged()
	 */
	@Override
	public void selectionChanged() {
		log.trace(Lang.text("selection_changed"));
		updateTypes();
		updateInputPanel();
	}

	/**
	 * Feed the updated types to the shapemapper
	 */
	private void updateTypes() {
		try {
			mGraph.getShapeMapper().setTypes(mModel.getTypes());
		} catch (NotHandledException e) {
			log.warn(Lang.text("not_handled")); // TODO Handle this?
		}
		mGraph.refreshGraph();
	}

	/**
	 * Provide new context for the input panel
	 */
	private void updateInputPanel() {

		IType selection = Selection.getSingleton().getSelectedShape();

		if (selection == null)
			mInputPane.newContext(null, null);
		else
			try {
				mInputPane.newContext(selection,
						mModel.getRelatingTypes(selection));
			} catch (OrmException e) {
				log.warn(e); // TODO proper error warning
			}
	}

	// ////////////////////////////////////////////////////////////////////////
	// LISTENING
	// ////////////////////////////////////////////////////////////////////////

	// ////////////////////////////////////////////////////////////////////////
	// VIEW LISTENERS

	/**
	 * Holds view listeners
	 */
	private IViewListener mViewListener = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.ru.jtimmerm.view.IViewObservable#setViewListener(nl.ru.jtimmerm.view
	 * .IViewListener)
	 */
	@Override
	public void setViewListener(IViewListener mcl) {
		mViewListener = mcl;
		mInputPane.setViewListener(mcl);
		mFrame.setViewListener(mcl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.view.IViewObservable#removeViewListener()
	 */
	@Override
	public void removeViewListener() {
		mViewListener = null;
		mInputPane.removeViewListener();
		mFrame.removeViewListener();
	}

	// ///////////////////////////////////////////////////////////////
	// APPLICATION
	// ///////////////////////////////////////////////////////////////

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.view.IView#showError(java.lang.String,
	 * java.lang.String, int)
	 */
	@Override
	public void showError(String title, String text, int type) {
		mFrame.showDialog(title, text, type);
	}

	// ///////////////////////////////////////////////////////////////
	// LOG
	// ///////////////////////////////////////////////////////////////

	private static final Logger log = Logger.getLogger(View.class);

}
