package nl.ru.jtimmerm.view.gui.dialogue;

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.layout.CC;
import nl.ru.jtimmerm.Lang;
import nl.ru.jtimmerm.Settings;
import nl.ru.jtimmerm.orm.types.IFactType;
import nl.ru.jtimmerm.orm.types.IObjectType;
import nl.ru.jtimmerm.view.IViewListener;
import nl.ru.jtimmerm.view.gui.WrapLayout;

/**
 * Allow the user to change the content of a fact type. Current only changing
 * the verbalization is possible
 * 
 * TODO Make something proper for renaming an existing facttype
 * 
 * @author joost
 * 
 */
public class AlterFactQuestion extends Question implements ActionListener {

	/**
	 * The wildcard used in verbalizations
	 */
	private static final String WILDCARD = Settings.string("ft_wildcard");

	// ////////////////////////////////////////////////////////////////////////
	// CONSTRUCT
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * The current selection
	 */
	private IFactType mSelection;

	/**
	 * Create a question with the given listener and selection
	 * 
	 * @param l
	 *            View listener
	 * @param selection
	 *            Current selection
	 */
	public AlterFactQuestion(IViewListener l, IFactType selection) {
		super(Lang.text("elementary_fact"), l);
		mSelection = selection;
		launchQuestion();
	}

	// ////////////////////////////////////////////////////////////////////////
	// CONTENT
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Holds the components (JTextArea and JLabels) using in this question
	 */
	private ArrayList<JComponent> mComponents = new ArrayList<JComponent>();

	/**
	 * A submit button
	 */
	private JButton mButton;

	private final LayoutManager WRAP_LAYOUT = new WrapLayout(WrapLayout.LEFT);

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.view.gui.dialogue.Question#createQuestion()
	 */
	@Override
	protected JPanel createQuestion() {

		mButton = createButton();
		mComponents = getComponents(mSelection);

		JPanel p = new JPanel(DEFAULT_QUESTION_LAYOUT);
		p.add(new JLabel(Lang.text("ef_alter_fact")));

		JPanel pForm = new JPanel(WRAP_LAYOUT);
		for (JComponent c : mComponents)
			pForm.add(c); // min:pref:max
		p.add(pForm , new CC().growX());

		p.add(mButton, new CC().alignX("right"));
		return p;
	}

	@Override
	protected boolean inputRequired() {
		return false;
	}

	/**
	 * Create the textfields for parts of the verbalization between objects and
	 * labels for the objects. In the correct order
	 * 
	 * @param type
	 *            The fact type for which edit components need to be created
	 * @return A list of JTextFields and JLabels
	 */
	private static ArrayList<JComponent> getComponents(IFactType type) {

		// Settings and return value
		ArrayList<JComponent> components = new ArrayList<JComponent>();

		// Get some info from the type
		Iterator<IObjectType> objs = type.getObjectTypes().iterator();
		Scanner s = new Scanner(type.getVerbalization());

		StringBuilder sentencePart = new StringBuilder(type.getVerbalization()
				.length());

		JTextField suffix = new JTextField();
		Dimension d = new Dimension(40, suffix.getPreferredSize().height);
		suffix.setPreferredSize(d);
		
		while (s.hasNext()) {
			String next = s.next();
			if (next.equals(WILDCARD)) {
				// If we encounter a wildcard, add the partial string and the
				// object to the list
				JTextField jt = new JTextField(sentencePart.toString().trim());
				
				if( jt.getText().isEmpty())
					jt.setPreferredSize(d);
				
				components.add(jt);
				components.add(new JLabel(objs.next().toString()));
				sentencePart.setLength(0);
			} else {
				// Add texts to the partial string
				sentencePart.append(" ");
				sentencePart.append(next.trim());
			}
		}
		s.close();

		// Append an empty field to appending text
		components.add(suffix);

		return components;
	}

	// ////////////////////////////////////////////////////////////////////////
	// CREATE PARTS
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * @return A submit button with actionlistener
	 */
	private JButton createButton() {
		JButton button = new JButton(Lang.text("submit"));
		button.addActionListener(this);

		return button;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.view.gui.dialogue.Question#createHelpText()
	 */
	@Override
	protected String createHelpText() {
		return Lang.text("ef_help");
	}

	// ////////////////////////////////////////////////////////////////////////
	// ACTIONS
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Called when the submit button is pressed. Collects the contents from the
	 * textfields and create a new verbalization
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		ArrayList<String> objects = new ArrayList<String>();
		StringBuilder verbalization = new StringBuilder();

		int i = 1;
		for (JComponent c : mComponents) {
			if (c instanceof JTextField) {
				JTextField t = (JTextField) c;
				verbalization.append(t.getText().trim());
				verbalization.append(" ");

				if (i != mComponents.size()) {
					verbalization.append(WILDCARD);
					verbalization.append(" ");
				}
			} else if (c instanceof JLabel) {
				JLabel l = (JLabel) c;
				objects.add(l.getText());
			}

			i++;
		}

		getListener().updateFactType(mSelection, verbalization.toString(),
				objects);

	}

	// ////////////////////////////////////////////////////////////////////////

	private static final long serialVersionUID = -7413405539996929821L;

}
