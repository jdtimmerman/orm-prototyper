package nl.ru.jtimmerm.view.graph.shapes;

import nl.ru.jtimmerm.Lang;
import nl.ru.jtimmerm.orm.types.IFactType;
import nl.ru.jtimmerm.orm.types.IObjectType;
import nl.ru.jtimmerm.orm.types.IType;
import nl.ru.jtimmerm.view.graph.AbstractShapeMapper;

import org.apache.log4j.Logger;

public class BatikShapeMapper extends
		AbstractShapeMapper<ISVGShape<? extends IType>> {

	public BatikShapeMapper() {
		super();
	}

	private ISVGShape<IFactType> createTypeShape(IFactType type) {
		return new FactTypeSVGShape(type, getNextGridPosition());
	}

	private ISVGShape<IObjectType> createTypeShape(IObjectType type) {
		return new ObjectTypeSVGShape(type, getNextGridPosition());
	}

	@Override
	public boolean handles(IType t) {

		if (t instanceof IFactType)
			return true;
		else if (t instanceof IObjectType)
			return true;

		log.warn(Lang.text("unsupported_class", t.getClass()));

		return false;
	}

	@Override
	protected ISVGShape<? extends IType> _createTypeShape(IType t) {
		if (t instanceof IFactType)
			return createTypeShape((IFactType) t);
		else if (t instanceof IObjectType)
			return createTypeShape((IObjectType) t);

		return null;
	}

	// ///////////////////////////////////////////////////////////////
	// LOG
	// ///////////////////////////////////////////////////////////////

	private static final Logger log = Logger.getLogger(BatikShapeMapper.class);
}
