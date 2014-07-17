package nl.ru.jtimmerm.orm;

import java.util.Set;

import junit.framework.TestCase;
import nl.ru.jtimmerm.orm.types.IType;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class FindTypesTest extends TestCase {
	
	@Rule
	public ExpectedException	exception	= ExpectedException.none();
	
	private IModel		model		= null;
	
	@Before
	public void init() {
		model = new Model();
	}
	
	@Test
	public void testFind() throws OrmException {
		model.analyzeSentence("Person eats Fruit");
		
		Set<? extends IType> types = model.getTypes();
		assertEquals(3, types.size());
		
		assertEquals("Person", model.findObjectType("Person").getContent());
		assertEquals("Fruit", model.findObjectType("Fruit").getContent());
		assertEquals("Person eats Fruit", model.findFactType("Person eats Fruit").verbalize());
		assertEquals("%s eats %s", model.findFactType("Person eats Fruit").getVerbalization());
	}
	
	@Test
	public void testNotFound() throws OrmException {
		model.analyzeSentence("Person eats Fruit");
		
		Set<? extends IType> types = model.getTypes();
		assertEquals(3, types.size());
		
		exception.expect(OrmException.class);
		model.findFactType("Some not existing FactType");
		exception.expect(OrmException.class);
		model.findObjectType("NotExistingObjectType");
		
	}
	
}
