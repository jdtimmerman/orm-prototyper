package nl.ru.jtimmerm.view.gui.dialogue;

import java.awt.event.ActionEvent;
import java.util.Collection;

import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import nl.ru.jtimmerm.Lang;
import nl.ru.jtimmerm.orm.types.IFactType;
import nl.ru.jtimmerm.orm.types.IObjectType;
import nl.ru.jtimmerm.orm.types.IType;
import nl.ru.jtimmerm.view.IViewListener;

/**
 * A totality constraint asks whether each value of the object or value type
 * should play a role in a fact type
 * 
 * @author joost
 * 
 */
public class TotalityConstraintQuestion extends Question {

	// ////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Holds the current selection
	 */
	private IObjectType mSelection;

	/**
	 * Holds the relations to the selection
	 */
	private Collection<? extends IType> mRelations;

	/**
	 * Create a new totality constraint question
	 * 
	 * @param ql
	 *            The view listener
	 * @param selected
	 *            The current selection
	 * @param relations
	 *            The relations of the current selection
	 */
	public TotalityConstraintQuestion(IViewListener ql, IObjectType selected,
			Collection<? extends IType> relations) {
		super(Lang.text("totality"), ql);
		mSelection = selected;
		mRelations = relations;

		launchQuestion();
	}

	// ////////////////////////////////////////////////////////////////////////
	// CREATE QUESTION
	// ////////////////////////////////////////////////////////////////////////

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.view.gui.dialogue.Question#createQuestion()
	 */
	@Override
	protected JPanel createQuestion() {

		// Setup panel
		JPanel p = new JPanel();
		p.setLayout(DEFAULT_QUESTION_LAYOUT);

		// Add content
		p.add(new JLabel(Lang.text("tc_is_mandatory")));

		try {
			p.add(askFactType(), "growx");
		} catch (IllegalArgumentException e) {
			p.add(new JLabel(e.getMessage()));
		}

		return p;
	}

	/**
	 * Adds checkboxes for each <code>FactType</code> with a relation to the
	 * <code>ObjectType</code> to ask whether it is a mandatory property
	 * 
	 * @return A panel with the verbalization of each fact type this object
	 *         plays a role in and a checkbox
	 * @throws IllegalArgumentException
	 *             When this object does not play a role in any fact type
	 */
	private JPanel askFactType() throws IllegalArgumentException {
		// This object has no relations
		if (mRelations == null)
			throw new IllegalArgumentException(Lang.text("tc_no_facts"));

		// Style
		JPanel p = new JPanel();
		p.setLayout(FILL_LAYOUT);
		p.setBorder(new EmptyBorder(0, 0, 0, 0));

		for (IType relation : mRelations) {
			// Only add facttypes
			if (!(relation instanceof IFactType))
				continue;

			IFactType facttype = (IFactType) relation;

			// Add question
			StringBuilder b = new StringBuilder();
			b.append("<html>");
			b.append(facttype.verbalize());
			b.append("</html>");
			p.add(new JLabel(b.toString()), "push");

			JCheckBox c = new JCheckBox(new CheckboxCheck(mSelection, facttype));
			c.setSelected(facttype.hasTotality(mSelection));
			p.add(c, "align right, wrap");
		}

		return p;
	}

	// ////////////////////////////////////////////////////////////////////////
	// CHECKBOX ACTION
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * An action representing a change in one of the checkboxes
	 * 
	 * @author joost
	 * 
	 */
	private class CheckboxCheck extends AbstractAction {

		private static final long serialVersionUID = 6294435410470426576L;

		/**
		 * The fact this object plays a role in
		 */
		private IFactType mFact;

		/**
		 * The object
		 */
		private IObjectType mObject;

		/**
		 * Create a new action
		 * 
		 * @param o
		 *            An object type
		 * @param f
		 *            A fact type the object plays a role in
		 */
		public CheckboxCheck(IObjectType o, IFactType f) {
			super();
			mObject = o;
			mFact = f;
		}

		/**
		 * Triggers updating the model
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			if (((JCheckBox) e.getSource()).isSelected())
				getListener().addTotalityConstraint(mObject, mFact);
			else
				getListener().removeTotalityConstraint(mObject, mFact);
		}

	}

	// ////////////////////////////////////////////////////////////////////////
	// CREATE PARTS
	// ////////////////////////////////////////////////////////////////////////

	/*
	 * (non-Javadoc)
	 * @see nl.ru.jtimmerm.view.gui.dialogue.Question#createHelpText()
	 */
	@Override
	protected String createHelpText() {
		return Lang.text("tc_help");
	}

	// ////////////////////////////////////////////////////////////////////////

	private static final long serialVersionUID = -7413405539996929821L;
}
