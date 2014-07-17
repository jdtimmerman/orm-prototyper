package nl.ru.jtimmerm.view.gui.dialogue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JTable;

import junit.framework.TestCase;
import nl.ru.jtimmerm.orm.types.IObjectType;
import nl.ru.jtimmerm.orm.types.MultiUnicityConstraint;
import nl.ru.jtimmerm.orm.types.ObjectType;
import nl.ru.jtimmerm.orm.types.UnicityConstraint;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class UnicityConstraintQuestionTest extends TestCase {
	
	@Test
	public void testUC_10() {
		JTable table = getTwoRoleTable(new boolean[] { true, false, true, true });
		MultiUnicityConstraint muc = UnicityDetermination.calculateUnicity(table, getObjects(2));
		
		assertEquals(1, muc.getNumberOfConstraints());
		UnicityConstraint uc = muc.getConstraints().first();
		assertEquals(true, uc.getConstraintRoles()[0]);
		assertEquals(false, uc.getConstraintRoles()[1]);
	}
	
	@Test
	public void testUC_01() {
		JTable table = getTwoRoleTable(new boolean[] { true, true, false, true });
		MultiUnicityConstraint muc = UnicityDetermination.calculateUnicity(table, getObjects(2));
		
		assertEquals(1, muc.getNumberOfConstraints());
		UnicityConstraint uc = muc.getConstraints().first();
		assertEquals(false, uc.getConstraintRoles()[0]);
		assertEquals(true, uc.getConstraintRoles()[1]);
	}
	
	@Test
	public void testUC_11() {
		JTable table = getTwoRoleTable(new boolean[] { true, true, true, true });
		MultiUnicityConstraint muc = UnicityDetermination.calculateUnicity(table, getObjects(2));
		
		assertEquals(1, muc.getNumberOfConstraints());
		UnicityConstraint uc = muc.getConstraints().first();
		assertEquals(true, uc.getConstraintRoles()[0]);
		assertEquals(true, uc.getConstraintRoles()[1]);
	}
	
	// //////////////////////////////////////////////////////////////
	
	@Test
	public void testUC_110() {
		JTable table = getThreeRoleTable(new boolean[] { true, false, true, true, true, true, true, true });
		MultiUnicityConstraint muc = UnicityDetermination.calculateUnicity(table, getObjects(3));
		
		assertEquals(1, muc.getNumberOfConstraints());
		UnicityConstraint uc = muc.getConstraints().first();
		assertEquals(true, uc.getConstraintRoles()[0]);
		assertEquals(true, uc.getConstraintRoles()[1]);
		assertEquals(false, uc.getConstraintRoles()[2]);
	}
	
	@Test
	public void testUC_101() {
		JTable table = getThreeRoleTable(new boolean[] { true, true, false, true, true, true, true, true });
		MultiUnicityConstraint muc = UnicityDetermination.calculateUnicity(table, getObjects(3));
		
		assertEquals(1, muc.getNumberOfConstraints());
		UnicityConstraint uc = muc.getConstraints().first();
		assertEquals(true, uc.getConstraintRoles()[0]);
		assertEquals(false, uc.getConstraintRoles()[1]);
		assertEquals(true, uc.getConstraintRoles()[2]);
	}
	
	@Test
	public void testUC_111() {
		JTable table = getThreeRoleTable(new boolean[] { true, true, true, true, true, true, true, true });
		MultiUnicityConstraint muc = UnicityDetermination.calculateUnicity(table, getObjects(3));
		
		assertEquals(1, muc.getNumberOfConstraints());
		UnicityConstraint uc = muc.getConstraints().first();
		assertEquals(true, uc.getConstraintRoles()[0]);
		assertEquals(true, uc.getConstraintRoles()[1]);
		assertEquals(true, uc.getConstraintRoles()[2]);
	} 
	
	@Test
	public void testUC_101_110() {
		JTable table = getThreeRoleTable(new boolean[] { true, false, false, true, true, true, true, true });
		MultiUnicityConstraint muc = UnicityDetermination.calculateUnicity(table, getObjects(3));
		
		Iterator<UnicityConstraint> it = muc.getConstraints().iterator();
		assertEquals(2, muc.getNumberOfConstraints());
		UnicityConstraint uc = it.next();
		assertEquals(true, uc.getConstraintRoles()[0]);
		assertEquals(false, uc.getConstraintRoles()[1]);
		assertEquals(true, uc.getConstraintRoles()[2]);
		
		uc = it.next();
		assertEquals(true, uc.getConstraintRoles()[0]);
		assertEquals(true, uc.getConstraintRoles()[1]);
		assertEquals(false, uc.getConstraintRoles()[2]);
	}
	
	
	
	@Test
	public void testUC_101_011() {
		JTable table = getThreeRoleTable(new boolean[] { true, true, false, false, true, true, true, true });
		MultiUnicityConstraint muc = UnicityDetermination.calculateUnicity(table, getObjects(3));
		
		Iterator<UnicityConstraint> it = muc.getConstraints().iterator();
		assertEquals(2, muc.getNumberOfConstraints());
		UnicityConstraint uc = it.next();
		assertEquals(false, uc.getConstraintRoles()[0]);
		assertEquals(true, uc.getConstraintRoles()[1]);
		assertEquals(true, uc.getConstraintRoles()[2]);
		
		uc = it.next();
		assertEquals(true, uc.getConstraintRoles()[0]);
		assertEquals(false, uc.getConstraintRoles()[1]);
		assertEquals(true, uc.getConstraintRoles()[2]);
	}
	
	// //////////////////////////////////////////////////////////////
	
	private List<IObjectType> getObjects(int n) {
		ArrayList<IObjectType> ot = new ArrayList<IObjectType>();
		
		for (int i = 1; i <= n; i++)
			ot.add(new ObjectType(""));
		
		return ot;
	}
	
	private JTable getTwoRoleTable(boolean[] values) {
		Object[][] data = new Object[4][];
		int i = -1;
		data[++i] = new Object[] { "A", "A", values[i] };
		data[++i] = new Object[] { "A", "B", values[i] };
		data[++i] = new Object[] { "B", "A", values[i] };
		data[++i] = new Object[] { "B", "B", values[i] };
		
		String[] columnTitles = new String[] { "A", "B", "Possible" };
		
		return new JTable(data, columnTitles);
	}
	
	private JTable getThreeRoleTable(boolean[] values) {
		Object[][] data = new Object[8][];
		int i = -1;
		data[++i] = new Object[] { "A", "A", "A", values[i] };
		data[++i] = new Object[] { "A", "A", "B", values[i] };
		data[++i] = new Object[] { "A", "B", "A", values[i] };
		data[++i] = new Object[] { "B", "A", "A", values[i] };
		data[++i] = new Object[] { "A", "B", "B", values[i] };
		data[++i] = new Object[] { "B", "A", "B", values[i] };
		data[++i] = new Object[] { "B", "B", "A", values[i] };
		data[++i] = new Object[] { "B", "B", "B", values[i] };
		
		String[] columnTitles = new String[] { "A", "B", "C", "Possible" };
		
		return new JTable(data, columnTitles);
	}
}
