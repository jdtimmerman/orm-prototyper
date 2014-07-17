package nl.ru.jtimmerm.view.gui;

import java.util.Collection;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import net.miginfocom.layout.AC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import nl.ru.jtimmerm.orm.types.IFactType;
import nl.ru.jtimmerm.orm.types.IObjectType;
import nl.ru.jtimmerm.orm.types.IType;
import nl.ru.jtimmerm.view.IComponentContainer;
import nl.ru.jtimmerm.view.IViewListener;
import nl.ru.jtimmerm.view.IViewObservable;
import nl.ru.jtimmerm.view.gui.dialogue.ElementaryFactQuestion;
import nl.ru.jtimmerm.view.gui.dialogue.IsValueTypeQuestion;
import nl.ru.jtimmerm.view.gui.dialogue.SampleQuestion;
import nl.ru.jtimmerm.view.gui.dialogue.TotalityConstraintQuestion;
import nl.ru.jtimmerm.view.gui.dialogue.UnicityConstraintQuestion;

import org.apache.log4j.Logger;

/**
 * The dialog panel is a {@link JPanel} that holds the various dialogs. It
 * provides styling and methods to change the dialogs based on the selection
 * context
 * 
 * @author joost
 * 
 */
public class DialogPanel extends JPanel implements IComponentContainer,
		IViewObservable {

	// ////////////////////////////////////////////////////////////////////////
	// CONSTRUCT
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Holds the view listener
	 */
	private IViewListener mListener;

	/**
	 * Create a new panel
	 */
	public DialogPanel() {

		// Style
		setLayout(new MigLayout(new LC().fillX().flowY().insets("0"), new AC()
				.grow().fill()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.view.IComponentContainer#getComponent()
	 */
	@Override
	public JComponent getComponent() {
		return this;
	}

	// ////////////////////////////////////////////////////////////////////////
	// CONTEXT
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Delegates the change in context (and this the reapplying dialogs) to the
	 * swing worker thread
	 * 
	 * @param selected
	 *            The current selection
	 * @param relations
	 *            The types that have a relation with the current selection
	 */
	public void newContext(final IType selected,
			final Collection<? extends IType> relations) {
		log.trace("Changing context: " + selected + " - " + relations);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				changeQuestions(selected, relations);
			}
		});
	}

	/**
	 * Update the questions visible on this panel to questions relevant to the
	 * current selection
	 * 
	 * @param selected
	 *            The current selection
	 * @param relations
	 *            The types that have a relation with the current selection
	 */
	private void changeQuestions(IType selected,
			Collection<? extends IType> relations) {
		removeAll();

		if (selected == null) {
			//
			add(new ElementaryFactQuestion(mListener));
		} else if (selected instanceof IObjectType) {
			//
			IObjectType o = (IObjectType) selected;
			add(new TotalityConstraintQuestion(mListener, o, relations));
			add(new IsValueTypeQuestion(mListener, o));
			add(new SampleQuestion(mListener, o));
		} else if (selected instanceof IFactType) {
			//
			IFactType f = (IFactType) selected;
//			add(new AlterFactQuestion(mListener, f));
			add(new UnicityConstraintQuestion(mListener, f));
		} else {
			log.debug("  Unknown context");
		}

		revalidate();
		repaint();
	}

	// ////////////////////////////////////////////////////////////////////////
	// ADD VIEW LISTENERS
	// ////////////////////////////////////////////////////////////////////////

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.ru.jtimmerm.view.IViewObservable#setViewListener(nl.ru.jtimmerm.view
	 * .IViewListener)
	 */
	@Override
	public void setViewListener(IViewListener l) {
		mListener = l;
		newContext(null, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.view.IViewObservable#removeViewListener()
	 */
	@Override
	public void removeViewListener() {
		mListener = null;
	}

	// ///////////////////////////////////////////////////////////////
	// LOG
	// ///////////////////////////////////////////////////////////////

	private final static Logger log = Logger.getLogger(DialogPanel.class);

	// ///////////////////////////////////////////////////////////////

	private static final long serialVersionUID = 70185217166646355L;

}
