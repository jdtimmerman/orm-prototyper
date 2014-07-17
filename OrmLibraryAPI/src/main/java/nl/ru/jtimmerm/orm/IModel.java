package nl.ru.jtimmerm.orm;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

import nl.ru.jtimmerm.orm.types.IFactType;
import nl.ru.jtimmerm.orm.types.IObjectType;
import nl.ru.jtimmerm.orm.types.IType;

/**
 * Adds methods that are not needed for the {@link IModelUpdates} class but are
 * still useful in other parts of the application.
 * 
 * @author joost
 */
public interface IModel extends IModelObservable, IModelUpdates, Serializable {

	// ////////////////////////////////////////////////////////////////////////
	// GETTING
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Get all <code>ObjectType</code>s
	 * 
	 * @return
	 */
	Set<IObjectType> getObjectTypes();

//	/**
//	 * Get all <code>Constraint</code>s
//	 * 
//	 * @return
//	 */
//	Set<IConstraint> getConstraints();

	/**
	 * Get all <code>FactType</code>s
	 * 
	 * @return
	 */
	Set<IFactType> getFactTypes();

	/**
	 * Get all types
	 * 
	 * @return
	 */
	Set<? extends IType> getTypes();
	
	
	/**
	 * Find the fact type with the given verbalization
	 * @param verbalization The full verbalization, including the contents of the object types
	 * @return
	 * @throws OrmException 
	 */
	IFactType findFactType(String verbalization) throws OrmException;
	
	/**
	 * Find the object type with the given content
	 * @param content The contents of the object type
	 * @return
	 * @throws OrmException 
	 */
	IObjectType findObjectType(String content) throws OrmException;

	// ////////////////////////////////////////////////////////////////////////
	// ADDING
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Add all types of the collection in the model
	 * 
	 * @param types
	 * @throws OrmException
	 */
	void addAll(Collection<? extends IType> types) throws OrmException;

	// ////////////////////////////////////////////////////////////////////////
	// REMOVING
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Removes all passed types
	 * 
	 * @param types
	 * @throws OrmException
	 */
	void removeAll(Collection<? extends IType> types) throws OrmException;

	// ////////////////////////////////////////////////////////////////////////
	// GETTING +
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Get all <code>Type</code>s that have a relation with the given type
	 * 
	 * @param type
	 *            The type to find the relations for
	 * @return The types that have a relation with the given type
	 * @throws OrmException
	 */
	Set<IType> getRelatingTypes(IType type) throws OrmException;

	/**
	 * Get all <code>Type</code>s that have a relation with all given types
	 * 
	 * @param types
	 *            The types to find the relations for
	 * @return Types that have a relation with all passed types
	 * @throws OrmException
	 */
	Set<IType> getRelatingTypes(IType... type) throws OrmException;

}
