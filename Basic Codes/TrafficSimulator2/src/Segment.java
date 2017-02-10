import java.util.ArrayList;
import java.util.List;


public class Segment {

	private int linkIndex;

        private int segmentIndex;

	private int segmentId;
	private double speedLimit;
	private double freeSpeed;
	private double gradient;
        private double bulge;
	private int speedDensityIndex;
        private int pedestriancount;

	private double StartingPntX;
	private double StartingPntY;
	private double EndPntX;
	private double EndPntY;

        private double sensor;
        private int sensorVehicleCount;
        private double sensorVehicleAvgSpeed;
	

        private boolean lastSegment;
	
	private int stripCount;	
	private List<Strip> stripList= new ArrayList<Strip>();
	 
	
	public Segment(int lnkIndex, int segIndex, int segID, double strtPntX, double strtPntY,
                double ndPntX, double ndPntY, double snsr, int scount, boolean lstSegment,int pedestriancnt)
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
                pedestriancount=pedestriancnt;
		stripIntialization();
	}
	public void stripIntialization()
	{
		for(int i=0; i<stripCount; i++)
		{
			stripList.add(new Strip(segmentIndex, i));
		}
	}
        public int getPedestriancount()
	{
		return pedestriancount;
	}
	public Strip getStrip(int x)
	{
		return stripList.get(x);
	}
	public int getStripCount()
	{
		return stripCount;
	}	
	public double getStartingPntX()
	{
		return StartingPntX;
	}
	public double getStartingPntY()
	{
		return StartingPntY;
	}
	public double getEndPntX()
	{
		return EndPntX;
	}
	public double getEndPntY()
	{
		return EndPntY;
	}
        public double getSensor()
        {
            return sensor;
        }
        public int getLinkIndex()
        {
            return linkIndex;
        }
        public int getSegmentIndex()
        {
            return segmentIndex;
        }
        public boolean lastSegment()
        {
            return lastSegment;
        }
	public void updateSensorInfo(double newSpeed)
        {
            sensorVehicleCount++;
            sensorVehicleAvgSpeed = sensorVehicleAvgSpeed/sensorVehicleCount*(sensorVehicleCount-1)+
                    newSpeed/sensorVehicleCount;
        }
        public int getSensorVehicleCount()
        {
            return sensorVehicleCount;
        }
        public double getSensorVehicleAvgSpeed()
        {
            return sensorVehicleAvgSpeed;
        }
}
