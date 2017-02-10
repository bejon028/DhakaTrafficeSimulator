import java.math.*;
import java.util.ArrayList;
import java.util.List;
public class Vehicle {
	
	
	
	private int type;
	private double length;
	private double width;
	private double stripWidth = 0.5;
	private int numStrip;

        private int demandIndex;
        private int pathIndex;
        private int pathLinkIndex;
	
	private Segment segment;
	private Strip strip;
	private double distance;
        private boolean passedSensor;
	
	private double speed;
	private double acceleration;
	
	private Vehicle leader;
	private Vehicle follower;
	private boolean leaderValid=false;
	private boolean followerValid=false;
		
	
	
	public Vehicle(double initSpeed,double initAccelaration,int itype, Segment initSegment, Strip initStrip, 
                int dmandIndex, int pthIndex, int pthLinkIndex)
	{
		type = itype;
		length = vehicleLength(type);
		width = vehicleWidth(type);
		numStrip = numStripOccupied(width);

                demandIndex = dmandIndex;
                pathIndex = pthIndex;
                pathLinkIndex = pthLinkIndex;
		
		segment = initSegment;
		strip = initStrip;
		distance = 0.1;
                passedSensor = false;
		
		speed = initSpeed;
		acceleration = initAccelaration;
		
		stripOccupy();
		//makeLeader();
		
	}
	
	//Setting Functions
	
	public void setSegment(Segment x)
	{
		segment = x;
	}
	public void setStrip(Strip x)
	{
		strip = x;
	}
        public void setDistance(double x)
        {
            distance = x;
        }
	public void setSpeed(double x)
	{
		speed=x;
	}
	public void setAcceleration(double x)
	{
		acceleration=x;
		speed+=acceleration;
	}
	
	
	//Get Functions
	
	public int getType()
	{
		return type;
	}
	public double getLength()
	{
		return length;
	}
	public double getWidth()
	{
		return width;
	}
        public int getDemandIndex()
        {
            return demandIndex;
        }
        public int getPathIndex()
        {
            return pathIndex;
        }
        public int getPathLinkIndex()
        {
            return pathLinkIndex;
        }
	public Segment getSegment()
	{
		return segment;
	}
	public Strip getStrip()
	{
		return strip;
	}
	public double getDistance()
	{
		return distance;
	}
	public double getSpeed()
	{
		return speed;
	}
	public double getAcceleration()
	{
		return acceleration;
	}
        public int getNumStrip()
        {
            return numStrip;
        }
	
	//Movement Functions
	
	public void moveright()
	{
		int x = strip.getStripIndex();
		if(x+numStrip<segment.getStripCount() && segment.getStrip(x+numStrip).isGapForStripChange(this)==true)
		{
			setStrip(segment.getStrip(x+1));
			segment.getStrip(x).delVehicle(this);
			segment.getStrip(x+numStrip).addVehicle(this);
		}
		findLeader();
		
	}
	public void moveleft()
	{
		int x = strip.getStripIndex();
		if(x!=0 && segment.getStrip(x-1).isGapForStripChange(this)==true)
		{
			setStrip(segment.getStrip(x-1));
			segment.getStrip(x-1).addVehicle(this);
			segment.getStrip(x+numStrip-1).delVehicle(this);
		}
		findLeader();
	}
	public void moveforward()
	{
		int flag = 1;
		int x = strip.getStripIndex();
		for(int i=x; i<x+numStrip; i++)
		{
			if(segment.getStrip(i).isGapforForwardMovement(this)== false)
				flag=0;				
		}
		if(flag==1)
                {
			distance+=speed;
                        if(passedSensor == false)
                        {
                            if(distance>segment.getSensor())
                                passedSensor = true;
                        }
                }
				
	}
        public boolean didPassSensor()
        {
            return passedSensor;
        }
	public boolean segmentEnd()
	{
		double thresholdDistance = speed;
		double segmentLength = Math.sqrt(Math.pow(segment.getStartingPntX()-segment.getEndPntX(), 2)
				+Math.pow(segment.getStartingPntY()-segment.getEndPntY(), 2));
		if(distance + length + thresholdDistance> segmentLength)
		{
			return true;
		}			
		else 
		{		
			return false;
		}		
	}
        public void segmentChange(Segment sgmnt, Strip strp)
        {
            segment = sgmnt;
            strip = strp;
            distance = 0.1;
            passedSensor = false;
            stripOccupy();
            findLeader();
        }
        public void linkChange(int pthLinkIndex, Segment sgmnt, Strip strp)
        {
            pathLinkIndex = pthLinkIndex;
            segmentChange(sgmnt,strp);
        }
	
	//Acceleration Functions	
	public void increaseSpeed(double acc)
	{
		speed+=acc;
	}
	public void decreaseSpeed(double dcc)
	{
		speed-=dcc;
	}
	public double vehicleWidth(int type)
	{
		if(type == 1)
			return 1.76;
		else if(type == 2 )
			return  1.78;
		else if(type == 3 )
			return  2.13;
		else if(type == 4 )
			return  2.02;
		else if(type == 5 )
			return  1.8;
		else if(type == 6 )
			return  1.3;
		else if(type == 7 )
			return  1.22;
		else if(type == 8 )
			return  0.75;
		else if(type == 9 )
			return  0.61;
		else if(type == 10 )
			return  2.46;
		else if(type == 11 )
			return  2.44;
		else
			return 1.76;
	}
	public double vehicleLength(int type)
	{
		if(type == 1)
			return 4.54;
		else if(type == 2 )
			return  4.29;
		else if(type == 3 )
			return  4.47;
		else if(type == 4 )
			return  5.78;
		else if(type == 5 )
			return  5.5;
		else if(type == 6 )
			return  2.63;
		else if(type == 7 )
			return  2.51;
		else if(type == 8 )
			return  2.13;
		else if(type == 9 )
			return  1.78;
		else if(type == 10 )
			return  8.46;
		else if(type == 11 )
			return  6.7;
		else
			return 4.54;
	}
	public int numStripOccupied(double width)
	{
		double widthCon = width/stripWidth;
		int numStrip = (int) Math.ceil(widthCon);
		return numStrip;
	}
	public void stripOccupy()
	{
		int x = strip.getStripIndex();
                int i;
		for(i=0; i<numStrip; i++)
		{ 
			segment.getStrip(x+i).addVehicle(this);
		}
	}
	public void findLeader()
	{
		int x = strip.getStripIndex();
		double min = 1000;
		for(int i=x; i<x+numStrip; i++)
		{
			Vehicle temp = strip.probableLeader(distance,length);
			if(temp!=null)
			{
				if(temp.getDistance()<min)
				{
					min = temp.getDistance();
					leader = temp;
                                        leaderValid = true;
				}
			}
			
		}
		
	}
	
}
