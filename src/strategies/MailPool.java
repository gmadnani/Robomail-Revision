package strategies;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.ListIterator;

import automail.MailItem;
import automail.PriorityMailItem;
import automail.Robot;
import exceptions.ItemTooHeavyException;

public class MailPool implements IMailPool {
	
	private Delivering_Strategy delivering_strategy;
	
	class Item {
		int priority;
		int destination;
		MailItem mailItem;
		// Use stable sort to keep arrival time relative positions
		
		public Item(MailItem mailItem) {
			priority = (mailItem instanceof PriorityMailItem) ? ((PriorityMailItem) mailItem).getPriorityLevel() : 1;
			destination = mailItem.getDestFloor();
			this.mailItem = mailItem;
		}
	}
	
	//The corresponding comparator of Item
		public class ItemComparator implements Comparator<Item> {
			@Override
			public int compare(Item i1, Item i2) {
				int order = 0;
				if (i1.priority < i2.priority) {
					order = 1;
				} else if (i1.priority > i2.priority) {
					order = -1;
				} else if (i1.destination < i2.destination) {
					order = 1;
				} else if (i1.destination > i2.destination) {
					order = -1;
				}
				return order;
			}
		}
	
	private LinkedList<Item> pool;
	private LinkedList<Robot> robots;

	public MailPool(int nrobots, Delivering_Strategy delivering_strategy){
		// Start empty
		pool = new LinkedList<Item>();
		robots = new LinkedList<Robot>();
		//also add the delivering strategy here
		this.delivering_strategy = delivering_strategy;
	}

	public void addToPool(MailItem mailItem) {
		Item item = new Item(mailItem);
		pool.add(item);
		pool.sort(new ItemComparator());
	}
	
	@Override
	public void registerWaiting(Robot robot) { // assumes won't be there already
		robots.add(robot);
	}
	
	
	//below this line are changed functions
	/**
	 * This step calls the strategy pattern it has for delivering mail
	 * @author Wuji Zhu
	 * */
	@Override
	public void step() throws ItemTooHeavyException {
		try{
			ListIterator<Robot> i = robots.listIterator();
			while (i.hasNext()) delivering_strategy.loadRobot(i, pool);
		} catch (Exception e) { 
            throw e; 
        } 
	}
	

}
