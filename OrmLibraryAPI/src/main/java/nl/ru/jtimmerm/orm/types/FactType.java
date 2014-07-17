package nl.ru.jtimmerm.orm.types;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import nl.ru.jtimmerm.Lang;
import nl.ru.jtimmerm.Settings;

/**
 * Represents a fact type
 * 
 * @author joost
 * 
 */
public class FactType extends Type implements IFactType, Serializable {
	
	private static final String	WILDCARD			= Settings.string("ft_wildcard");
	private static final String	WILDCARD_FRIENDLY	= Settings.string("ft_wildcard_friendly");
	
	// ////////////////////////////////////////////////////////////////////////
	// CONSTRUCT
	// /////////////////////////////////////////////////////////////////////////
	
	/**
	 * Create a fact type with the given object types and sentence
	 * 
	 * @param objects
	 *            The objects that play a role
	 * @param verbalization
	 *            The sentences with wildcards where roles are supposed to go
	 */
	public FactType(Collection<IObjectType> objects, String verbalization) {
		setObjectTypes(objects);
		setVerbalization(verbalization);
	}
	
	// ////////////////////////////////////////////////////////////////////////
	// VERBALIZATION
	// /////////////////////////////////////////////////////////////////////////
	
	/**
	 * Probably will use %s for the object names. "Person %s buys %s from %s",
	 * Joost, egg, farmer
	 */
	private String	mVerbalization	= "";
	
	/**
	 * @return the verbalization
	 */
	public String getVerbalization() {
		return mVerbalization;
	}
	
	/**
	 * @return the verbalization cleaned
	 */
	public String getCleanVerbalization() {
		return mVerbalization.replace(WILDCARD, WILDCARD_FRIENDLY);
	}
	
	/**
	 * @return the parsed verbalize
	 */
	@Override
	public String verbalize() {
		return String.format(mVerbalization, mObjectTypes.toArray());
	}
	
	/**
	 * @param verbalization
	 *            The verbalization to set
	 * @throws IllegalArgumentException
	 *             When the number of wildcards does not match the number of
	 *             objects
	 */
	@Override
	public void setVerbalization(String verbalization) throws IllegalArgumentException {
		
		// Replace under scores, used by friendly display, to wildcards
		verbalization = verbalization.replace(WILDCARD_FRIENDLY, WILDCARD);
		
		// Count wildcards
		int wcCount = findOccurences(WILDCARD, verbalization);
		
		if (wcCount == mObjectTypes.size())
			mVerbalization = verbalization;
		else
			throw new IllegalArgumentException(Lang.text("err_incorrect_wildcards", wcCount, mObjectTypes.size()));
	}
	
	/**
	 * Find the number of occurrences of the neelde in the haystack
	 * 
	 * @param needle
	 *            The needle to seek
	 * @param haystack
	 *            The haystack to seek in
	 * @return The number of needle in the haystack
	 */
	public static final int findOccurences(String needle, String haystack) {
		int lastIndex = 0;
		int count = 0;
		
		while (lastIndex != -1) {
			lastIndex = haystack.indexOf(needle, lastIndex);
			if (lastIndex != -1) {
				count++;
				lastIndex += needle.length();
			}
		}
		
		return count;
	}
	
	// ////////////////////////////////////////////////////////////////////////
	// OBJECTTYPES
	// ////////////////////////////////////////////////////////////////////////
	
	/**
	 * Holds the object types that play a role in this fact type
	 */
	private ArrayList<IObjectType>	mObjectTypes	= new ArrayList<IObjectType>();
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.orm.types.IFactType#getObjectTypes()
	 */
	public List<IObjectType> getObjectTypes() {
		return mObjectTypes;
	}
	
	/**
	 * @param objects
	 *            the bbjects to set
	 */
	public void setObjectTypes(Collection<IObjectType> objects) {
		mObjectTypes.clear();
		mObjectTypes.addAll(objects);
	}
	
	// ////////////////////////////////////////////////////////////////////////
	// TOTALITY CONSTRAINTS
	// ////////////////////////////////////////////////////////////////////////
	
	/**
	 * Holds the object types for which a totality constraint is set
	 */
	private Collection<IObjectType>	mTotalities	= new HashSet<IObjectType>();
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.ru.jtimmerm.orm.types.IFactType#addTotality(nl.ru.jtimmerm.orm.types
	 * .IObjectType)
	 */
	@Override
	public void addTotality(IObjectType object) {
		mTotalities.add(object);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.ru.jtimmerm.orm.types.IFactType#removeTotality(nl.ru.jtimmerm.orm.
	 * types.IObjectType)
	 */
	@Override
	public void removeTotality(IObjectType object) {
		mTotalities.remove(object);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.ru.jtimmerm.orm.types.IFactType#hasTotality(nl.ru.jtimmerm.orm.types
	 * .IObjectType)
	 */
	@Override
	public boolean hasTotality(IObjectType object) {
		return mTotalities.contains(object);
	}
	
	// ////////////////////////////////////////////////////////////////////////
	// TUPLES
	// ////////////////////////////////////////////////////////////////////////
	
	// /*
	// * (non-Javadoc)
	// *
	// * @see nl.ru.jtimmerm.orm.types.IFactType#addTuple(Collection<String>)
	// */
	// public void addTuple(Collection<IObjectType> objects) throws OrmException
	// {
	//
	// if (mObjectTypes.size() != objects.size())
	// throw new OrmException(Lang.text("err_incorrect_objects",
	// objects.size(), mObjectTypes.size()));
	//
	// }
	
	// ////////////////////////////////////////////////////////////////////////
	// UNICITY
	// /////////////////////////////////////////////////////////////////////////
	
	/**
	 * Holds the unicity constraints
	 */
	private MultiUnicityConstraint	mUnicity	= new MultiUnicityConstraint();
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.orm.types.IFactType#setUnicities(int[][])
	 */
	@Override
	public void setUnicity(MultiUnicityConstraint muc) {
		mUnicity = muc;
	}
	
	/*
	 * (non-Javadoc)
	 * @see nl.ru.jtimmerm.orm.types.IFactType#addUnicity(nl.ru.jtimmerm.orm.types.UnicityConstraint)
	 */
	@Override
	public void addUnicity(UnicityConstraint uc) {
		mUnicity.addConstraint(uc);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.orm.types.IFactType#removeUnicities()
	 */
	@Override
	public void removeUnicities() {
		mUnicity = new MultiUnicityConstraint();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.orm.types.IFactType#getUnicities()
	 */
	@Override
	public MultiUnicityConstraint getUnicities() {
		return mUnicity;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.orm.types.IFactType#hasUnicity()
	 */
	@Override
	public boolean hasUnicity() {
		return mUnicity.getNumberOfConstraints() > 0;
	}
	
	// ////////////////////////////////////////////////////////////////////////
	// REPRESENTATION
	// ////////////////////////////////////////////////////////////////////////
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return verbalize();
	}
	
	// ////////////////////////////////////////////////////////////////////////
	// COMPARING
	// ////////////////////////////////////////////////////////////////////////
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.orm.IFactType#compareTo(nl.ru.jtimmerm.orm.IFactType)
	 */
	@Override
	public int compareTo(IType arg0) {
		
		if (!(arg0 instanceof IFactType))
			return -100;
		
		IFactType type = (IFactType) arg0;
		
		// TODO check this method
		
		if (!mVerbalization.equals(type.getVerbalization()))
			return getVerbalization().compareTo(type.getVerbalization());
		
		if (mObjectTypes.size() != type.getObjectTypes().size())
			if (mObjectTypes.size() < type.getObjectTypes().size())
				return -1;
			else
				return 1;
		
		if (!mObjectTypes.containsAll(type.getObjectTypes()))
			return 1;
		
		return 0;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof IFactType))
			return false;
		
		return compareTo((IFactType) other) == 0;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return generateHashCode(mVerbalization.hashCode(), mObjectTypes);
	}
	
	/**
	 * Generate a hashcode based on the passed values
	 * 
	 * @param verbalizationHashCode
	 *            The hashcode of the verbalization string
	 * @param objectTypes
	 *            A collection of object types
	 * @return
	 */
	public static int generateHashCode(int verbalizationHashCode, Collection<IObjectType> objectTypes) {
		
		int hash = 3;
		
		hash = hash * 5 + FactType.class.hashCode();
		hash = hash * 7 + verbalizationHashCode;
		hash = hash * 11 + objectTypes.size();
		
		for (IObjectType o : objectTypes)
			hash = hash * 11 + o.hashCode();
		
		return hash;
		
	}
	
	// ////////////////////////////////////////////////////////////////////////
	//
	// ////////////////////////////////////////////////////////////////////////
	
	private static final long	serialVersionUID	= 5587988649477434322L;
}
