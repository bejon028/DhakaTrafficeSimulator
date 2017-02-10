import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        private boolean isFootPathStrip;
        private int probOutOf = 10;
        //my change
        public static double stripWidth = DrawingStep.isStripBased ? 0.5 : 2.5;
	public static double fpStripWidth = 0.5;
	private List<Vehicle> vehicleList= new ArrayList<Vehicle>();
	private List<MobileObject> objectList = new ArrayList<MobileObject>();
        //Constructor sets segment index and strip index
	public Strip(int segIndex, int strIndex, boolean isfoot)
	{
		segmentIndex = segIndex;
		stripIndex = strIndex;
                isFootPathStrip = isfoot;
	}
        
        public boolean isFp()
        {
            return isFootPathStrip;
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
        
        public void addObject(MobileObject obj)
        {
            objectList.add(obj);
        }
        
        public void delObject(MobileObject obj)
        {
            objectList.remove(obj);
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
                Random rand = new Random();
                boolean accident = false;
                
		for(int sp=0; sp<vehicleList.size(); sp++)
		{
			if(vehicleList.get(sp)!=v && (vehicleList.get(sp).getDistance()>lowerLimit && vehicleList.get(sp).getDistance()<upperLimit ||
					vehicleList.get(sp).getDistance()+vehicleList.get(sp).getLength()>lowerLimit && vehicleList.get(sp).getDistance()+
					vehicleList.get(sp).getLength()<upperLimit))
				return false;
		}
                if(rand.nextInt()%probOutOf == 0)accident = true;
                for(int i=0; i < objectList.size(); i++)
                {
                    double objpos =objectList.get(i).getInitPos();
                    if(lowerLimit < objpos && objpos < upperLimit)
                    {
                        if(!accident)
                        {
                            return false;
                        }
                        objectList.get(i).getSegment().updateAccidentcount();
                        objectList.get(i).inAccident = true;
                        delObject(objectList.get(i));
                    }
                }
		return true;

	}
        
        public boolean hasGapForObject(MobileObject obj)
        {
            double lowerLimit,upperLimit,thresholdDistance = 0.08;
            Vehicle v;
            
            Random rand = new Random();
            boolean accident = (rand.nextInt()%probOutOf == 0) ? true : false ;
            for(int i=0; i<vehicleList.size(); i++)
            {
                v = vehicleList.get(i);
		upperLimit = v.getDistance() + v.getLength()+ thresholdDistance;
		lowerLimit = v.getDistance();
                if(lowerLimit < obj.getInitPos() && obj.getInitPos() < upperLimit)
                {
                    if(!accident)
                    {
                        return false;
                    }
                    obj.getSegment().updateAccidentcount();
                    obj.inAccident = true;
                    delObject(obj);
                }
            }
            return true;
        }
        
        
        
        //checks whether there is adequate space for adding a new vehicle 
	public boolean isGapforAddingVehicle(double vehicleLength)
	{
		double lowerLimit = 0.08;
                double upperLimit = 0.6+vehicleLength;
                boolean accident = false;
                Random rand = new Random();
		for(int sp=0; sp<vehicleList.size(); sp++)
		{
			if(vehicleList.get(sp).getDistance()<upperLimit && 
					vehicleList.get(sp).getDistance()>lowerLimit)
				return false;
		}
                if(rand.nextInt()%probOutOf == 0)accident = true;
                for(int i=0; i<objectList.size(); i++)
                {
                    double objpos = objectList.get(i).getInitPos();
                    if(lowerLimit < objpos && objpos < upperLimit)
                    {
                        if(!accident)
                        {
                            return false;
                        }
                        objectList.get(i).getSegment().updateAccidentcount();
                        objectList.get(i).inAccident = true;
                        delObject(objectList.get(i));
                    }
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
                
                boolean accident = false;
                Random rand = new Random();
                if(rand.nextInt()%probOutOf == 0)accident = true;
                for(int i=0; i<objectList.size(); i++)
                {
                    double objpos = objectList.get(i).getInitPos();
                    if(lowerLimit1 < objpos && objpos < upperLimit1)
                    {
                        if(!accident)
                        {
                            return false;
                        }
                        objectList.get(i).getSegment().updateAccidentcount();
                        objectList.get(i).inAccident = true;
                        delObject(objectList.get(i));
                    }
                }
                
		return true;	
		
	}
}
