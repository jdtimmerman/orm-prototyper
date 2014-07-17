package nl.ru.jtimmerm.view.gui;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nl.ru.jtimmerm.orm.IModel;
import nl.ru.jtimmerm.orm.IModelListener;
import nl.ru.jtimmerm.orm.types.IType;
import nl.ru.jtimmerm.view.IComponentContainer;
import nl.ru.jtimmerm.view.ISelectListener;
import nl.ru.jtimmerm.view.Selection;

import org.apache.log4j.Logger;

public class ListPanel extends JList implements IComponentContainer,
		IModelListener, ISelectListener {

	private static final long serialVersionUID = 6366330271528311211L;

	// ////////////////////////////////////////////////////////////////////////
	// CONSTRUCT
	// ////////////////////////////////////////////////////////////////////////

	private IModel mModel;

	public ListPanel(IModel model) {
		super();
		mModel = model;

		setPreferredSize(new Dimension(150, 100));
		Selection.getSingleton().addSelectListener(this);
		addListSelectionListener(listSelectionListener);
	}

	@Override
	public Component getComponent() {
		return this;
	}

	// ////////////////////////////////////////////////////////////////////////
	// LISTENING
	// ////////////////////////////////////////////////////////////////////////

	@Override
	public void modelChanged() {
		removeListSelectionListener(listSelectionListener);

		IType[] types = new IType[0];
		types = mModel.getTypes().toArray(types);
		setListData(types);

		addListSelectionListener(listSelectionListener);
		validate();

	}

	@Override
	public void selectionChanged() {
		removeListSelectionListener(listSelectionListener);

		if (Selection.getSingleton().hasSelectedShape())
			setSelectedValueSilent(Selection.getSingleton().getSelectedShape());
		else
			clearSelection();

		addListSelectionListener(listSelectionListener);
		repaint();
	}

	private transient ListSelectionListener listSelectionListener = new ListSelectionListener() {
		@Override
		public void valueChanged(ListSelectionEvent arg0) {
			Selection.getSingleton().select((IType) getSelectedValue());
		}
	};

	// ////////////////////////////////////////////////////////////////////////
	// SILENT ON EXTERNAL SELECTION http://stackoverflow.com/a/3093172/689893

	private boolean isSilent = false;

	public void setSelectedValueSilent(Object object) {
		isSilent = true;
		setSelectedValue(object, true);
		isSilent = false;
	}

	@Override
	protected void fireSelectionValueChanged(int firstIndex, int lastIndex,
			boolean isAdjusting) {
		if (!isSilent)
			super.fireSelectionValueChanged(firstIndex, lastIndex, isAdjusting);
	}

	// ////////////////////////////////////////////////////////////////////////
	// LISTENERS
	// ////////////////////////////////////////////////////////////////////////

	// private HashSet<IViewListener> mListeners = new HashSet<IViewListener>();
	//
	// @Override
	// public void addViewListener(IViewListener l) {
	// mListeners.add(l);
	// }
	//
	// @Override
	// public void removeViewListeners(IViewListener l) {
	// mListeners.remove(l);
	// }
	//
	// @Override
	// public void removeAllListeners() {
	// mListeners.clear();
	// }

	// ////////////////////////////////////////////////////////////////////////
	// LOG
	// ////////////////////////////////////////////////////////////////////////

	protected static Logger log = Logger.getLogger(ListPanel.class);

}
