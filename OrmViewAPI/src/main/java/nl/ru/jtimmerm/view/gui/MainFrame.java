package nl.ru.jtimmerm.view.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import nl.ru.jtimmerm.Lang;
import nl.ru.jtimmerm.view.IViewListener;
import nl.ru.jtimmerm.view.IViewObservable;

import org.apache.log4j.Logger;

/**
 * The frame holds all component (list, graph, dialogs)
 * 
 * @author joost
 * 
 */
public class MainFrame extends JFrame implements IViewObservable {

	// ////////////////////////////////////////////////////////////////////////
	// PANES
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Constructor, populate and show the view
	 * 
	 * @param graphpanel
	 * @param sidepanel
	 * @param inputpanel
	 */
	public MainFrame(Component graphpanel, Component sidepanel,
			Component inputpanel) {
		super(Lang.text("application_title"));

		// Style
		setPreferredSize(new Dimension(DEFAULT_W, DEFAULT_H));

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			log.info(e.getMessage());
		}

		// Menu
		setJMenuBar(createMenu());

		// Create the tabbedpanel
		JTabbedPane tabs = new JTabbedPane();
		tabs.setBorder(new EmptyBorder(0, 0, 0, 0));
		tabs.addTab(Lang.text("dialog"), inputpanel);
		tabs.addTab(Lang.text("contents"), sidepanel);

		// Add panels to split
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		split.setLeftComponent(graphpanel);
		split.setRightComponent(tabs);
		split.setResizeWeight(0.75);

		add(split);
	}

	// ////////////////////////////////////////////////////////////////////////
	// DISPLAYING
	// ////////////////////////////////////////////////////////////////////////

	// TODO Move to settings
	private static final int DEFAULT_W = 800, DEFAULT_H = 600;

	/**
	 * Display the frame
	 */
	public void launchFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	/**
	 * @return A (the) menu
	 */
	private JMenuBar createMenu() {
		JMenuBar bar = new JMenuBar();
		JMenu menu;

		// File-menu
		menu = new JMenu(Lang.text("file"));
		menu.add(new JMenuItem(ACTION_EXPORTSVG));
		bar.add(menu);

		// Other menu
		// menu = new JMenu("X");
		// ...
		// bar.add(menu);

		return bar;
	}

	// ////////////////////////////////////////////////////////////////////////
	// ACTIONS

	/**
	 * A menu action representing the "export to svg" option
	 */
	public final Action ACTION_EXPORTSVG = new AbstractAction(Lang.text(
			"export_file", "SVG")) {

		private static final long serialVersionUID = 2060582772727996113L;

		@Override
		public void actionPerformed(ActionEvent e) {
			mListener.exportFile();
		}
	};

	/**
	 * Show an error message
	 * 
	 * @param title
	 *            The title of the messagebox
	 * @param e
	 *            The cause of the error
	 */
	public void showError(String title, Exception e) {
		showDialog(title, e.getMessage(), JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Show a dialog window
	 * 
	 * @param title
	 *            The title of the messagebox
	 * @param message
	 *            The message
	 * @param type
	 *            The type of the dialog (see {@link JOptionPane})
	 */
	public void showDialog(String title, String message, int type) {
		JOptionPane.showMessageDialog(this, message, title,
				JOptionPane.ERROR_MESSAGE);
	}

	// ////////////////////////////////////////////////////////////////////////
	// LISTENERS
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Holds a listener
	 */
	private IViewListener mListener = null;

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

	// ////////////////////////////////////////////////////////////////////////
	// LOG
	// ////////////////////////////////////////////////////////////////////////

	private static final Logger log = Logger.getLogger(MainFrame.class);

	//

	private static final long serialVersionUID = -3960564996456056827L;
}
