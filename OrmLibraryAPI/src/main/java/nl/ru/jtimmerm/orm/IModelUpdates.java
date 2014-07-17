package nl.ru.jtimmerm.orm;

import java.util.ArrayList;
import java.util.Collection;

import nl.ru.jtimmerm.orm.types.IFactType;
import nl.ru.jtimmerm.orm.types.IObjectType;
import nl.ru.jtimmerm.orm.types.IType;
import nl.ru.jtimmerm.orm.types.MultiUnicityConstraint;

/**
 * This interface holds all external interaction to update the model
 * 
 * @author joost
 * 
 */
public interface IModelUpdates {
	
	/**
	 * Notify someone that a string needs to be analyzed
	 * 
	 * @param fact
	 * @throws OrmException
	 */
	void analyzeSentence(String fact) throws OrmException;
	
	/**
	 * Add the passed type
	 * 
	 * @param type
	 *            The type to be removed
	 * @throws OrmException
	 */
	<T extends IType> void add(T type) throws OrmException;
	
	/**
	 * Remove the passed type
	 * 
	 * @param objectType
	 * @param facttype
	 */
	<T extends IType> void remove(T type) throws OrmException;
	
	/**
	 * Add a totality constraint on the objecttype for the role it plays in the
	 * facttype
	 * 
	 * @param objectType
	 * @param facttype
	 * @throws OrmException
	 */
	void addTotalityConstraint(IObjectType objectType, IFactType facttype) throws OrmException;
	
	/**
	 * Removed the totality constraint on the role that the objecttype plays in
	 * the facttype
	 * 
	 * @param objectType
	 * @param facttype
	 * @throws OrmException
	 */
	void removeTotalityConstraint(IObjectType objectType, IFactType facttype) throws OrmException;
	
	/**
	 * Set sample values for the passed objecttype
	 * 
	 * @param objectType
	 * @param samples
	 * @throws OrmException
	 */
	void setSampleData(IObjectType objectType, Collection<String> samples) throws OrmException;
	
	/**
	 * Set a collection of unicityconstraints on the passed facttype
	 * 
	 * @param facttype
	 * @param calculateUnicity
	 * @throws OrmException
	 */
	void setUnicityConstraints(IFactType facttype, MultiUnicityConstraint muc) throws OrmException;
	
	/**
	 * Set whether the passed objecttype is in fact a valuetype
	 * 
	 * @param objecttype
	 * @param isValueType
	 * @throws OrmException
	 */
	void setValueType(IObjectType objecttype, boolean isValueType) throws OrmException;
	
	/**
	 * Update the verbalization of the facttype
	 * 
	 * TODO: Also update the objecttypes
	 * 
	 * @param facttype
	 * @param verbalization
	 * @param objects
	 * @throws OrmException
	 */
	void updateFactType(IFactType facttype, String verbalization, ArrayList<String> objects) throws OrmException;
	
}
