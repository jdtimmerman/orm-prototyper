package nl.ru.jtimmerm.orm;

import java.util.ArrayList;
import java.util.Set;

import junit.framework.TestCase;
import nl.ru.jtimmerm.Settings;
import nl.ru.jtimmerm.orm.types.FactType;
import nl.ru.jtimmerm.orm.types.IFactType;
import nl.ru.jtimmerm.orm.types.IObjectType;
import nl.ru.jtimmerm.orm.types.IType;
import nl.ru.jtimmerm.orm.types.ObjectType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class AnalyzeSentenceTest extends TestCase {
	
	private IModel	model	= null;
	
	@Before
	public void init() {
		model = new Model();
		
	}
	
	@Test
	public void test2RoleFactType() throws OrmException {
		model.analyzeSentence("Person eats Fruit");
		
		Set<? extends IType> types = model.getTypes();
		assertEquals(3, types.size());
		
		IObjectType o1 = new ObjectType("Person");
		IObjectType o2 = new ObjectType("Fruit");
		ArrayList<IObjectType> ot = new ArrayList<IObjectType>();
		ot.add(o1);
		ot.add(o2);
		IFactType f = new FactType(ot, String.format("%1$s eats %1$s", Settings.string("ft_wildcard")));
		
		assertTrue(types.contains(o1));
		assertTrue(types.contains(o2));
		assertTrue(types.contains(f));
	}

	@Test
	public void test3RoleFactType() throws OrmException {
		model.analyzeSentence("an Application is tested using a some Code in a UnitTest");
		
		Set<? extends IType> types = model.getTypes();
		assertEquals(4, types.size());
		
		IObjectType o1 = new ObjectType("Application");
		IObjectType o2 = new ObjectType("Code");
		IObjectType o3 = new ObjectType("UnitTest");
		ArrayList<IObjectType> ot = new ArrayList<IObjectType>();
		ot.add(o1);
		ot.add(o2);
		ot.add(o3);
		IFactType f = new FactType(ot, String.format("an %1$s is tested using a some %1$s in a %1$s", Settings.string("ft_wildcard")));
		
		assertTrue(types.contains(o1));
		assertTrue(types.contains(o2));
		assertTrue(types.contains(f));
	}
	
	
}
