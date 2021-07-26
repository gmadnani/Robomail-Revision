package strategies;

import java.util.LinkedList;
import java.util.ListIterator;

import automail.Robot;
import exceptions.ItemTooHeavyException;

/**
 * Since in OVERDRIVE mode, there are different delivering strategy as the normal one
 * And in the future there might be more delivering strategy
 * So this interface is the strategy pattern for delivering mail
 * @author Wuji Zhu 
 * */
public interface Delivering_Strategy {
	
	
	
	void loadRobot(ListIterator<Robot> i, LinkedList<MailPool.Item> pool) throws ItemTooHeavyException;
}
