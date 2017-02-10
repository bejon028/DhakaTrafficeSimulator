
/**
 * describes the property and functions of a vehicle
 * @author USER
 */

public class Vehicle {
	
	
	
	private int type;
	private double length;
	private double width;
	private double stripWidth = 0.5;  //last changed 0.5
	private int numStrip;

        private int demandIndex;
        private int pathIndex;
        private int pathLinkIndex;
	
        private Link link;
	private Segment segment;
	private Strip strip;
	private double distance;  //represents distance within a segment
        private boolean passedSensor;
	
        //my change for lane change
        private double distanceInLink;
        private boolean hasPassedDistance;
        private double distanceForMandatory=0.33;  //half way through link
        private double linkSensor;
        
	private double speed,maxSpeed;
	private double acceleration;
	
	private Vehicle leader;
	private Vehicle follower;
	private boolean leaderValid=false;
	private boolean followerValid=false;
        //tagging for debugging purpose
        public boolean tagged=false; 
        
        //whether vehicle has to stop or not
        public boolean reachedJunction;
        public boolean isMovedForward;
	private boolean isPositiveDirectional;
	//vehicle constructor
	public Vehicle(double initSpeed,double initAccelaration,int itype,Link initLink, Segment initSegment, Strip initStrip, 
                int dmandIndex, int pthIndex, int pthLinkIndex, boolean dir)
	{
		type = itype;
		length = vehicleLength(type);
		width = vehicleWidth(type);
		numStrip = numStripOccupied(width);

                demandIndex = dmandIndex;
                pathIndex = pthIndex;
                pathLinkIndex = pthLinkIndex;
		
                link=initLink;
		segment = initSegment;
		strip = initStrip;
		distance = 0.1;
                linkSensor=link.getLinkLength()*distanceForMandatory;
                passedSensor = false;
                
                //my change
                distanceInLink=0.1;
                hasPassedDistance=false;
                //change for junction modelling
                reachedJunction = false;
		
		speed = maxSpeed = initSpeed;
		acceleration = initAccelaration;
		isPositiveDirectional = dir;
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
        
        //returns length
	public double getLength()
	{
		return length;
	}
        
        //returns width
	public double getWidth()
	{
		return width;
	}
        
        //every vehicle satisfies a demand,this method returns demand index
        public int getDemandIndex()
        {
            return demandIndex;
        }
        
        //reurns the path index through which it is running
        public int getPathIndex()
        {
            return pathIndex;
        }
        
        //returns the link index of a path
        public int getPathLinkIndex()
        {
            return pathLinkIndex;
        }
        
        //returns segment
	public Segment getSegment()
	{
		return segment;
	}
        
        //returns strip
	public Strip getStrip()
	{
		return strip;
	}
        
        //returns distance moved
	public double getDistance()
	{
		return distance;
	}
        
        //returns speed of this vehicle
	public double getSpeed()
	{
		return speed;
	}
        
        //returns acceleration of this vehicle
	public double getAcceleration()
	{
		return acceleration;
	}
        
        //returns strip number
        public int getNumStrip()
        {
            return numStrip;
        }
	
        public boolean getDir()
        {
            return isPositiveDirectional;
        }
        
	//Movement Functions
	//moves the vehicle right if finds enough gap and finds new leader
	public void moveright()
	{
		int x = strip.getStripIndex();
		if(((isPositiveDirectional && x+numStrip<segment.getStripCount()/2)||(!isPositiveDirectional && x+numStrip<segment.getStripCount())) 
                        && segment.getStrip(x+numStrip).isGapForStripChange(this)==true
                        && !this.reachedJunction
                        && isMovedForward) //last changed /2
		{
                    
                        setStrip(segment.getStrip(x+1));
                        segment.getStrip(x).delVehicle(this);
                        segment.getStrip(x+numStrip).addVehicle(this);                   
		}
		findLeader();
		
	}
        
        //moves the vehicle left if finds enough gap and finds new leader
	public void moveleft()
	{
		int x = strip.getStripIndex();
		if(((isPositiveDirectional && x!=0)||(!isPositiveDirectional && x!=segment.getStripCount()/2)) && segment.getStrip(x-1).isGapForStripChange(this)==true
                        && !this.reachedJunction
                        && isMovedForward)
		{
                        setStrip(segment.getStrip(x-1));
                        segment.getStrip(x-1).addVehicle(this);
                        segment.getStrip(x+numStrip-1).delVehicle(this);   
		}
		findLeader();
	}
        
        
        public boolean controlSpeed(){
                int x = strip.getStripIndex();
                if(speed < maxSpeed)speed += acceleration;
		for(int i=x; i<x+numStrip; i++)
		{
			if(segment.getStrip(i).isGapforForwardMovement(this)== false)
                        {
                            //flag=0;
                            if(acceleration==0 || (speed -= acceleration)<=0)return false;
                            i--;
                        }
		}
                return true;
        }
        /*it first checks whether there is gap for each strip occupied by this vehicle,if so it then moves
	*forward and sets the passedSensor variable true when it passes by the distance.
        */
        public boolean moveforward()
	{
                /*
		int flag = 1;                
		int x = strip.getStripIndex();
		for(int i=x; i<x+numStrip; i++)
		{
			if(segment.getStrip(i).isGapforForwardMovement(this)== false)
                        {
                            //flag=0;
                            if((speed -= acceleration)<=0){
                                flag = 0;
                                break;
                            };
                            i--;
                        }
		}
                */
		if(controlSpeed())
                {
			distance+= speed;
                        if(passedSensor == false)
                        {
                            if(distance>segment.getSensor())
                                passedSensor = true;
                        }
                        
                        //my change for lane change
                        distanceInLink+= speed;
                        if(hasPassedDistance == false)
                        {
                            if(distanceInLink > linkSensor)
                                hasPassedDistance = true;
                        }
                        return isMovedForward=true;
                }
                return isMovedForward=false;
				
	}
        
        //checks whether vehicle has passed the sensor
        public boolean didPassSensor()
        {
            return passedSensor;
        }
        
        //my change for lane change
        public boolean hasGoneDistance()
        {
            //System.out.println("Distanceinlink: "+distanceInLink+" linksensor: "+linkSensor+" passed?: "+hasPassedDistance);
            return hasPassedDistance;
        }
        
        
        //checks whether this vehicle has come to the end of current segment
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
        
        /*does the required setup when this vehicle changed a segment, gets a new segment and strip
         *finds a new leader on that strip, set the initial distance movement.
         */
        public void segmentChange(Segment sgmnt, Strip strp)
        {
            segment = sgmnt;
            strip = strp;
            distance = 0.1;
            passedSensor = false;
            stripOccupy();
            findLeader();
        }
        
        //does the required setup when this vehicle changes a link
        public void linkChange(int pthLinkIndex,Link lnk, Segment sgmnt, Strip strp)
        {
            pathLinkIndex = pthLinkIndex;
            //my change for lane change
            link=lnk;
            distanceInLink=0.1;  
            linkSensor=link.getLinkLength()*distanceForMandatory;
            hasPassedDistance=false;
            
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
        
        //returns vehicle width
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
        
        //returns vehicle length
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
        
        //returns the number of strip
	public int numStripOccupied(double width)
	{
		double widthCon = width/stripWidth;
		int numStrip = (int) Math.ceil(widthCon);
		return numStrip;
	}
        
        //adds this vehicle to the occupied strips
	public void stripOccupy()
	{
		int x = strip.getStripIndex();
                int i;
		for(i=0; i<numStrip; i++)
		{ 
			segment.getStrip(x+i).addVehicle(this);
		}
	}
        
        //finds vehicle that is immediately ahead of this vehicle but in shares same strip
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
