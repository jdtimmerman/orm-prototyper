/**
 * 
 */
package nl.ru.jtimmerm.orm.types;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author joost
 * 
 */
public class UnicityConstraint implements Serializable, Comparable<UnicityConstraint> {
	
	private static final long	serialVersionUID	= 1L;
	
	/**
	 * Each element in the array indicates whether this unicity constraints is
	 * active on that role.
	 */
	private boolean[]			constraintRoles;
	
	public UnicityConstraint(boolean[] roles) {
		if (!satisfiesNMinusOne(roles))
			throw new IllegalArgumentException("Constraint does not satisfy N-1 rule: " + Arrays.toString(roles));
		this.constraintRoles = roles;
	}
	
	public boolean[] getConstraintRoles() {
		return constraintRoles;
	}
	
	public int getNumberOfRoles() {
		return constraintRoles.length;
	}
	
	public static boolean satisfiesNMinusOne(boolean[] constraint) {
		int n = constraint.length;
		int truees = 0;
		
		for (boolean b : constraint)
			if (b)
				truees++;
		
		return (truees >= n - 1);
	}
	
	private static void assertArraySize(boolean[] a, boolean[] b) {
		if (a == null || b == null)
			throw new IllegalArgumentException("Null is not valid");
		if (a.length != b.length)
			throw new IllegalArgumentException("Arrays not of same size");
	}
	
	public int getInt() {
		int n = 0, l = constraintRoles.length;
		for (int i = 0; i < l; ++i) {
			n = (n << 1) + (constraintRoles[i] ? 1 : 0);
		}
		return n;
	}
	
	/**
	 * A constraint is broader if it has the same roles active and more.
	 * 
	 * Here a is broader then b:
	 * 
	 * <pre>
	 * a  ------
	 * b  ---
	 *    [ ][ ]
	 * </pre>
	 * 
	 * Here a is not broader than b
	 * 
	 * <pre>
	 * a  ---------
	 * b        ------
	 *    [ ][ ][ ][ ]
	 * </pre>
	 * 
	 * @param otherRoles
	 * @return
	 */
	public boolean isBroaderThan(UnicityConstraint otherConstraint) {
		assertArraySize(constraintRoles, otherConstraint.getConstraintRoles());
		
		boolean otherConstraintIsBroader = false;
		for (int j = 0; j < constraintRoles.length; j++)
			if (constraintRoles[j] && !otherConstraint.getConstraintRoles()[j]) {
				// The other constraint is totally different
				otherConstraintIsBroader = false;
				break;
			} else if (constraintRoles[j] == otherConstraint.getConstraintRoles()[j]) {
				// Okay, they're the same, this is not very interesting
			} else if (!constraintRoles[j] && otherConstraint.getConstraintRoles()[j]) {
				// It appears that the otherone is broader, but were not
				// certain yet, it might be a totally different constraint
				otherConstraintIsBroader = true;
			}
		
		return otherConstraintIsBroader;
	}
	
	@Override
	public int compareTo(UnicityConstraint o) {
		if (o == null)
			return -1;
		return Integer.compare(this.getInt(), o.getInt());
	}
	
	/**
	 * Given that the tuple [0,0,0,...] exists, is the given tuple still
	 * allowed?
	 * 
	 * @param tuple
	 *            The tuple to test
	 * @return
	 */
	public boolean isAllowed(boolean[] tuple) {
		if (tuple == null || tuple.length != constraintRoles.length)
			throw new IllegalArgumentException("Tuple and constraint are not of same size.");
		
		// If any of the values under the constraint differs from the
		// false-tuple the tuple is accepted
		for (int i = 0; i < tuple.length; i++)
			// If constraint is active on this role
			// And the tuples value is different than the false-tuple
			if (constraintRoles[i] && tuple[i])
				return true;
		
		return false;
	}
	
}