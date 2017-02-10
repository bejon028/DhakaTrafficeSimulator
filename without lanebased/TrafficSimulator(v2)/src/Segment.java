import java.util.ArrayList;
import java.util.List;

/*
 * Each link in a path is divided into one or more segment.This class describes formation and methods of segments
 */

public class Segment {

	private int linkIndex;

        private int segmentIndex;
        
        //The following 6 items are not used 
	private int segmentId;
	private double speedLimit;
	private double freeSpeed;
	private double gradient;
        private double bulge;
	private int speedDensityIndex;

	private double StartingPntX;
	private double StartingPntY;
	private double EndPntX;
	private double EndPntY;

        private double sensor;         //a point within a segment that detects movement of a vehicle
        private int sensorVehicleCount;
        private double sensorVehicleAvgSpeed;
	

        private boolean lastSegment;
	
	private int stripCount;	
	private List<Strip> stripList= new ArrayList<Strip>();
	 
	//constructor of segment class sets required parameters and initializes its constituent strips
	public Segment(int lnkIndex, int segIndex, int segID, double strtPntX, double strtPntY,
                double ndPntX, double ndPntY, double snsr, int scount, boolean lstSegment)
	{
		linkIndex = lnkIndex;
                segmentIndex = segIndex;
		segmentId = segID;
		StartingPntX = strtPntX;
		StartingPntY = strtPntY;
		EndPntX = ndPntX;
		EndPntY = ndPntY;
                sensor = snsr;
		stripCount = scount;
                lastSegment = lstSegment;
		stripIntialization();
	}
        
        
        //Initializes strips in this segment
	public void stripIntialization()
	{
		for(int i=0; i<stripCount; i++)
		{
			stripList.add(new Strip(segmentIndex, i));
		}
	}
        
        //gets strip from striplist according to stripindex
	public Strip getStrip(int x)
	{
		return stripList.get(x);
	}
        
        //returns number of strips in this segment
	public int getStripCount()
	{
		return stripCount;
	}
        
        //gets segment's starting 'X' coordinate
	public double getStartingPntX()
	{
		return StartingPntX;
	}
        
        //gets segment's starting 'Y' coordinate
	public double getStartingPntY()
	{
		return StartingPntY;
	}
        
        //gets segment's ending 'X' coordinate
	public double getEndPntX()
	{
		return EndPntX;
	}
        
        //gets segment's ending 'Y' coordinate
	public double getEndPntY()
	{
		return EndPntY;
	}
        
        //gets segment's sensor
        public double getSensor()
        {
            return sensor;
        }
        
        //returns link index
        public int getLinkIndex()
        {
            return linkIndex;
        }
        
        //returns segment index
        public int getSegmentIndex()
        {
            return segmentIndex;
        }
        
        //checks whether this is the last segment of link 
        public boolean lastSegment()
        {
            return lastSegment;
        }
        
        //counts number of vehicle passed over the sensor and updates average speed
	public void updateSensorInfo(double newSpeed)
        {
            sensorVehicleCount++;
            sensorVehicleAvgSpeed = sensorVehicleAvgSpeed/sensorVehicleCount*(sensorVehicleCount-1)+
                    newSpeed/sensorVehicleCount;
        }
        
        //returns number of vehicle passed over the sensor
        public int getSensorVehicleCount()
        {
            return sensorVehicleCount;
        }
        
        //returns vehicles average speed
        public double getSensorVehicleAvgSpeed()
        {
            return sensorVehicleAvgSpeed;
        }
}
