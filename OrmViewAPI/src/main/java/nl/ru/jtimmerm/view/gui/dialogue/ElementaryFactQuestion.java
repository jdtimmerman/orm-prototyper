package nl.ru.jtimmerm.view.gui.dialogue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import nl.ru.jtimmerm.Lang;
import nl.ru.jtimmerm.view.IViewListener;

/**
 * State a new elementary fact
 * 
 * @author joost
 * 
 */
public class ElementaryFactQuestion extends Question implements ActionListener {

	// ////////////////////////////////////////////////////////////////////////
	// CONSTRUCT
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Create a new question
	 * 
	 * @param l
	 *            View listener
	 */
	public ElementaryFactQuestion(IViewListener l) {
		super(Lang.text("elementary_fact"), l);
		launchQuestion();
	}

	// ////////////////////////////////////////////////////////////////////////
	// CONTENT
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Holds the textarea
	 */
	private JTextField mText;
	/**
	 * Holds the submit button
	 */
	private JButton mButton;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.view.gui.dialogue.Question#createQuestion()
	 */
	@Override
	protected JPanel createQuestion() {

		mText = createTextfield();
		mButton = createButton();

		JPanel p = new JPanel(DEFAULT_QUESTION_LAYOUT);
		p.add(new JLabel(Lang.text("ef_state_fact")));
		p.add(mText, "grow");
		p.add(mButton, "align right");

		return p;
	}

	// ////////////////////////////////////////////////////////////////////////
	// CREATE PARTS
	// ////////////////////////////////////////////////////////////////////////

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.view.gui.dialogue.Question#createHelpText()
	 */
	@Override
	protected String createHelpText() {
		return Lang.text("ef_help");
	}

	/**
	 * @return A textfield to enter an elementary fact in
	 */
	private JTextField createTextfield() {
		JTextField jt = new JTextField();
		jt.addActionListener(this);
		return jt;
	}

	/**
	 * @return A submit button with actionlistener
	 */
	private JButton createButton() {
		JButton button = new JButton(Lang.text("submit"));
		button.addActionListener(this);
		return button;
	}

	// ////////////////////////////////////////////////////////////////////////
	// ACTIONS
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Trigger updating the model and clear the textfield
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String s = mText.getText();
		getListener().analyzeSentence(s);
		mText.setText("");
	}

	// ////////////////////////////////////////////////////////////////////////
	
	private static final long serialVersionUID = -7413405539996929821L;
}
