package nl.ru.jtimmerm.view;

import java.util.HashSet;
import java.util.Set;

import nl.ru.jtimmerm.Lang;
import nl.ru.jtimmerm.orm.types.IType;

import org.apache.log4j.Logger;

/**
 * Handles selections
 * 
 * TODO: Move selection to model
 * 
 * @author joost
 * 
 */
public class Selection implements ISelection<IType> {

	/**
	 * Constructor is private, use singleton
	 */
	private Selection() {

	}

	// ////////////////////////////////////////////////////////////////////////
	// SINGLETON
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * The singleton
	 */
	private static Selection mSingleton = null;

	/**
	 * Get the singleton
	 * 
	 * @return
	 */
	public static Selection getSingleton() {

		if (mSingleton == null)
			mSingleton = new Selection();

		return mSingleton;
	}

	// ////////////////////////////////////////////////////////////////////////
	// MANIPULATE SELECTION
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * The currently active selection
	 */
	private IType selectedShape = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.view.ISelection#select(java.lang.Object)
	 */
	@Override
	public void select(IType shape) {

		boolean changed = false;

		if (shape != null && selectedShape != shape) {
			log.debug(Lang.text("selecting", shape.toString()));
		} else if (selectedShape != null) {
			log.debug(Lang.text("deselecting"));
		}

		if (selectedShape != shape)
			changed = true;

		if (!(selectedShape == null && shape == null))
			selectedShape = shape;

		if (changed)
			changed();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.view.ISelection#getSelectedShape()
	 */
	@Override
	public IType getSelectedShape() {
		return selectedShape;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.view.ISelection#hasSelectedShape()
	 */
	@Override
	public boolean hasSelectedShape() {
		return selectedShape != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.ru.jtimmerm.view.ISelection#isSelected(java.lang.Object)
	 */
	@Override
	public boolean isSelected(IType shape) {

		if (hasSelectedShape() && getSelectedShape().equals(shape))
			return true;

		return false;
	}

	// ////////////////////////////////////////////////////////////////////////
	// LISTENER
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Holds all listeners attached to this object
	 */
	private Set<ISelectListener> mListeners = new HashSet<ISelectListener>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.ru.jtimmerm.view.ISelection#addSelectListener(nl.ru.jtimmerm.view.
	 * ISelectListener)
	 */
	@Override
	public void addSelectListener(ISelectListener listener) {
		mListeners.add(listener);
	}

	/**
	 * Notify all listeners that a change occured
	 */
	private void changed() {
		for (ISelectListener l : mListeners)
			l.selectionChanged();
	}

	// ////////////////////////////////////////////////////////////////////////
	// LOG
	// ////////////////////////////////////////////////////////////////////////

	private final static Logger log = Logger.getLogger(Selection.class);

}
