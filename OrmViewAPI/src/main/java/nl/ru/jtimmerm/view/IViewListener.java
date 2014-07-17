package nl.ru.jtimmerm.view;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;

import nl.ru.jtimmerm.orm.IModelUpdates;
import nl.ru.jtimmerm.orm.types.IFactType;
import nl.ru.jtimmerm.orm.types.IObjectType;
import nl.ru.jtimmerm.orm.types.IType;
import nl.ru.jtimmerm.orm.types.MultiUnicityConstraint;

/**
 * Holds some methods that that are used by the view only and do not require
 * action of the model, and overrides methods in the {@link IModelUpdates}
 * -interface to not throw an exception. The class that implements this
 * interface should handle those exceptions.
 * 
 * @author joost
 * 
 */
public interface IViewListener extends IModelUpdates {
	
	/**
	 * Notify someone that <code>type</code> has moved to grid position
	 * <code>position</code>
	 * 
	 * @param type
	 *            The type to be moved
	 * @param position
	 *            The grid position the type is moved to
	 */
	<T extends IType> void newPosition(T type, Point position);
	
	/**
	 * Export the current graph to a file
	 * 
	 * @throws Exception
	 */
	public void exportFile();
	
	// ////////////////////////////////////////////////////////////////////////
	// OVERRIDE QUESTIONLISTENER WITHOUT EXCEPTIONS
	// ////////////////////////////////////////////////////////////////////////
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.ru.jtimmerm.orm.IQuestionListener#remove(nl.ru.jtimmerm.orm.types.
	 * IType)
	 */
	@Override
	<T extends IType> void remove(T type);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.ru.jtimmerm.orm.IQuestionListener#updateFactType(nl.ru.jtimmerm.orm
	 * .types.IFactType, java.lang.String, java.util.ArrayList)
	 */
	@Override
	void updateFactType(IFactType facttype, String verbalization, ArrayList<String> objects);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.ru.jtimmerm.orm.IQuestionListener#analyzeSentence(java.lang.String)
	 */
	@Override
	void analyzeSentence(String fact);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.ru.jtimmerm.orm.IQuestionListener#add(nl.ru.jtimmerm.orm.types.IType)
	 */
	@Override
	<T extends IType> void add(T type);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.ru.jtimmerm.orm.IQuestionListener#addTotalityConstraint(nl.ru.jtimmerm
	 * .orm.types.IObjectType, nl.ru.jtimmerm.orm.types.IFactType)
	 */
	@Override
	void addTotalityConstraint(IObjectType objectType, IFactType facttype);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.ru.jtimmerm.orm.IQuestionListener#removeTotalityConstraint(nl.ru.jtimmerm
	 * .orm.types.IObjectType, nl.ru.jtimmerm.orm.types.IFactType)
	 */
	@Override
	void removeTotalityConstraint(IObjectType objectType, IFactType facttype);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.ru.jtimmerm.orm.IQuestionListener#setSampleData(nl.ru.jtimmerm.orm
	 * .types.IObjectType, java.util.Collection)
	 */
	@Override
	void setSampleData(IObjectType objectType, Collection<String> samples);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.ru.jtimmerm.orm.IQuestionListener#setUnicityConstraints(nl.ru.jtimmerm
	 * .orm.types.IFactType, java.util.Collection)
	 */
	@Override
	void setUnicityConstraints(IFactType facttype, MultiUnicityConstraint muc);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.ru.jtimmerm.orm.IQuestionListener#setValueType(nl.ru.jtimmerm.orm.
	 * types.IObjectType, boolean)
	 */
	@Override
	void setValueType(IObjectType objecttype, boolean isValueType);
}
