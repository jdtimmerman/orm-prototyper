package nl.ru.jtimmerm.orm.types;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import nl.ru.jtimmerm.orm.types.IObjectType;

public class ObjectType extends Type implements IObjectType, Serializable {
	
	/**
	 * Create an object type with the given content
	 * 
	 * @param content
	 *            The name/title of this object type
	 */
	public ObjectType(String content) {
		super();
		setContent(content);
	}
	
	/**
	 * Create an object or value type, based on a boolean value
	 * 
	 * @param content
	 *            The name/title of this object type
	 * @param isValueType
	 *            Whether this is a value or an object type
	 */
	public ObjectType(String content, boolean isValueType) {
		this(content);
		mIsValueType = isValueType;
	}
	
	// ////////////////////////////////////////////////////////////////////////
	// CONTENT
	// ////////////////////////////////////////////////////////////////////////
	
	/**
	 * Holds the title of this object (or value) type
	 */
	private String	mContent;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.orm.types.IObjectType#getContent()
	 */
	@Override
	public String getContent() {
		return mContent;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.orm.types.IObjectType#setContent(java.lang.String)
	 */
	@Override
	public void setContent(String content) {
		mContent = content.trim();
	}
	
	// ////////////////////////////////////////////////////////////////////////
	// SAMPLE
	// ////////////////////////////////////////////////////////////////////////
	/**
	 * Holds sample data
	 */
	private Set<String>	mSamples	= new HashSet<String>();
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.orm.types.IObjectType#getSamples()
	 */
	@Override
	public Set<String> getSamples() {
		return mSamples;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.orm.types.IObjectType#addSample(java.lang.String)
	 */
	@Override
	public void addSample(String string) {
		mSamples.add(string);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.orm.types.IObjectType#addSamples(java.lang.String[])
	 */
	@Override
	public void addSamples(String... strings) {
		for (String s : strings)
			mSamples.add(s);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.ru.jtimmerm.orm.types.IObjectType#setSamples(java.util.Collection)
	 */
	@Override
	public void setSamples(Collection<String> samples) {
		mSamples.clear();
		mSamples.addAll(samples);
	}
	
	// ////////////////////////////////////////////////////////////////////////
	// VALUETYPE
	// ////////////////////////////////////////////////////////////////////////
	
	/**
	 * Whether this is a value type
	 */
	private boolean	mIsValueType	= false;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.orm.types.IObjectType#setValueType(boolean)
	 */
	@Override
	public void setValueType(boolean isValueType) {
		mIsValueType = isValueType;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.orm.types.IObjectType#isValueType()
	 */
	@Override
	public boolean isValueType() {
		return mIsValueType;
	}
	
	// ////////////////////////////////////////////////////////////////////////
	// Output
	// ////////////////////////////////////////////////////////////////////////
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return mContent;
	}
	
	// ////////////////////////////////////////////////////////////////////////
	// COMPARING
	// ////////////////////////////////////////////////////////////////////////
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.ru.jtimmerm.orm.types.IType#compareTo(nl.ru.jtimmerm.orm.types.IType)
	 */
	@Override
	public int compareTo(IType o) {
		
		if (!(o instanceof IObjectType))
			return -10;
		
		return getContent().compareTo(((IObjectType) o).getContent());
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		
		if (!(other instanceof IObjectType))
			return false;
		
		return compareTo((IObjectType) other) == 0;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return generateHashCode(mContent.hashCode());
	}
	
	/**
	 * Generate a hashcode
	 * 
	 * @param contentHashCode
	 *            The hashcode of the title
	 * @return
	 */
	public static int generateHashCode(int contentHashCode) {
		int hash = 5;
		hash = hash * 7 + ObjectType.class.hashCode();
		hash = hash * 11 + contentHashCode;
		// TODO Untested, so not implemented
		// hash = hash * 13 + ((isValueType) ? 3 : 5);
		return hash;
	}
	
	// ////////////////////////////////////////////////////////////////////////
	//
	// ////////////////////////////////////////////////////////////////////////
	
	private static final long	serialVersionUID	= -1508716982119339450L;
}
