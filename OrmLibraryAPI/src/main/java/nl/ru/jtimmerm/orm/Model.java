package nl.ru.jtimmerm.orm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import nl.ru.jtimmerm.Lang;
import nl.ru.jtimmerm.orm.grammar.ParseResult;
import nl.ru.jtimmerm.orm.grammar.ParseUtil;
import nl.ru.jtimmerm.orm.types.FactType;
import nl.ru.jtimmerm.orm.types.IFactType;
import nl.ru.jtimmerm.orm.types.IObjectType;
import nl.ru.jtimmerm.orm.types.IType;
import nl.ru.jtimmerm.orm.types.MultiUnicityConstraint;
import nl.ru.jtimmerm.orm.types.ObjectType;

import org.antlr.runtime.RecognitionException;
import org.apache.log4j.Logger;

public class Model implements IModel {
	
	private static final long				serialVersionUID	= -700313384280185788L;
	
	// ////////////////////////////////////////////////////////////////////////
	// TYPE CONTAINERS
	// ////////////////////////////////////////////////////////////////////////
	
	/**
	 * Holds the object types
	 */
	private HashMap<Integer, IObjectType>	mObjects			= new HashMap<Integer, IObjectType>();
	
	/**
	 * Holds the fact types
	 */
	private HashMap<Integer, IFactType>		mFacts				= new HashMap<Integer, IFactType>();
	
	// ////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR
	// ////////////////////////////////////////////////////////////////////////
	
	public Model() {
	}
	
	// ////////////////////////////////////////////////////////////////////////
	// GETTING
	// ////////////////////////////////////////////////////////////////////////
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.orm.IModel#getObjectTypes()
	 */
	@Override
	public Set<IObjectType> getObjectTypes() {
		return new HashSet<IObjectType>(mObjects.values());
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.orm.IModel#getFactTypes()
	 */
	@Override
	public Set<IFactType> getFactTypes() {
		return new HashSet<IFactType>(mFacts.values());
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.orm.IModel#getTypes()
	 */
	@Override
	public Set<IType> getTypes() {
		
		LinkedHashSet<IType> combined = new LinkedHashSet<IType>();
		combined.addAll(getObjectTypes());
		combined.addAll(getFactTypes());
		
		return combined;
	}
	
	/**
	 * Find an ObjectType by its content (or title)
	 * 
	 * @param s
	 *            The title of an object or value you're searching
	 * @return The object type
	 * @throws OrmException
	 */
	@Override
	public IObjectType findObjectType(String s) throws OrmException {
		if (log.isDebugEnabled())
			log.debug("Searching for objecttype with content: " + s);
		
		IObjectType ot = mObjects.get(ObjectType.generateHashCode(s.hashCode()));
		
		if (ot != null)
			return ot;
		else
			throw new OrmException(Lang.text("err_ot_notfound", s));
	}
	
	@Override
	public IFactType findFactType(String verbalization) throws OrmException {
		if (log.isDebugEnabled())
			log.debug("Searching for facttype with verbalization: " + verbalization);
		
		for (IFactType ft : getFactTypes()) {
			if (ft.verbalize().equals(verbalization))
				return ft;
		}
		throw new OrmException(Lang.text("err_ft_notfound", verbalization));
	}
	
	// ////////////////////////////////////////////////////////////////////////
	// ADDING
	// ////////////////////////////////////////////////////////////////////////
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.orm.IModel#analyzeSentence(java.lang.String)
	 */
	@Override
	public void analyzeSentence(String sentence) throws OrmException {
		HashSet<ParseResult> prs;
		
		if(sentence == null || sentence.isEmpty() )
			throw new OrmException(Lang.text("empty_sentence"));
		
		// Get results from ANTLR
		try {
			prs = ParseUtil.parseSentence(sentence);
		} catch (RecognitionException e) {
			throw new OrmException(Lang.text("parse_error"), e);
		}
		
		// Turn them into objects
		
		for (ParseResult pr : prs) {
			// Determine objects
			ArrayList<IObjectType> objects = new ArrayList<IObjectType>(pr.getObjects().size());
			for (String o : pr.getObjects()) {
				IObjectType obj = new ObjectType(o);
				
				if (mObjects.containsKey(obj.hashCode())) {
					objects.add(mObjects.get(obj.hashCode()));
				} else {
					add(obj);
					objects.add(obj);
				}
			}
			
			// Create the fact
			IFactType ft = new FactType(objects, pr.getSentence());
			
			if (mFacts.containsKey(ft.hashCode()))
				log.warn(Lang.text("err_facttype_exists", pr.getSentence()));
			else
				add(ft);
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.orm.IModelUpdates#add(nl.ru.jtimmerm.orm.types.IType)
	 */
	@Override
	public void add(IType type) throws OrmException {
		
		_add(type);
		
		changed();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.orm.IModel#addAll(java.util.Collection)
	 */
	@Override
	public void addAll(Collection<? extends IType> types) throws OrmException {
		
		for (IType t : types)
			_add(t);
		
		changed();
	}
	
	/**
	 * Add the object without notifying the listeners of the factbase. This
	 * should be done when this methods returns normally
	 * 
	 * @param type
	 * @throws OrmException
	 */
	private void _add(IType type) throws OrmException {
		if (type instanceof IObjectType)
			mObjects.put(type.hashCode(), (IObjectType) type);
		else if (type instanceof IFactType)
			mFacts.put(type.hashCode(), (IFactType) type);
		else
			throw new OrmException(Lang.text("unsupported_class", type.getClass().getName()));
	}
	
	// ////////////////////////////////////////////////////////////////////////
	// REMOVING
	// ////////////////////////////////////////////////////////////////////////
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.ru.jtimmerm.orm.IModelUpdates#remove(nl.ru.jtimmerm.orm.types.IType)
	 */
	@Override
	public void remove(IType type) throws OrmException {
		_remove(type);
		
		changed();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.orm.IModel#removeAll(java.util.Collection)
	 */
	@Override
	public void removeAll(Collection<? extends IType> types) throws OrmException {
		
		for (IType t : types)
			remove(t);
	}
	
	/**
	 * Remove the object without notifying
	 * 
	 * @param type
	 * @throws OrmException
	 */
	private void _remove(IType type) throws OrmException {
		
		if (type instanceof IObjectType) {
			
			if (getRelatingTypes((IObjectType) type).size() == 0)
				mObjects.remove(type.hashCode());
			else
				throw new OrmException(Lang.text("err_still_playing_roles"));
			
		} else if (type instanceof IFactType) {
			mFacts.remove(type.hashCode());
		} else {
			throw new OrmException(Lang.text("unsupported_class", type.getClass().getName()));
		}
		
	}
	
	// ////////////////////////////////////////////////////////////////////////
	// MANIPULATING CONSTRAINTS
	// ////////////////////////////////////////////////////////////////////////
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.ru.jtimmerm.orm.IModelUpdates#addTotalityConstraint(nl.ru.jtimmerm
	 * .orm.types.IObjectType, nl.ru.jtimmerm.orm.types.IFactType)
	 */
	@Override
	public void addTotalityConstraint(IObjectType object, IFactType facttype) {
		mFacts.get(facttype.hashCode()).addTotality(object);
		changed();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.ru.jtimmerm.orm.IModelUpdates#removeTotalityConstraint(nl.ru.jtimmerm
	 * .orm.types.IObjectType, nl.ru.jtimmerm.orm.types.IFactType)
	 */
	@Override
	public void removeTotalityConstraint(IObjectType object, IFactType facttype) {
		mFacts.get(facttype.hashCode()).removeTotality(object);
		changed();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.ru.jtimmerm.orm.IModelUpdates#setUnicityConstraints(nl.ru.jtimmerm
	 * .orm.types.IFactType, java.util.Collection)
	 */
	@Override
	public void setUnicityConstraints(IFactType facttype, MultiUnicityConstraint muc) {
		mFacts.get(facttype.hashCode()).setUnicity(muc);
		changed();
	}
	
	// ////////////////////////////////////////////////////////////////////////
	// MANIPULATING CONTENT
	// ////////////////////////////////////////////////////////////////////////
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.ru.jtimmerm.orm.IModelUpdates#setSampleData(nl.ru.jtimmerm.orm.types
	 * .IObjectType, java.util.Collection)
	 */
	@Override
	public void setSampleData(IObjectType objectType, Collection<String> samples) {
		mObjects.get(objectType.hashCode()).setSamples(samples);
		changed();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.ru.jtimmerm.orm.IModelUpdates#setValueType(nl.ru.jtimmerm.orm.types
	 * .IObjectType, boolean)
	 */
	@Override
	public void setValueType(IObjectType object, boolean isValueType) {
		mObjects.get(object.hashCode()).setValueType(isValueType);
		changed();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.ru.jtimmerm.orm.IModelUpdates#updateFactType(nl.ru.jtimmerm.orm.types
	 * .IFactType, java.lang.String, java.util.ArrayList)
	 */
	@Override
	public void updateFactType(IFactType facttype, String verbalization, ArrayList<String> objects) throws OrmException {
		
		// First get the real objects from the string representations
		
		log.warn("Changing objecttypes not implemented yet!");
		// ArrayList<IObjectType> realObjects = new ArrayList<IObjectType>();
		// for(String objStr : objects)
		// realObjects.add(findObjectType(objStr));
		
		IFactType ft = mFacts.get(facttype.hashCode());
		_remove(ft);
		ft.setVerbalization(verbalization);
		_add(ft);
		changed();
	}
	
	// ////////////////////////////////////////////////////////////////////////
	// FIND RELATIONSHIPS
	// ////////////////////////////////////////////////////////////////////////
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.ru.jtimmerm.orm.IModel#getRelatingTypes(nl.ru.jtimmerm.orm.types.IType
	 * )
	 */
	@Override
	public Set<IType> getRelatingTypes(IType type) throws OrmException {
		
		if (type instanceof IObjectType)
			return getRelatingTypes((IObjectType) type);
		else if (type instanceof IFactType)
			return getRelatingTypes((IFactType) type);
		else
			throw new OrmException(Lang.text("unsupported_class", type.getClass().getName()));
	}
	
	/**
	 * Find all facts having a relationship with the given ObjectType
	 * 
	 * @param type
	 * @return
	 * @throws OrmException
	 */
	public Set<IType> getRelatingTypes(IObjectType type) throws OrmException {
		LinkedHashSet<IType> relating = new LinkedHashSet<IType>();
		
		log.debug(Lang.text("getting_relating", type.toString()));
		
		for (IFactType f : mFacts.values())
			if (f.getObjectTypes().contains(type))
				relating.add(f);
		
		return relating;
	}
	
	/**
	 * Find all objects and constraints having a relationship with the given
	 * FactType
	 * 
	 * @param type
	 * @return
	 * @throws OrmException
	 */
	public Set<IType> getRelatingTypes(IFactType type) throws OrmException {
		LinkedHashSet<IType> relating = new LinkedHashSet<IType>();
		
		relating.addAll(((IFactType) type).getObjectTypes());
		
		return relating;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.ru.jtimmerm.orm.IModel#getRelatingTypes(nl.ru.jtimmerm.orm.types.IType
	 * [])
	 */
	@Override
	public Set<IType> getRelatingTypes(IType... types) throws OrmException {
		
		HashSet<IType> intersection = new HashSet<IType>();
		HashMap<IType, Set<IType>> allrelations = new HashMap<IType, Set<IType>>();
		
		// Fetch all relations for the given types
		for (IType type : types)
			allrelations.put(type, getRelatingTypes(type));
		
		// Put them on a big heap
		for (Set<IType> relations : allrelations.values())
			intersection.addAll(relations);
		
		// Retain only those who are in all relation-sets
		for (Set<IType> relations : allrelations.values())
			intersection.retainAll(relations);
		
		return intersection;
	}
	
	// ////////////////////////////////////////////////////////////////////////
	// DEBUG
	// ////////////////////////////////////////////////////////////////////////
	
	/**
	 * Print a simple representation of the content of the model (debugging
	 * purposes)
	 */
	public void printFactbase() {
		
		log.debug("--- Facts ---");
		for (IFactType f : mFacts.values())
			log.debug(f.toString());
		
		log.debug("--- Objects ---");
		for (IObjectType o : mObjects.values())
			log.debug(o.toString());
		
	}
	
	// ////////////////////////////////////////////////////////////////////////
	// OBSERVE MODEL CHANGES
	// ////////////////////////////////////////////////////////////////////////
	
	/**
	 * Notify all listeners that something has changed
	 */
	private void changed() {
		
		if (mModelListeners == null || mModelListeners.size() == 0) {
			log.warn(Lang.text("err_listeners_attached"));
			return;
		}
		
		for (IModelListener l : mModelListeners)
			l.modelChanged();
	}
	
	/**
	 * Holds all model listeners
	 */
	private transient HashSet<IModelListener>	mModelListeners	= new HashSet<IModelListener>();
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.ru.jtimmerm.orm.IModelObservable#addModelListener(nl.ru.jtimmerm.orm
	 * .IModelListener)
	 */
	@Override
	public void addModelListener(IModelListener mcl) {
		mModelListeners.add(mcl);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.ru.jtimmerm.orm.IModelObservable#removeModelListeners(nl.ru.jtimmerm
	 * .orm.IModelListener)
	 */
	@Override
	public void removeModelListeners(IModelListener mcl) {
		mModelListeners.remove(mcl);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.ru.jtimmerm.orm.IModelObservable#removeAllListeners(nl.ru.jtimmerm
	 * .orm.IModelListener)
	 */
	@Override
	public void removeAllListeners(IModelListener mcl) {
		mModelListeners.clear();
	}
	
	// ////////////////////////////////////////////////////////////////////////
	// LOG
	// ////////////////////////////////////////////////////////////////////////
	
	private final static Logger	log	= Logger.getLogger(Model.class);
	
}
