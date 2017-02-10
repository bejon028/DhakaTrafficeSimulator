import java.util.ArrayList;
import java.util.List;

/**
 * We consider strip as fundamental unit of path.
 * Formation hierarchy: path->link->segment->strip
 * Each strip is identified by its segment index and strip index. Strips in a segment are indexed from left to right.
 * 
 * @author USER
 */

public class Strip {
	
	private int segmentIndex;
	private int stripIndex;
	
	private List<Vehicle> vehicleList= new ArrayList<Vehicle>();
	
        //Constructor sets segment index and strip index
	public Strip(int segIndex, int strIndex)
	{
		segmentIndex = segIndex;
		stripIndex = strIndex;
	}
        
        //gets strip index
	public int getStripIndex()
	{
		return stripIndex;
	}
        
        //adds vehicle to the strip's vehicle list when vehicle comes over the strip
	public void addVehicle(Vehicle v)
	{
		vehicleList.add(v);
	}
        
        //removes vehicle from the strip's vehicle list when vehicle lefts the strip 
	public void delVehicle(Vehicle v)
	{
		vehicleList.remove(v);
	}
        
        //for a vehicle on this strip,finds another vehicle on the same strip with minimum distance ahead.
	public Vehicle probableLeader(double distance, double length)
	{
		double min = 9999;
		Vehicle ret = null;
		for(int i=0; i<vehicleList.size();i++)
		{
			if(vehicleList.get(i).getDistance()>distance+0.1)
			{
				double compare = vehicleList.get(i).getDistance()-distance;
				if(compare<min)
				{
					min=compare;
					ret=vehicleList.get(i);
				}					
			}
		}
		return ret;
	}
        
        //checks whether there is space for a vehicle to move forward without a collision 
        //and keeping a threshold distance
        public boolean isGapforForwardMovement(Vehicle v)
	{
		double thresholdDistance = 0.12;
		double upperLimit = v.getDistance() + v.getLength() + v.getSpeed() + thresholdDistance;
		double lowerLimit = v.getDistance();
		for(int sp=0; sp<vehicleList.size(); sp++)
		{
			if(vehicleList.get(sp)!=v && (vehicleList.get(sp).getDistance()>lowerLimit && vehicleList.get(sp).getDistance()<upperLimit ||
					vehicleList.get(sp).getDistance()+vehicleList.get(sp).getLength()>lowerLimit && vehicleList.get(sp).getDistance()+
					vehicleList.get(sp).getLength()<upperLimit))
				return false;
		}
		return true;

	}
        
        //checks whether there is adequate space for adding a new vehicle 
	public boolean isGapforAddingVehicle(double vehicleLength)
	{
		double lowerLimit = 0.08;
                double upperLimit = 0.6+vehicleLength;
		for(int sp=0; sp<vehicleList.size(); sp++)
		{
			if(vehicleList.get(sp).getDistance()<upperLimit && 
					vehicleList.get(sp).getDistance()>lowerLimit)
				return false;
		}
		return true;	
		
	}
        
        /*similar to isGapForMoveForward but doesn't consider vehicle speed, checks whether there  
        *is enough space for a given vehicle forward movement.
        */
	public boolean isGapForStripChange(Vehicle v)
	{
		double thresholdDistance = 0.1;
		double lowerLimit1 = v.getDistance() - thresholdDistance;
		double upperLimit1 = v.getDistance() + v.getLength() + thresholdDistance;
		for(int sp=0; sp<vehicleList.size(); sp++)
		{
                        double lowerLimit2 = vehicleList.get(sp).getDistance() - thresholdDistance;
                        double upperLimit2 = vehicleList.get(sp).getDistance() +
                               vehicleList.get(sp).getLength() + thresholdDistance;
			if((lowerLimit1>=lowerLimit2 && lowerLimit1<=upperLimit2 ||
                            upperLimit1>=lowerLimit2 && upperLimit1<=upperLimit2)||
                           (lowerLimit2>=lowerLimit1 && lowerLimit2<=upperLimit1 ||
                            upperLimit2>=lowerLimit1 && upperLimit2<=upperLimit1))
				return false;
		}
		return true;	
		
	}
}
