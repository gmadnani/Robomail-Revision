package automail;

import exceptions.ExcessiveDeliveryException;

public interface Robot_Strategy {
	
	//the interface is for various Robot_Strategy pattern(how robot step moves) 
	void step(Robot robot) throws ExcessiveDeliveryException;
}
