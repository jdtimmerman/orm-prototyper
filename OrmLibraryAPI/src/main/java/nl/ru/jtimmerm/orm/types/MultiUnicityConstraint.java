package nl.ru.jtimmerm.orm.types;

import java.util.Collection;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Holds a
 * 
 * @author joost
 * 
 */
public class MultiUnicityConstraint implements Comparable<MultiUnicityConstraint> {
	/**
	 * Width of the role for ascii printing. This includes the [] brackets
	 */
	public static final int					ROLE_WIDTH	= 4;
	
	/**
	 * Holds the constraints
	 */
	private SortedSet<UnicityConstraint>	constraints;
	
	/**
	 * Create a new {@link MultiUnicityConstraint} without any constraints
	 */
	public MultiUnicityConstraint() {
		constraints = new TreeSet<UnicityConstraint>();
	}
	
	/**
	 * Create a new {@link MultiUnicityConstraint} with the given constraints,
	 * and minimize to reduce overlapping constraints
	 */
	public MultiUnicityConstraint(Collection<UnicityConstraint> constraints) {
		this.constraints = new TreeSet<UnicityConstraint>(constraints);
		minimizeConstraints();
	}
	
	/**
	 * Add a constraint and minimize to reduce overlap
	 * 
	 * @param constraint
	 */
	public void addConstraint(UnicityConstraint constraint) {
		constraints.add(constraint);
		minimizeConstraints();
	}
	
	/**
	 * Remove the given constraint
	 * 
	 * @param constraint
	 */
	public void removeConstraint(UnicityConstraint constraint) {
		constraints.remove(constraint);
	}
	
	/**
	 * Get the contained constraints
	 * 
	 * @return
	 */
	public SortedSet<UnicityConstraint> getConstraints() {
		return constraints;
	}
	
	public int getNumberOfConstraints(){
		return constraints.size();
	}
	
	/**
	 * Remove constraint overlap by removing the broader ones.
	 */
	private void minimizeConstraints() {
		
		TreeSet<UnicityConstraint> toRemove = new TreeSet<UnicityConstraint>();
		for (UnicityConstraint constraintA : constraints) {
			for (UnicityConstraint constraintB : constraints) {
				if (constraintA.isBroaderThan(constraintB))
					toRemove.add(constraintA);
			}
		}
		
		constraints.removeAll(toRemove);
		toRemove.clear();
	}
	
	/**
	 * Make a pretty representation for printing
	 * 
	 * @return A string
	 */
	public String toAsciiString() {
		StringBuilder sb = new StringBuilder();
		
		// Print all constraints
		for (UnicityConstraint uc : constraints) {
			for (int i = 0; i < uc.getNumberOfRoles(); i++) {
				for (int w = 0; w < MultiUnicityConstraint.ROLE_WIDTH; w++)
					sb.append((uc.getConstraintRoles()[i]) ? '-' : ' ');
			}
			sb.append("\n");
		}
		
		// Print blocks for roles
		for (int i = constraints.iterator().next().getNumberOfRoles(); i > 0; --i) {
			sb.append('[');
			for (int j = 2; j < ROLE_WIDTH; j++)
				sb.append(' ');
			sb.append(']');
		}
		return sb.toString();
	}
	
	/**
	 * Compare multi-constraints by comparing each contraint, which are sorted
	 */
	@Override
	public int compareTo(MultiUnicityConstraint other) {
		if (other == null)
			return -1;
		if (this == other)
			return 0;
		
		// First compare size
		int sizeDiff = this.getConstraints().size() - other.getConstraints().size();
		if (sizeDiff != 0)
			return sizeDiff;
		
		// Same size, compare contents
		Iterator<UnicityConstraint> itA = this.getConstraints().iterator();
		Iterator<UnicityConstraint> itB = other.getConstraints().iterator();
		for (int i = 0; i < this.constraints.size(); i++) {
			int diff = itA.next().compareTo(itB.next());
			if (diff != 0)
				return diff;
		}
		
		// No diff found, so equal
		return 0;
	}
	
	/**
	 * Test if the given tuple is allowed by each of the unicity constraints
	 * 
	 * @param tuple
	 *            The tuple to test
	 * @return Whether each UC allowes the tuple
	 */
	public boolean isAllowed(boolean[] tuple) {
		for (UnicityConstraint uc : constraints)
			if (!uc.isAllowed(tuple))
				return false;
		return true;
	}
	
}