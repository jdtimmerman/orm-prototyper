package nl.ru.jtimmerm.orm.types;

import java.util.List;

/**
 * Represents a fact type.
 * 
 * @author joost
 */
public interface IFactType extends IType {

	// ////////////////////////////////////////////////////////////////////////
	// Objects
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Get the objecttypes that play a role in this facttype
	 * 
	 * @return
	 */
	List<IObjectType> getObjectTypes();

	// ////////////////////////////////////////////////////////////////////////
	// Verbalization
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Get the verbalization of this facttype, that is the description of the
	 * fact with all objecttypes filled out
	 * 
	 * @return
	 */
	String verbalize();

	/**
	 * Get the verbalization with the wildcards that are internally used for
	 * objects
	 * 
	 * @return
	 */
	String getVerbalization();

	/**
	 * Get the verbalization with clean wildcards for objects
	 * 
	 * @return
	 */
	String getCleanVerbalization();

	/**
	 * Set a verbalization
	 * 
	 * @param verbalization
	 *            A string with either internal of clean wildcards
	 * @throws IllegalArgumentException
	 *             When the number of wildcards does not match the number of
	 *             objects
	 */
	void setVerbalization(String verbalization) throws IllegalArgumentException;

	// ////////////////////////////////////////////////////////////////////////
	// Tuples
	// ////////////////////////////////////////////////////////////////////////

	// /**
	// * TODO: Implement
	// *
	// * Add a tuple to this fact type
	// *
	// * @param values
	// * A collection of values for each role in this tuple
	// * @throws OrmException
	// */
	// void addTuple(Collection<String> values) throws OrmException;

	// ////////////////////////////////////////////////////////////////////////
	// Totality
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * A totality constraint for the given object
	 * 
	 * @param object
	 *            The object type
	 */
	void addTotality(IObjectType object);

	/**
	 * Remove the totality constraint on the given object
	 * 
	 * @param object
	 *            The object type
	 */
	void removeTotality(IObjectType object);

	/**
	 * @param object
	 *            The object type
	 * @return Whether the object type has a totality constraint in this fact
	 *         type
	 */
	boolean hasTotality(IObjectType object);

	// ////////////////////////////////////////////////////////////////////////
	// Unicity
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Set unicity constraints on this fact type
	 * 
	 * @param unicities
	 */
	void setUnicity(MultiUnicityConstraint multiUC);

	/**
	 * Add a single unicity constraint to the existing MultiUnicityConstraint (or create a new MUC)
	 * @param uc
	 */
	void addUnicity(UnicityConstraint uc);
	
	
	/**
	 * Remove all unicity constraints of this fact type
	 */
	void removeUnicities();

	/**
	 * Get the unicity constraints of this fact type
	 * 
	 * @return An array of unicity constraints
	 */
	MultiUnicityConstraint getUnicities();

	/**
	 * @return Whether this fact type has a (or more) unicity constraint set
	 */
	boolean hasUnicity();

}
