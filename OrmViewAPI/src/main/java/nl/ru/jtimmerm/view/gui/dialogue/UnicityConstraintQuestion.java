package nl.ru.jtimmerm.view.gui.dialogue;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import nl.ru.jtimmerm.Lang;
import nl.ru.jtimmerm.orm.types.IFactType;
import nl.ru.jtimmerm.orm.types.IObjectType;
import nl.ru.jtimmerm.orm.types.MultiUnicityConstraint;
import nl.ru.jtimmerm.orm.types.UnicityConstraintFactory;
import nl.ru.jtimmerm.view.IViewListener;

import org.apache.log4j.Logger;

/**
 * Determine the unicity constraints by filling out a table
 * 
 * @author joost
 * 
 */
public class UnicityConstraintQuestion extends Question implements ActionListener {
	
	/**
	 * Holds the current selection
	 */
	private IFactType	mSelection;
	
	/**
	 * Create a new question
	 * 
	 * @param ql
	 *            The view listener
	 * @param selected
	 *            The current selection
	 */
	public UnicityConstraintQuestion(IViewListener ql, IFactType selected) {
		super(Lang.text("unicity"), ql);
		mSelection = selected;
		
		launchQuestion();
	}
	
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
		p.add(new JLabel(Lang.text("uc_is_unique")));
		
		mTable = createTable();
		
		JScrollPane js = new JScrollPane(mTable);
		Dimension d = new Dimension(p.getWidth(), (int) ((1.5 + mTable.getRowCount()) * mTable.getRowHeight()));
		js.setPreferredSize(d);
		p.add(js, "growx");
		
		// Add button
		mButton = createButton();
		p.add(mButton, "align right");
		
		return p;
	}
	
	@Override
	protected boolean inputRequired() {
		// Hide if already completed
		if (mSelection.hasUnicity()) 
			return false;
		
		return super.inputRequired();
	}
	
	// ////////////////////////////////////////////////////////////////////////
	// TABLE
	// ////////////////////////////////////////////////////////////////////////
	
	/**
	 * Holds the table
	 */
	private JTable	mTable;
	
	/**
	 * Create a table for determining a unicity constraint
	 * 
	 * TODO Handle case of only 1 role
	 * 
	 * @return
	 */
	private JTable createTable() {
		JTable table = new JTable(new UCTableModel(getData(), getColumns()));
		table.getColumnModel().getColumn(table.getColumnCount() - 1).setPreferredWidth(8);
		table.setDefaultRenderer(String.class, new UCTableRenderer());
		table.setFillsViewportHeight(false);
		return table;
	}
	
	/**
	 * This renderer colors the first row to indicate that it's not editable.
	 * 
	 * @author joost
	 */
	private static class UCTableRenderer extends DefaultTableCellRenderer {
		
		private static final long	serialVersionUID	= 1L;
		
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			
			if (row == 0)
				c.setBackground(Color.lightGray);
			else if (isSelected)
				c.setBackground(table.getSelectionBackground());
			else
				c.setBackground(Color.white);
			
			return c;
		}
		
	}
	
	/**
	 * A table model to disable editing of the first row and non-last columns
	 * and sets the cell-classes based on the proper content
	 * 
	 * @author joost
	 * 
	 */
	private static class UCTableModel extends DefaultTableModel {
		
		private static final long	serialVersionUID	= -7020486883016832962L;
		
		public UCTableModel(Object[][] data, String[] columns) {
			super(data, columns);
		}
		
		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return getValueAt(0, columnIndex).getClass();
		}
		
		@Override
		public boolean isCellEditable(int row, int column) {
			// Never edit first row
			if (row == 0)
				return false;
			
			// Only edit last cell
			if (column == getColumnCount() - 1)
				return true;
			else
				return false;
		}
		
	}
	
	/**
	 * Determine the content of the UC table based on sample content or object
	 * type names. Create unique rows and append whether this row is allowed or
	 * not
	 * 
	 * @return An array of data rows (as arrays)
	 */
	private Object[][] getData() {
		
		List<IObjectType> columns = mSelection.getObjectTypes();
		List<boolean[]> variants = UnicityConstraintFactory.getVariants(columns.size());
		
		if (log.isDebugEnabled()) {
			log.debug("Variants: ");
			for (boolean[] v : variants)
				log.debug(Arrays.toString(v));
		}
		
		Object[][] data = new Object[variants.size()][columns.size() + 1];
		
		int i = 0;
		for (boolean[] variant : variants) {
			for (int j = 0; j < columns.size(); j++)
				data[i][j] = UnicityDetermination.variantToString(variant[j], columns.get(j));
			
			// TODO: CHECK!
			data[i++][columns.size()] = mSelection.getUnicities().isAllowed(variant);
		}
		
		// Wait, the false-tuple should ('visually') always be allowed
		data[0][columns.size()] = new Boolean(true);
		
		if (log.isDebugEnabled()) {
			log.debug("Data for JTable: ");
			for (Object[] v : data)
				log.debug(Arrays.toString(v));
		}
		
		return data;
	}
	
	/**
	 * Determine the column titles based on the roles the fact type has and add
	 * a column for 'possible or not'
	 * 
	 * @return An array with the column titles
	 */
	private String[] getColumns() {
		
		String[] columns = new String[mSelection.getObjectTypes().size() + 1];
		
		int i = 0;
		for (IObjectType o : mSelection.getObjectTypes())
			columns[i++] = o.getContent();
		
		columns[i++] = Lang.text("uc_possible");
		
		return columns;
		
	}
	
	// ////////////////////////////////////////////////////////////////////////
	// CREATE PARTS
	// ////////////////////////////////////////////////////////////////////////
	
	/**
	 * Holds the submit button
	 */
	private JButton	mButton;
	
	/**
	 * @return A submit button with action listener
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
		return Lang.text("uc_help");
	}
	
	/**
	 * Called when the submit button is pressed. Forwards the updating of the
	 * unicity constraints to the controller in a new thread
	 * 
	 * TODO Am i not forwarding to the swingthread?
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					MultiUnicityConstraint muc = UnicityDetermination.calculateUnicity(mTable, mSelection.getObjectTypes());
					getListener().setUnicityConstraints(mSelection, muc);
					
				} catch (IllegalArgumentException e) {
					findMainFrame().showDialog(Lang.text("err_update_constraint"), e.getMessage(), Dialog.ERROR);
				}
			}
		});
	}
	
	// ////////////////////////////////////////////////////////////////////////
	// LOG
	// ////////////////////////////////////////////////////////////////////////
	
	private static Logger		log					= Logger.getLogger(UnicityConstraintQuestion.class);
	
	// ////////////////////////////////////////////////////////////////////////
	
	private static final long	serialVersionUID	= -7413405539996929821L;
}
