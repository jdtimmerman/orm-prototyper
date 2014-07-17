package nl.ru.jtimmerm.view.gui.dialogue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import nl.ru.jtimmerm.Lang;
import nl.ru.jtimmerm.orm.types.IObjectType;
import nl.ru.jtimmerm.view.IViewListener;

/**
 * Provide example data to an object or value type
 * 
 * @author joost
 * 
 */
public class SampleQuestion extends Question implements ActionListener {

	// ////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Holds the current selected object type
	 */
	private IObjectType mSelection;

	/**
	 * Create a new example data dialog
	 * 
	 * @param ql
	 *            View listener
	 * @param selected
	 *            The current selected type
	 */
	public SampleQuestion(IViewListener ql, IObjectType selected) {
		super(Lang.text("sample"), ql);
		mSelection = selected;

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
		p.add(new JLabel(Lang.text("sample_provide")));

		mTable = createTable(mSelection.getSamples());
		p.add(mTable, "growx");

		// Add submit button
		mButton = createButton();
		p.add(mButton, "align right");

		return p;
	}
	
	/*
	 * (non-Javadoc)
	 * @see nl.ru.jtimmerm.view.gui.dialogue.Question#inputRequired()
	 */
	@Override
	protected boolean inputRequired() {
		// hide if already completed
		if(mSelection.getSamples().size() >= 2)
			return false;
		
		return super.inputRequired();
	}

	// ////////////////////////////////////////////////////////////////////////
	// EDITABLE TABLE
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Holds the table for sample data
	 */
	private JTable mTable;

	/**
	 * Create a table with one column to show and enter the sample data
	 * 
	 * @param sampleList
	 *            A Set of the existing sample data. May be empty
	 * @return A JTable
	 */
	private JTable createTable(Set<String> sampleList) {

		String[] samples = new String[0];
		samples = sampleList.toArray(samples);
		JTable list = new JTable(samples.length + 1, 1);

		for (int i = 0; i < samples.length; i++)
			list.setValueAt(samples[i], i, 0);

		// Add a listener to add empty rows if needed
		list.getModel().addTableModelListener(mTableChangeListener);

		return list;
	}

	/**
	 * A table change listener that appends a new row if all rows are full
	 */
	private TableModelListener mTableChangeListener = new TableModelListener() {

		@Override
		public void tableChanged(TableModelEvent arg0) {

			if (arg0.getType() == TableModelEvent.UPDATE) {
				// This method changes the table, causing a stack overflow
				// with the listener attached
				mTable.getModel().removeTableModelListener(this);

				// Only add a row if there are not empty ones
				if (getData(mTable).size() == mTable.getRowCount())
					((DefaultTableModel) mTable.getModel())
							.addRow(new String[] { "" });

				// Done, reattach listener
				mTable.getModel().addTableModelListener(this);

			}
		}
	};

	/**
	 * Get the data in the table (including rows that are currently being
	 * editted)
	 * 
	 * @param l
	 *            The (one-column) table
	 * @return A Set of strings
	 */
	private static Set<String> getData(JTable l) {

		// Stop editting (rows being editted appear to contain null
		if (l.getEditingRow() != -1)
			l.editCellAt(-1, -1);

		TableModel model = l.getModel();
		Set<String> data = new HashSet<String>();

		for (int i = 0; i < model.getRowCount(); i++) {
			String content = "";

			try {
				content = model.getValueAt(i, 0).toString();
			} catch (NullPointerException e) {
				// Ignore, consider the cell empty
			}

			if (!content.trim().isEmpty())
				data.add(content);
		}

		return data;
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
		return Lang.text("sample_help");
	}

	/**
	 * Holds the submit button
	 */
	private JButton mButton;

	/**
	 * 
	 * @return A submit button with listener
	 */
	private JButton createButton() {
		JButton button = new JButton(Lang.text("submit"));
		button.addActionListener(this);
		return button;
	}

	// ////////////////////////////////////////////////////////////////////////
	// ACTION
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Store the contents of the table as samples for the selected object
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		final Set<String> samples = getData(mTable);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				getListener().setSampleData(mSelection, samples);
			}
		});

	}

	// ////////////////////////////////////////////////////////////////////////

	private static final long serialVersionUID = -7413405539996929821L;
}
