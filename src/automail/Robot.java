package automail;

import exceptions.ExcessiveDeliveryException;
import exceptions.ItemTooHeavyException;
import strategies.IMailPool;
import java.util.Map;
import java.util.TreeMap;

/**
 * The robot delivers mail!
 */
public class Robot {
	
    static public final int INDIVIDUAL_MAX_WEIGHT = 2000;

    IMailDelivery delivery;
    protected final String id;
    /** Possible states the robot can be in */
    public enum RobotState { DELIVERING, WAITING, RETURNING}
    public RobotState current_state;
    int current_floor;
    int destination_floor;
    IMailPool mailPool;
    boolean receivedDispatch;
    
    public MailItem deliveryItem = null;
    MailItem tube = null;
    
    int deliveryCounter;
    
    public Robot_Strategy step_strategy;
    
    
    /**
     * Initiates the robot's location at the start to be at the mailroom
     * also set it to be waiting for mail.
     * 
     * Also, initiates the strategy pattern Robot_Strategy which indicate the strategy robot make step
     * 
     * Robot_Overdeive : indicate robot step with OVERDRIVE potential
     * Normal_Robot : indicate robot step as normal defined 
     * 
     * @param behaviour governs selection of mail items for delivery and behaviour on priority arrivals
     * @param delivery governs the final delivery
     * @param mailPool is the source of mail items
     * @param step_strategy is strategy robot make step
     */
    public Robot(IMailDelivery delivery, IMailPool mailPool, Robot_Strategy step_strategy){
    	id = "R" + hashCode();
        // current_state = RobotState.WAITING;
    	current_state = RobotState.RETURNING;
        current_floor = Building.MAILROOM_LOCATION;
        this.delivery = delivery;
        this.mailPool = mailPool;
        this.receivedDispatch = false;
        this.deliveryCounter = 0;
        if(step_strategy instanceof Robot_Overdrive) {
        	this.step_strategy = new Robot_Overdrive();
        }
        else {
        	this.step_strategy = new Normal_Robot();
        }
    }
    
    public void dispatch() {
    	receivedDispatch = true;
    }

    /**
     * Sets the route for the robot
     */
    void setRoute() {
        /** Set the destination floor */
        destination_floor = deliveryItem.getDestFloor();
    }

    /**
     * Generic function that moves the robot towards the destination
     * @param destination the floor towards which the robot is moving
     */
    void moveTowards(int destination) {
        if(current_floor < destination){
            current_floor++;
        } else {
            current_floor--;
        }
    }
    
    String getIdTube() {
    	return String.format("%s(%1d)", id, (tube == null ? 0 : 1));
    }
    
	public MailItem getTube() {
		return tube;
	}
    
	static private int count = 0;
	static private Map<Integer, Integer> hashMap = new TreeMap<Integer, Integer>();

	@Override
	public int hashCode() {
		Integer hash0 = super.hashCode();
		Integer hash = hashMap.get(hash0);
		if (hash == null) { hash = count++; hashMap.put(hash0, hash); }
		return hash;
	}

	public boolean isEmpty() {
		return (deliveryItem == null && tube == null);
	}

	public void addToHand(MailItem mailItem) throws ItemTooHeavyException {
		assert(deliveryItem == null);
		deliveryItem = mailItem;
		if (deliveryItem.weight > INDIVIDUAL_MAX_WEIGHT) throw new ItemTooHeavyException();
	}

	public void addToTube(MailItem mailItem) throws ItemTooHeavyException {
		assert(tube == null);
		tube = mailItem;
		if (tube.weight > INDIVIDUAL_MAX_WEIGHT) throw new ItemTooHeavyException();
	}
	
	
	
	//From this line below it the new function added from original source code
	/**
     * This is called on every time step
     * @throws ExcessiveDeliveryException if robot delivers more than the capacity of the tube without refilling
     * @author Wuji Zhu
     */
    public void step() throws ExcessiveDeliveryException {    	
    	step_strategy.step(this);
    }
	
	/**
     * Overloading move functions so that move toward the destination with a mininmum step limit
     * (i.e. for the 'OVAERDRIVE' minimum step is 2)
     * 
     * */
    void moveTowards(int destination, int min_step) {
    	if(current_floor < destination) {
    		current_floor += Math.min(min_step, destination - current_floor);
    	}
    	else {
    		current_floor -= Math.min(min_step, current_floor - destination);
    	}
    }
    
    /**
     * Change the state
     * Through overload, check whether print out the changes
     * @param nextState the state to which the robot is transitioning
     * @param indicate_info whether give the info or not
     */
    public void changeState(RobotState nextState, Boolean indicate_info){
    	assert(!(deliveryItem == null && tube != null));
    	if (current_state != nextState && indicate_info) {
            System.out.printf("T: %3d > %7s changed from %s to %s%n", Clock.Time(), getIdTube(), current_state, nextState);
    	}
    	current_state = nextState;
    	if(nextState == RobotState.DELIVERING && indicate_info){
            System.out.printf("T: %3d > %7s-> [%s]%n", Clock.Time(), getIdTube(), deliveryItem.toString());
    	}
    }
}
