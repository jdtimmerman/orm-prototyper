package nl.ru.jtimmerm.view.gui.dialogue;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import javax.swing.JTable;

import nl.ru.jtimmerm.Lang;
import nl.ru.jtimmerm.orm.types.IObjectType;
import nl.ru.jtimmerm.orm.types.MultiUnicityConstraint;
import nl.ru.jtimmerm.orm.types.UnicityConstraintFactory;

import org.apache.log4j.Logger;

/**
 * This class provide a method to determine the unicity, based on which rows of
 * variants are allowed. The main use is via
 * {@link #determineUnicitiy(int, boolean[])} but some other methods are left
 * public as they can be used as utility methods.
 * 
 * @author Joost Timmerman
 * 
 */
public class UnicityDetermination {
	
	static String variantToString(boolean var, IObjectType obj) {
		
		Object[] samples = obj.getSamples().toArray();
		
		// Set sample data
		if (var && samples.length > 0)
			return samples[0].toString();
		else if (!var && samples.length > 1)
			return samples[1].toString();
		else
			// Or append the name with A or B
			return obj.getContent() + (var ? "B" : "A");
		
	}
	
	static boolean stringToVariant(String var, IObjectType obj) {
		Object[] samples = obj.getSamples().toArray();
		
		if (samples.length > 0 && var.equals(samples[0]))
			return true;
		else if (samples.length > 1 && var.equals(samples[1]))
			return false;
		else {
			return var.endsWith("B");
		}
	}
	
	/**
	 * Retrieves value of the judgement column and use this to calculate the
	 * unicity constraint.
	 * 
	 * @return A Set of unicity constraints
	 */
	static MultiUnicityConstraint calculateUnicity(JTable table, List<IObjectType> columns) {
		int columnCount = table.getColumnCount() - 1;
		int rowCount = table.getRowCount();
		
		Collection<MultiUnicityConstraint> allPossibleConstraints = UnicityConstraintFactory.getPossibleConstraints(columnCount);
		logConstraintCollection("Pre allPossibleConstraints", allPossibleConstraints);
		
		TreeSet<MultiUnicityConstraint> invalidConstraints = new TreeSet<MultiUnicityConstraint>();
		
		for (int rowIndex = 1; rowIndex < rowCount; rowIndex++) {
			boolean accepted = (Boolean) table.getValueAt(rowIndex, columnCount);
			boolean[] tuple = getRow(table, rowIndex, columns);
			
			printTuple(tuple, accepted);
			
			for (MultiUnicityConstraint muc : allPossibleConstraints) {
				if (accepted && !muc.isAllowed(tuple)) {
					logRejectReason(muc, true);
					invalidConstraints.add(muc);
				} else if (!accepted && muc.isAllowed(tuple)) {
					logRejectReason(muc, false);
					invalidConstraints.add(muc);
				}
			}
		}
		
		if (allPossibleConstraints.size() == invalidConstraints.size())
			throw new IllegalArgumentException(Lang.text("err_no_contraints_poss"));
		
		allPossibleConstraints.removeAll(invalidConstraints);
		logConstraintCollection("Post allPossibleConstraints", allPossibleConstraints);
		
		MultiUnicityConstraint muc = allPossibleConstraints.iterator().next();
		if (log.isDebugEnabled()) {
			log.debug("Picking:\n" + muc.toAsciiString());
		}
		
		return muc;
		
	}
	
	private static void logRejectReason(MultiUnicityConstraint muc, boolean rejectAccepted) {
		if (!log.isDebugEnabled())
			return;
		
		if (rejectAccepted)
			log.debug("  MUC invalid, it rejects an allowed tuple" + muc.toAsciiString());
		else
			log.debug("  MUC invalid, it accepts a disallowed tuple" + muc.toAsciiString());
	}
	
	private static void printTuple(boolean[] tuple, boolean accepted) {
		if (!log.isDebugEnabled())
			return;
		
		StringBuilder sb = new StringBuilder();
		sb.append(Arrays.toString(tuple));
		sb.append(" => ");
		sb.append(accepted);
		log.debug(sb.toString());
	}
	
	private static void logConstraintCollection(String txt, Collection<MultiUnicityConstraint> c) {
		if (!log.isDebugEnabled())
			return;
		
		StringBuilder sb = new StringBuilder();
		sb.append(txt);
		sb.append(" - ");
		sb.append(c.size());
		sb.append(" constraints");
		for (MultiUnicityConstraint muc : c) {
			sb.append("\n");
			sb.append(muc.toAsciiString());
		}
		log.debug(sb.toString());
		
	}
	
	private static boolean[] getRow(JTable table, int rowIndex, List<IObjectType> columns) {
		int columnCount = table.getColumnCount() - 1;
		boolean[] row = new boolean[columnCount];
		
		for (int i = 0; i < columnCount; i++)
			row[i] = stringToVariant((String) table.getValueAt(rowIndex, i), columns.get(i));
		
		return row;
	}
	
	private static Logger	log	= Logger.getLogger(UnicityDetermination.class);
	
}
