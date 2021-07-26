package automail;



import automail.Robot.RobotState;
import exceptions.ExcessiveDeliveryException;

public class Robot_Overdrive implements Robot_Strategy{
	/**
	 * This is the implementation of robot strategy with potential OVERDRIVING feature
	 * Here only the OVERDRIVE feature is newly added, all others are same as what class Normal_Robot does
	 * @author Wuji Zhu
	 * */
	
	
	public enum Overdrive_State  {OVERDRIVING, COOLING, NORMAL};
	private Overdrive_State _O_State = Overdrive_State.NORMAL;
	public int CoolingCount = 0;
	
	/**
	 * Returning: as normal
	 * Waiting: Distinguish between WAITING->OVERDRVIGN / WAITING ->DELIVERING
	 * DELIVERING: Distinguish between OVERDRIVING delivering /  NORMAL delivering
	 * @param robot robot that undertaking the step
	 * @author Wuji Zhu
	 * */
	public void step(Robot robot) throws ExcessiveDeliveryException {
		Robot.RobotState current_state = robot.current_state;
		switch(current_state) {
    		/** This state is triggered when the robot is returning to the mailroom after a delivery */
			//returning same as normal
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
            //waiting same as normal
            //But distinguish whether is waiting to OVERDRIVING or waiting to DELIVERIGN
            //and give corresponding indicating info
    		case WAITING:
                /** If the StorageTube is ready and the Robot is waiting in the mailroom then start the delivery */
                if(!robot.isEmpty() && robot.receivedDispatch){
                	robot.receivedDispatch = false;
                	robot.deliveryCounter = 0; // reset delivery counter
        			robot.setRoute();
        			if(_O_State == Robot_Overdrive.Overdrive_State.OVERDRIVING) {
        				System.out.printf("T: %3d > %7s changed from %s to %s%n", Clock.Time(), robot.getIdTube(), robot.current_state, _O_State);
        				robot.changeState(RobotState.DELIVERING, false);
        			}
        			else {
        				robot.changeState(RobotState.DELIVERING, true);
        			}
                	
                }
                break;
            //1. normal delivering state
            //2. delivering and OVERDRIVING
            //3. finish delivering but cooling
    		case DELIVERING:
    			
    			if(_O_State == Robot_Overdrive.Overdrive_State.NORMAL) {
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
        			} 
    				else {
    	        		/** The robot is not at the destination yet, move towards it! */
    	                robot.moveTowards(robot.destination_floor);
        			}
    			}
    			
    			//Cooling stage: 1.back count cooling
    			//             : 2.finish cooling, change to returning
    			else if (_O_State == Overdrive_State.COOLING){
    				if(CoolingCount > 0) {
                    		CoolingCount --;
                    	}
                    	else {
                    		robot.changeState(RobotState.RETURNING, false);
                    		System.out.printf("T: %3d > %7s changed from %s to %s%n", Clock.Time(), robot.getIdTube(), _O_State, robot.current_state);
                    		change_O_State(Overdrive_State.NORMAL);
                    	}
    			}
    			
    			else{
    				if(robot.current_floor == robot.destination_floor) {
    					robot.delivery.deliver(robot.deliveryItem);
	                    robot.deliveryItem = null;
	                    robot.deliveryCounter++;
	                    start_Cooling(robot);
    				}
    				else {
    					robot.moveTowards(robot.destination_floor, 2);
    				}
    			}
    			break;
    	}
	}
	/**
	 * changing the _O_State
	 * @param  _O_State the current _O_State
	 * */
	public void change_O_State(Overdrive_State _O_State) {
		this._O_State = _O_State;
	}
	
	/**
	 * Function to initiates cooling
	 * Initiates the cooling counts
	 * @param robot the robot that start cooling
	 * */
	public void start_Cooling(Robot robot) {
		System.out.printf("T: %3d > %7s changed from %s to %s%n", Clock.Time(), robot.getIdTube(), _O_State ,Overdrive_State.COOLING);
		change_O_State(Overdrive_State.COOLING);
		CoolingCount = 4;
	}
	
	
	
}

	

