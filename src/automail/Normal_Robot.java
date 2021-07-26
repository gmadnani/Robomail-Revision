package automail;

import automail.Robot.RobotState;
import exceptions.ExcessiveDeliveryException;

public class Normal_Robot implements Robot_Strategy{
	/**
	 * This implementation is just changing some local variable of Robot to robot.(local variable)
	 * Since the project is for checking the OVERDRIVEING feature, this class is just for preserving the 
	 * original feature
	 * @author Wuji Zhu
	 * */ 
	
	//Reference: the code is mostly copy from original code 
	
	public void step(Robot robot) throws ExcessiveDeliveryException {    	
    	Robot.RobotState current_state = robot.current_state;
		switch(current_state) {
    		/** This state is triggered when the robot is returning to the mailroom after a delivery */
    		case RETURNING:
    			/** If its current position is at the mailroom, then the robot should change state */
                if(robot.current_floor == Building.MAILROOM_LOCATION){
                	if (robot.tube != null) {
                		robot.mailPool.addToPool(robot.tube);
                        System.out.printf("T: %3d > old addToPool [%s]%n", Clock.Time(), robot.tube.toString());
                        robot.tube = null;
                	}
        			/** Tell the sorter the robot is ready */
        			robot.mailPool.registerWaiting(robot);
                	robot.changeState(RobotState.WAITING, true);
                } else {
                	/** If the robot is not at the mailroom floor yet, then move towards it! */
                    robot.moveTowards(Building.MAILROOM_LOCATION);
                	break;
                }
    		case WAITING:
                /** If the StorageTube is ready and the Robot is waiting in the mailroom then start the delivery */
                if(!robot.isEmpty() && robot.receivedDispatch){
                	robot.receivedDispatch = false;
                	robot.deliveryCounter = 0; // reset delivery counter
        			robot.setRoute();
                	robot.changeState(RobotState.DELIVERING, true);
                }
                break;
    		case DELIVERING:
    			if(robot.current_floor == robot.destination_floor){ // If already here drop off either way
                    /** Delivery complete, report this to the simulator! */
                    robot.delivery.deliver(robot.deliveryItem);
                    robot.deliveryItem = null;
                    robot.deliveryCounter++;
                    if(robot.deliveryCounter > 2){  // Implies a simulation bug
                    	throw new ExcessiveDeliveryException();
                    }
                    /** Check if want to return, i.e. if there is no item in the tube*/
                    if(robot.tube == null){
                    	robot.changeState(RobotState.RETURNING, true);
                    }
                    else{
                        /** If there is another item, set the robot's route to the location to deliver the item */
                        robot.deliveryItem = robot.tube;
                        robot.tube = null;
                        robot.setRoute();
                        robot.changeState(RobotState.DELIVERING, true);
                    }
    			} else {
	        		/** The robot is not at the destination yet, move towards it! */
	                robot.moveTowards(robot.destination_floor);
    			}
                break;
    	}
    }
}
