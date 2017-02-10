import java.util.ArrayList;
import java.util.List;


public class Strip {
	
	private int segmentIndex;
	private int stripIndex;
	
	private List<Vehicle> vehicleList= new ArrayList<Vehicle>();
	
	public Strip(int segIndex, int strIndex)
	{
		segmentIndex = segIndex;
		stripIndex = strIndex;
	}
	public int getStripIndex()
	{
		return stripIndex;
	}
	public void addVehicle(Vehicle v)
	{
		vehicleList.add(v);
	}
	public void delVehicle(Vehicle v)
	{
		vehicleList.remove(v);
	}
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
        public boolean isGapforForwardMovement(Vehicle v)
	{
		double thresholdDistance = 0.12;
		double upperLimit = v.getDistance() + v.getLength() + v.getSpeed()+ thresholdDistance;
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
