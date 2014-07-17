package nl.ru.jtimmerm.orm.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import org.apache.log4j.Logger;

public class UnicityConstraintFactory {

	/**
	 * Get every binary combination for the given number of roles
	 * 
	 * @param numberOfRoles
	 * @return
	 */
	public static List<boolean[]> getVariants(int numberOfRoles) {
		// Determine number of combinations
		int variants = ((int) Math.pow(2, numberOfRoles)) - 1;

		// Get a representation for each combination
		ArrayList<boolean[]> data = new ArrayList<boolean[]>();
		for (int i = 0; i <= variants; i++)
			data.add(getBinary(i, numberOfRoles));

		return data;
	}

	/**
	 * Create a binary representation of an integer, and pad it with zeroes to
	 * be at least {@code numRoles} bits wide
	 * 
	 * @param index
	 * @param numRoles
	 * @return
	 */
	private static boolean[] getBinary(int index, int numRoles) {
		boolean[] bits = new boolean[numRoles];
		for (int i = numRoles - 1; i >= 0; i--) {
			bits[i] = (index & (1 << i)) != 0;
		}
		return bits;
	}

	/**
	 * Get every possible constraint for a fact-type with the given number of
	 * roles
	 * 
	 * @param numberOfRoles
	 * @return
	 */
	public static Collection<MultiUnicityConstraint> getPossibleConstraints(int numberOfRoles) {
		List<Collection<boolean[]>> constraints = combinations(getVariants(numberOfRoles), new ArrayList<boolean[]>());

		if (log.isDebugEnabled()) {
			log.debug("Constraints");
			for (Collection<boolean[]> col : constraints) {
				log.debug("");
				for (boolean[] arr : col)
					log.debug(Arrays.toString(arr));
			}
		}

		TreeSet<MultiUnicityConstraint> constraintsSet = new TreeSet<MultiUnicityConstraint>();
		for (Collection<boolean[]> constraint : constraints) {
			// constraint = removeOverruledContraints(constraint);
			MultiUnicityConstraint muc = new MultiUnicityConstraint();

			for (boolean[] c : constraint)
				try {
					muc.addConstraint(new UnicityConstraint(c));
				} catch (IllegalArgumentException e) {
					// Invalid constraints shall not be added
				}

			if (muc.getNumberOfConstraints() > 0)
				constraintsSet.add(muc);
		}

		return constraintsSet;
	}

	/**
	 * Find all combinations with the elements in the available list using
	 * recursion.
	 * 
	 * Given an 'available' set of abc, it will return abc,ab,ac,a,bc,b,c
	 * 
	 * @param available
	 *            Elements that are available
	 * @param included
	 *            Elements that have been added
	 * @return
	 */
	private static <E> List<Collection<E>> combinations(Collection<E> available, Collection<E> included) {
		List<Collection<E>> retval = new ArrayList<Collection<E>>();

		// Recursion ends if nog elements are available
		if (available.isEmpty()) {
			if (!included.isEmpty())
				retval.add(included);
			return retval;
		}

		// New arrays for passing on
		List<E> newIncludedA = new ArrayList<E>(included);
		List<E> newIncludedB = new ArrayList<E>(included);
		List<E> newAvailable = new ArrayList<E>(available);

		// Get the next E, and remove it from the available array
		newIncludedA.add(newAvailable.get(0));
		newAvailable.remove(0);

		// Recurse with the current item
		retval.addAll(combinations(newAvailable, newIncludedA));
		// Recurse without the current item
		retval.addAll(combinations(newAvailable, newIncludedB));

		return retval;
	}

	private static Logger log = Logger.getLogger(UnicityConstraintFactory.class);
}
