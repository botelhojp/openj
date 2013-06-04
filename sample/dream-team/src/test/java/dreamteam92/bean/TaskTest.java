package dreamteam92.bean;

import static junit.framework.Assert.*;
import jade.core.AID;

import org.junit.Test;

public class TaskTest {

	@Test
	public void test1() {
		Task task = new Task("principal");
		task.addSubTask(new Task("sub01"));
		task.addSubTask(new Task("sub02"));
		task.addSubTask(new Task("sub03"));
		try {
			task.execute(5, 9);
			fail("exception not throw");
		} catch (RuntimeException e) {
			assertEquals("subtasks is not empty", e.getMessage());
		}
	}

	@Test
	public void test2() {
		Task task = new Task("principal");
		task.addSubTask(new Task("sub01"));
		task.addSubTask(new Task("sub02"));
		task.addSubTask(new Task("sub03"));
		for (Task t : task.getSubtasks()) {
			t.execute(30, 40);
		}
		assertEquals(30, task.getCompleted());
		assertEquals(40, task.getQuality());		
	}
	
	@Test
	public void test3() {
		Task task = new Task("principal", "A", "B", "C");
		
		assertTrue(task.getAbilities().contains("A"));
		assertTrue(task.getAbilities().contains("B"));
		assertTrue(task.getAbilities().contains("C"));
		
		task.addSubTask(new Task("sub1", "D"));
		task.addSubTask(new Task("sub2", "E"));
		task.addSubTask(new Task("sub3", "F"));
		
		assertTrue(task.getAbilities().contains("D"));
		assertTrue(task.getAbilities().contains("E"));		
		assertTrue(task.getAbilities().contains("F"));
		
	}
	
	
	
	@Test
	public void test4() {
		Task task = new Task("principal");
		assertFalse(task.isReserved());
		
		task.setOwnerAID(new AID("AA", true));
		assertTrue(task.isReserved());
		
		Task sub = new Task("sub1");
		
		task.addSubTask(sub);
		assertFalse(task.isReserved());
		sub.setOwnerAID(new AID("AA", true));
		assertTrue(task.isReserved());
		
				
	}
	
	@Test
	public void testMerge1(){
		Task t1 = new Task("t1");
		Task t2 = new Task("t1");
		t2.setOwnerAID(new AID("aid1", true));
		t1.merge(t2);
		assertEquals(t1.getOwnerAID().toString(), "( agent-identifier :name aid1 )");
	}

	
	@Test
	public void testMerge2(){
		Task t1 = new Task("t1");
		t1.setOwnerAID(new AID("aid1", true));
		t1.execute(11, 111);
		
		Task t2 = new Task("t1");
		t2.setOwnerAID(new AID("aid2", true));
		t2.execute(22, 222);
		
		t1.merge(t2);
		assertEquals(t1.getOwnerAID().toString(), "( agent-identifier :name aid1 )");
	}

	
}
