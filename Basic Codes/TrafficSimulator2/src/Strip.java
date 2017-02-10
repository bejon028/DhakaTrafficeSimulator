import java.util.ArrayList;
import java.util.List;


public class Strip {
	
	private int segmentIndex;
	private int stripIndex;
	
	private List<Vehicle> vehicleList= new ArrayList<Vehicle>();
	private List<Pedestrian> pedestrianList= new ArrayList<Pedestrian>();
        
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
        public void addPedestrian(Pedestrian p)
	{
		pedestrianList.add(p);
	}
	public void delPedestrian(Pedestrian p)
	{
		pedestrianList.remove(p);
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
		double thresholdDistance = 0.5;
		double upperLimit = v.getDistance() + v.getLength() + v.getSpeed()+ thresholdDistance;
		double lowerLimit = v.getDistance();
		for(int sp=0; sp<vehicleList.size(); sp++)
		{
			if(vehicleList.get(sp)!=v && (vehicleList.get(sp).getDistance()>lowerLimit && vehicleList.get(sp).getDistance()<upperLimit ||
					vehicleList.get(sp).getDistance()+vehicleList.get(sp).getLength()>lowerLimit && vehicleList.get(sp).getDistance()+
					vehicleList.get(sp).getLength()<upperLimit))
				return false;
		}
                for(int sp=0; sp<pedestrianList.size(); sp++)
		{
			if( (pedestrianList.get(sp).getDistance()>lowerLimit && pedestrianList.get(sp).getDistance()<upperLimit ||
					pedestrianList.get(sp).getDistance()+pedestrianList.get(sp).getLength()>lowerLimit && pedestrianList.get(sp).getDistance()+
					pedestrianList.get(sp).getLength()<upperLimit))
				return false;
		}
		return true;

	}
        
        public boolean isGapforForwardMovement(Pedestrian p)
	{
		double thresholdDistance = 0.5;
		double upperLimit = p.getDistance() + p.getLength() + p.getSpeed()+ thresholdDistance;
		double lowerLimit = p.getDistance();
                for(int sp=0; sp<vehicleList.size(); sp++)
		{
			if( (vehicleList.get(sp).getDistance()>lowerLimit && vehicleList.get(sp).getDistance()<upperLimit ||
					vehicleList.get(sp).getDistance()+vehicleList.get(sp).getLength()>lowerLimit && vehicleList.get(sp).getDistance()+
					vehicleList.get(sp).getLength()<upperLimit))
				return false;
		}
		for(int sp=0; sp<pedestrianList.size(); sp++)
		{
			if(pedestrianList.get(sp)!=p && (pedestrianList.get(sp).getDistance()>lowerLimit && pedestrianList.get(sp).getDistance()<upperLimit ||
					pedestrianList.get(sp).getDistance()+pedestrianList.get(sp).getLength()>lowerLimit && pedestrianList.get(sp).getDistance()+
					pedestrianList.get(sp).getLength()<upperLimit))
				return false;
		}
		return true;

	}
        
        public boolean isGapforBackwardMovement(Pedestrian p)
	{
		double thresholdDistance = 0.5;
		double upperLimit = p.getDistance()+ p.getLength();
		double lowerLimit = p.getDistance()  - p.getSpeed()- thresholdDistance;
                for(int sp=0; sp<vehicleList.size(); sp++)
		{
			if( (vehicleList.get(sp).getDistance()>lowerLimit && vehicleList.get(sp).getDistance()<upperLimit ||
					vehicleList.get(sp).getDistance()+vehicleList.get(sp).getLength()>lowerLimit && vehicleList.get(sp).getDistance()+
					vehicleList.get(sp).getLength()<upperLimit))
				return false;
		}
		for(int sp=0; sp<pedestrianList.size(); sp++)
		{
			if(pedestrianList.get(sp)!=p && (pedestrianList.get(sp).getDistance()>lowerLimit && pedestrianList.get(sp).getDistance()<upperLimit ||
					pedestrianList.get(sp).getDistance()+pedestrianList.get(sp).getLength()>lowerLimit && pedestrianList.get(sp).getDistance()+
					pedestrianList.get(sp).getLength()<upperLimit))
				return false;
		}
		return true;

	}
        
	public boolean isGapforAddingVehicle(double vehicleLength)
	{
		double lowerLimit = 0;
                double upperLimit = 0.5+vehicleLength;
		for(int sp=0; sp<vehicleList.size(); sp++)
		{
			if(vehicleList.get(sp).getDistance()<upperLimit && 
					vehicleList.get(sp).getDistance()>lowerLimit)
				return false;
		}
                for(int sp=0; sp<pedestrianList.size(); sp++)
		{
			if(pedestrianList.get(sp).getDistance()<upperLimit && 
					pedestrianList.get(sp).getDistance()>lowerLimit)
				return false;
		}
		return true;	
		
	}
        public boolean isGapforAddingPedestrian(double distance,double pedestrianWidth)
	{
		double lowerLimit = distance-0.5;
                double upperLimit = distance+0.5+pedestrianWidth;
                for(int sp=0; sp<vehicleList.size(); sp++)
		{
			if(vehicleList.get(sp).getDistance()<upperLimit && 
					vehicleList.get(sp).getDistance()>lowerLimit)
				return false;
		}
		for(int sp=0; sp<pedestrianList.size(); sp++)
		{
			if(pedestrianList.get(sp).getDistance()<upperLimit && 
					pedestrianList.get(sp).getDistance()>lowerLimit)
				return false;
		}
		return true;	
		
	}
	public boolean isGapForStripChange(Vehicle v)
	{
		double thresholdDistance = 0.5;
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
                for(int sp=0; sp<pedestrianList.size(); sp++)
		{
                        double lowerLimit2 = pedestrianList.get(sp).getDistance() - thresholdDistance;
                        double upperLimit2 = pedestrianList.get(sp).getDistance() +
                               pedestrianList.get(sp).getLength() + thresholdDistance;
			if((lowerLimit1>=lowerLimit2 && lowerLimit1<=upperLimit2 ||
                            upperLimit1>=lowerLimit2 && upperLimit1<=upperLimit2)||
                           (lowerLimit2>=lowerLimit1 && lowerLimit2<=upperLimit1 ||
                            upperLimit2>=lowerLimit1 && upperLimit2<=upperLimit1))
				return false;
		}
		return true;	
		
	}
        public boolean isGapForStripChange(Pedestrian p)
	{
		double thresholdDistance = 0.5;
		double lowerLimit1 = p.getDistance() - thresholdDistance;
		double upperLimit1 = p.getDistance() + p.getLength() + thresholdDistance;
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
		for(int sp=0; sp<pedestrianList.size(); sp++)
		{
                        double lowerLimit2 = pedestrianList.get(sp).getDistance() - thresholdDistance;
                        double upperLimit2 = pedestrianList.get(sp).getDistance() +
                               pedestrianList.get(sp).getLength() + thresholdDistance;
			if((lowerLimit1>=lowerLimit2 && lowerLimit1<=upperLimit2 ||
                            upperLimit1>=lowerLimit2 && upperLimit1<=upperLimit2)||
                           (lowerLimit2>=lowerLimit1 && lowerLimit2<=upperLimit1 ||
                            upperLimit2>=lowerLimit1 && upperLimit2<=upperLimit1))
				return false;
		}
		return true;	
		
	}
}
