package automail;

public class Statistics {
	/**
	 * The class for collecting required statistics
	 * Print what has been collected if required
	 * @author Wuji Zhu
	 * */
	public static int normal_package_count = 0;
	public static int overdrive_package_count = 0;
	public static int normal_package_weight = 0;
	public static int overdrive_package_weight = 0;
	
	public Statistics() {};
	
	public static void showStatistics() {
    	System.out.println("\nStatistics: \n");
    	System.out.println("The number of packages delivered normally: "+normal_package_count);
    	System.out.println("The number of packages delivered using overdrive: "+overdrive_package_count);
    	System.out.println("The total weight of the packages delivered normally: "+normal_package_weight);
    	System.out.println("The total weight of the packages delivered using overdrive: "+overdrive_package_weight);
    }
	
}
