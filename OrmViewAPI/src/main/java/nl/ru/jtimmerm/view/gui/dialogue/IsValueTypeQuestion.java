package nl.ru.jtimmerm.view.gui.dialogue;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import nl.ru.jtimmerm.Lang;
import nl.ru.jtimmerm.orm.types.IObjectType;
import nl.ru.jtimmerm.view.IViewListener;

import org.apache.log4j.Logger;

public class IsValueTypeQuestion extends Question {

	private static final long serialVersionUID = -7413405539996929821L;

	// ////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR
	// ////////////////////////////////////////////////////////////////////////

	private IObjectType mSelection;

	public IsValueTypeQuestion(IViewListener ql, IObjectType selected) {
		super(Lang.text("valuetype"), ql);
		mSelection = selected;

		launchQuestion();
	}

	// ////////////////////////////////////////////////////////////////////////
	// CREATE QUESTION
	// ////////////////////////////////////////////////////////////////////////

	@Override
	protected JPanel createQuestion() {

		// Setup panel
		JPanel p = new JPanel();
		p.setLayout(FILL_LAYOUT);

		p.add(new JLabel(Lang.text("vt_isValueType")), "push");

		JCheckBox c = new JCheckBox(new CheckboxCheck(mSelection));
		c.setSelected(mSelection.isValueType());
		p.add(c, "align right, wrap");

		return p;
	}

	// ////////////////////////////////////////////////////////////////////////
	// ACTION
	// ////////////////////////////////////////////////////////////////////////

	private class CheckboxCheck extends AbstractAction {

		private static final long serialVersionUID = 6294435410470426576L;

		private IObjectType mObject;

		public CheckboxCheck(IObjectType o) {
			super();
			mObject = o;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			boolean selected = ((JCheckBox) e.getSource()).isSelected();
			getListener().setValueType(mObject, selected);
		}

	}

	// ////////////////////////////////////////////////////////////////////////
	// CREATE PARTS
	// ////////////////////////////////////////////////////////////////////////

	@Override
	protected String createHelpText() {
		return Lang.text("vt_help");
	}

	// ////////////////////////////////////////////////////////////////////////
	// LOG
	// ////////////////////////////////////////////////////////////////////////

	protected static Logger log = Logger.getLogger(IsValueTypeQuestion.class);

}
