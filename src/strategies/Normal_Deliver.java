package strategies;

import java.util.LinkedList;
import java.util.ListIterator;

import automail.Robot;
import exceptions.ItemTooHeavyException;

public class Normal_Deliver implements Delivering_Strategy{
	
	/**
	 * The code here are just for preserving the original feature of delivering
	 * Since this project is just for checking the OVERDRIVE feature
	 * this class deliverer probably won't appear at all
	 * */
	//Reference: the code here are all copy paste from MailPool -> loadRobot
	public void loadRobot(ListIterator<Robot> i, LinkedList<MailPool.Item> pool) throws ItemTooHeavyException{
		
		Robot robot = i.next();
		assert(robot.isEmpty());
		// System.out.printf("P: %3d%n", pool.size());
		ListIterator<MailPool.Item> j = pool.listIterator();
		if (pool.size() > 0) {
			try {
			robot.addToHand(j.next().mailItem); // hand first as we want higher priority delivered first
			j.remove();
			if (pool.size() > 0) {
				robot.addToTube(j.next().mailItem);
				j.remove();
			}
			robot.dispatch(); // send the robot off if it has any items to deliver
			i.remove();       // remove from mailPool queue
			} catch (Exception e) { 
	            throw e; 
	        } 
		}
		
	};
	
}
