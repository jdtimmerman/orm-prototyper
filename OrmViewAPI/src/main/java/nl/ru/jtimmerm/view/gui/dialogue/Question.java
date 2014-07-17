package nl.ru.jtimmerm.view.gui.dialogue;

import java.awt.Component;
import java.awt.Font;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import nl.ru.jtimmerm.Lang;
import nl.ru.jtimmerm.view.IViewListener;
import nl.ru.jtimmerm.view.gui.MainFrame;

/**
 * This class provides the container and title of the dialogs. The abstract
 * methods should be implemented by actual questions/dialogs
 * 
 * @author joost
 * 
 */
public abstract class Question extends JPanel {

	// ////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * The main layout of this panel
	 */
	private final LayoutManager layout = new MigLayout( //
			new LC().fillX().flowY().insets("0").gridGapY("0").hideMode(3), //
			new AC().fill().grow(), //
			new AC().gap(""));

	/**
	 * Prepares the question (style, header, helptext) except for adding the
	 * actual question since this might require locally stored data. Use
	 * <code>{@link #launchQuestion()}</code> at the end of local constructors.
	 * 
	 * @param title
	 * @param questionlistener
	 */
	public Question(String title, IViewListener listener) {
		mListener = listener;

		// Style
		setLayout(layout);
		setBorder(new LineBorder(SystemColor.textHighlight, 1));

		// Header
		mHelpButton = createHelpButton();
		mHeader = createHeader(title);
		add(mHeader);

		// Help
		mHelp = createHelp();
		mHelp.setBackground(SystemColor.info);
		mHelp.setVisible(false);
		add(mHelp, new CC().gapY("0", "0"));
	}

	/**
	 * This should be called after all the variables of a questions are set. The
	 * last statement in the constructor of the concrete questions.
	 */
	protected void launchQuestion() {

		// Question
		mQuestion = createQuestion();

		if (!inputRequired())
			toggleQuestionVisibility();

		add(mQuestion);

	}

	// ////////////////////////////////////////////////////////////////////////
	// QUESTION LISTENER
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Holds the view listener
	 */
	private IViewListener mListener;

	/**
	 * @return the view listener
	 */
	public IViewListener getListener() {
		return mListener;
	}

	// ////////////////////////////////////////////////////////////////////////
	// OVERRIDEABLE CONTENT
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * This panel contains the actual question content
	 */
	private JPanel mQuestion;

	/**
	 * @return A panel containing the actual content of the question
	 */
	protected abstract JPanel createQuestion();

	/**
	 * @return The help text
	 */
	protected abstract String createHelpText();

	// ////////////////////////////////////////////////////////////////////////
	// HEADER
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * The top-bar of a question
	 */
	private JPanel mHeader;

	/**
	 * The button that toggles display of the help-text
	 */
	private JButton mHelpButton;

	/**
	 * Create a title and help button. Clicking the title toggles the display of
	 * the question
	 * 
	 * @param title
	 * @return
	 */
	private JPanel createHeader(String title) {

		// Make title pretty
		title = ucfirst(title);

		// Create the panel
		JPanel p = new JPanel();
		p.setBackground(SystemColor.textHighlight);
		p.setLayout(new MigLayout(new LC().fillX().hideMode(3), //
				new AC().grow().fill().gap("rel").shrink()));

		// Add a button to toggle the question
		JButton l = new JButton(title);
		removeStyle(l);
		l.setHorizontalAlignment(SwingConstants.LEFT);
		l.addActionListener(mQuestionToggle);
		p.add(l, new CC().grow());

		// Add a button to toggle the help
		p.add(mHelpButton, new CC().width("20px"));
		return p;

	}

	/**
	 * Whether we still need input for this question (or when we don't know if
	 * we still need input). This is used to hide completed tasks.
	 * 
	 * @return
	 */
	protected boolean inputRequired() {
		return true;
	}

	/**
	 * Toggles the visibility of question and help features, while keeping the
	 * header-bar
	 */
	private void toggleQuestionVisibility() {
		if (mQuestion.isVisible()) {
			mQuestion.setVisible(false);
			mHelp.setVisible(false);
			mHelpButton.setVisible(false);
		} else {
			mQuestion.setVisible(true);
			mHelpButton.setVisible(true);
		}
	}

	/**
	 * An actionlistener to toggle the display of the entire question
	 */
	protected ActionListener mQuestionToggle = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			toggleQuestionVisibility();
		}
	};

	/**
	 * Make the first character of the string an uppercase one
	 * 
	 * @param s
	 *            A string
	 * @return A string, with its first character always in uppercase
	 */
	private String ucfirst(String s) {
		char[] chars = s.toCharArray();
		chars[0] = Character.toUpperCase(chars[0]);
		return new String(chars);
	}

	/**
	 * Create a button that toggles the display of the help function
	 * 
	 * @return A help button
	 */
	private JButton createHelpButton() {
		// Button toggle question
		JButton help = new JButton(Lang.text("q_help_short"));
		help.setToolTipText(Lang.text("q_help_tooltip"));
		removeStyle(help);
		help.addActionListener(mHelpToggle);
		return help;
	}

	/**
	 * An actionlistener to toggle display of the help text
	 */
	private ActionListener mHelpToggle = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			mHelp.setVisible(!mHelp.isVisible());
		}
	};

	// ////////////////////////////////////////////////////////////////////////
	// HELP PANEL
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Holds a panel with help text
	 */
	private JPanel mHelp;

	/**
	 * @return A panel containing the help text
	 */
	private JPanel createHelp() {
		JPanel p = new JPanel();
		JLabel l = new JLabel(createHelpText());
		l.setFont(l.getFont().deriveFont(Font.PLAIN, 11f));

		p.setLayout(new MigLayout(new LC().fillX().noGrid().flowY(), new AC()
				.grow().fill()));

		p.add(l);

		return p;
	}

	// ////////////////////////////////////////////////////////////////////////
	// STYLING
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * A layout that fills and makes stuff flow to south (auto wrap)
	 */
	protected final LayoutManager DEFAULT_QUESTION_LAYOUT = new MigLayout(
			new LC().fillX().noGrid().flowY());

	/**
	 * A layout that makes sure the contents are filled, but has not other
	 * constraints
	 */
	protected final LayoutManager FILL_LAYOUT = new MigLayout(new LC().fillX(),
			new AC().fill());

	/**
	 * Remove the style of a button for use in the header
	 * 
	 * @param b
	 *            The button to have its style removed
	 */
	private static void removeStyle(JButton b) {
		b.setForeground(SystemColor.textHighlightText);
		b.setMargin(new Insets(0, 0, 0, 0));
		b.setBorderPainted(false);
		b.setContentAreaFilled(false);
		b.setFocusPainted(false);
	}

	protected MainFrame findMainFrame() {
		Component c = getParent();
		while (c.getParent() != null)
			c = c.getParent();

		if (c instanceof MainFrame)
			return (MainFrame) c;

		return null;
	}

	// ////////////////////////////////////////////////////////////////////////

	private static final long serialVersionUID = 5664804961296433999L;

}
