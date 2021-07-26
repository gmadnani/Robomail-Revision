package strategies;

import java.util.LinkedList;
import java.util.ListIterator;

import automail.MailItem;
import automail.PriorityMailItem;
import automail.Robot;
import automail.Robot_Overdrive;
import exceptions.ItemTooHeavyException;

public class Overdrive_Deliver implements Delivering_Strategy{
	/**
	 * The function follow the algorithm:
	 * 		1.since the first one must have the highest priority
	 * 		so load it if the mail pool has one mail
	 * 		2.if this mail is an urgent one(needs OVERDRIVE),send
	 * 		it out and make the robot OVERDRIVIGN immediately
	 * 		3.if not, check if it has more mails and load it
	 * 		
	 * 		@param i lists of robots
	 * 		@param pool lists of mails
	 * 		@author Wuji Zhu
	 * */
	
	
	public void loadRobot(ListIterator<Robot> i, LinkedList<MailPool.Item> pool) throws ItemTooHeavyException{
		
		Robot robot = i.next();
		assert(robot.isEmpty());
		ListIterator<MailPool.Item> j = pool.listIterator();
		
		
		if (pool.size() > 0) {
			try {
				//if there are mails left, add one to hand first
				//Algorithm step 1
				MailItem _j = j.next().mailItem;
				robot.addToHand(_j); 
				j.remove();
				
				
				//Algorithm step 2
				if(_j instanceof PriorityMailItem && urgent((PriorityMailItem) _j)) {
					//switch on the Overdrive Mode
					Robot_Overdrive strategy = (Robot_Overdrive) robot.step_strategy;
					strategy.change_O_State(Robot_Overdrive.Overdrive_State.OVERDRIVING);
				}
				
				//Algorithm step 3
				else if(pool.size()>0){
					//there is no urgent one normal send
					
						robot.addToTube(j.next().mailItem);
						j.remove();
				}
				
			robot.dispatch(); // send the robot off if it has any items to deliver
			i.remove();       // remove from mailPool queue
			} catch (Exception e) { 
	            throw e; 
	        } 
		}
	}
	//checking if the mail needs to be OVERDRIVED by an robot
	public static boolean urgent(PriorityMailItem mailitem) {
		return (mailitem.getPriorityLevel() > 50);
	}

}
