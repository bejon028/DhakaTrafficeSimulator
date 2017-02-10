import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import java.awt.event.*;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.geom.AffineTransform;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DrawingStep extends JPanel implements ActionListener, MouseListener, MouseMotionListener
{
    //Holds the network
    private List<Link> linkList = new ArrayList<Link>();
    //Holds the demands
    private List<Demand> demandList = new ArrayList<Demand>();
    //Holds the next vehicle generatio time
    private List<Integer> nextGenerationTime = new ArrayList<Integer>();
    //Holds the number of vehicles to be generated
    private List<Integer> numberOfVehiclesToGenerate = new ArrayList<Integer>();
    //Holds all vehicles
    private List<Vehicle> vehicleList = new ArrayList<Vehicle>();
    //Holds vehicles to be removed
    private List<Vehicle> vehicleRemovalList = new ArrayList<Vehicle>();
    //private List<Junction> junctionList = new ArrayList<Junction>();
    
    //last addition
    private List<MobileObject> objectList = new ArrayList<MobileObject>();
    private List<MobileObject> objectRemovalList = new ArrayList<MobileObject>();
    
    //Simulation time
    private int simulationStep = 0;
    //Simulation end time
    private int simulationEndTime = 0;
    //Screen Ratio
    private int stripPixelCount = 1; 
    private int mpRatio = 2;
    private int fpStripPixelCount = 1;
    //Simulation speed
    private int simulationSpeed = 1440;

    Timer timer;

    //Pan and Zoom
    double translateX = 0;
    double translateY = 0;
    double scale = 1;

    //Reference point to translate
    int referenceX;
    int referenceY;

    int numberOfSegments;
    
    public static boolean isStripBased;
    //for debugging purpose
    private static boolean isTagged=false;
    int acci = 0;
    boolean complete = false;
    boolean pedestrainmode = true;
    File outFile;
    BufferedWriter br;
    public DrawingStep(int strpPixelCount, int simSpeed, int simEndTime, boolean isStrip)
    {
        
        this.addMouseListener(this);
	this.addMouseMotionListener(this);
        isStripBased = isStrip;
        //Loading network and demand
        NetworkParse np = new NetworkParse();
    	linkList = np.getLinkList();
        numberOfSegments = np.getNumberOfSegments();
        VehicleLoad vl = new VehicleLoad();
        demandList = vl.getDemand();
        
        //for junction
        //junctionList = np.getJunctionList();
        
        int index;
        
        try {
            outFile = new File("log.txt");
            br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));
        } catch (Exception e) {
            System.out.println("Exception for file");
        }
        /* Vehicle generation event creation.
         * Demand signifies number of vehicle enters into the specific path per hour.
         * We calculate required number of vehicle to draw/generate per second. This information is later used to draw
         * vehicle in each simulation step or drawing turn.In a word, in each call of paintComponent a specific number
         * of vehicle is generated to meet the demand.
         */
        for(index=0;index<demandList.size();index++)
        {
            nextGenerationTime.add(1);
            double demand = (double)demandList.get(index).getDemand();          //returns number of vehicle
            double demandRatio = 3600/demand;

            if(demandRatio>1)
                numberOfVehiclesToGenerate.add(1);
            else
            {
                numberOfVehiclesToGenerate.add((int)Math.round(1/demandRatio));
            }

        }


        /*
        for(int i=0; i<junctionList.size(); i++){
            linkList.get(junctionList.get(i).inLinkIndexes.get(0)).stopLink();
        }
        */
        //Simulation speed, end time and screen ratio setting
        
        if(isStrip)
        {
            fpStripPixelCount = strpPixelCount;
            stripPixelCount = strpPixelCount;
            mpRatio = stripPixelCount*2;
        }
        else
        {
            fpStripPixelCount = strpPixelCount;
            stripPixelCount = strpPixelCount*5;
            mpRatio = stripPixelCount/2;
        }
        
        simulationSpeed = simSpeed;
        simulationEndTime = simEndTime;
    	timer = new Timer(simulationSpeed, this);
    	timer.setInitialDelay(800);
    	timer.start();
        
        
    }
    //This method draws the whole simulation and is called after each "simulationSpeed" delay
    @Override
    public void paintComponent(Graphics g)
    {

        Graphics2D ourGraphics = (Graphics2D) g;

        AffineTransform saveTransform = ourGraphics.getTransform();

        ourGraphics.setColor(Color.BLACK);
	ourGraphics.fillRect(0, 0, getWidth(), getHeight());

        AffineTransform at = new AffineTransform(saveTransform);

        at.translate(getWidth()/2, getHeight()/2);
	at.scale(scale, scale);
	at.translate(-getWidth()/2, -getHeight()/2);

        at.translate(translateX, translateY);

	ourGraphics.setTransform(at);

        //Time increases
    	simulationStep++;
        
        /*if(simulationStep >= simulationEndTime/2)
        {
            linkList.get(junctionList.get(0).inLinkIndexes.get(1)).stopLink();
            linkList.get(junctionList.get(0).inLinkIndexes.get(0)).startLink();
        }*/
        super.paintComponent(ourGraphics);

        this.setBackground(Color.BLACK);        
        drawRoadNetwork(ourGraphics);

        int index;

        //Removing vehicles
        for(index=0;index<vehicleRemovalList.size();index++)
        {
            Vehicle v = vehicleRemovalList.get(index);
            int index2;
            for(index2=v.getStrip().getStripIndex();index2<v.getStrip().getStripIndex()+v.getNumStrip();index2++)
            {
                v.getSegment().getStrip(index2).delVehicle(v);
            }
            vehicleList.remove(v);
        }
        if(pedestrainmode)
        {
            //Removing object
            for(index=0;index<objectRemovalList.size();index++)
            {
                MobileObject obj = objectRemovalList.get(index);
                objectList.remove(obj);
            }



            //adding random-objects
            Random numGenerator = new Random();
            int randomSegmentId,randomPos;
            double randomObjSpeed;
            for(int i=0; i<linkList.size(); i++)
            {
                if(Math.abs(numGenerator.nextInt())%10 == 0)
                {
                    randomSegmentId = Math.abs(numGenerator.nextInt())%linkList.get(i).getSegmentCount();
                    Segment randomSegment = linkList.get(i).getSegment(randomSegmentId);
                    randomPos = Math.abs(numGenerator.nextInt())%(int)(randomSegment.getSegmentLength());
                    randomObjSpeed = Math.abs(numGenerator.nextInt())%2+0.5;

                    MobileObject mobj = new MobileObject(randomSegment,randomSegment.getStrip(0),randomPos,randomObjSpeed);
                    objectList.add(mobj);
                }
            }
        }
        changeLane();
        //Adding vehicles
        Random randomPathGenerator = new Random();
        Random randomSpeedGenerator = new Random();
        Random randomTypeGenerator = new Random();
        VehicleType vt = new VehicleType();
        int randomPath, randomSpeed, randomType;
        
        //loop for each demand
        for(index=0; index<demandList.size(); index++)
        {
            //check whether it is turn for this demand to add vehicle
            if(nextGenerationTime.get(index)==simulationStep)
            {
                int numPath = demandList.get(index).getNumPath();

                int index2=0;
                int lastEmptyStrip = 1;        //keeps track of which strip was found empty last time.Initially assume the first.
                int index3=0;
                
                //loop while required number of vehicle generated for this turn.
                for(index2=0;index2<numberOfVehiclesToGenerate.get(index);index2++)
                {
                    randomPath = Math.abs(randomPathGenerator.nextInt());
                    randomSpeed = Math.abs(randomSpeedGenerator.nextInt());
                    randomType = Math.abs(randomSpeedGenerator.nextInt());
                    int pathSelected = randomPath%numPath;
                    int speedSelected = (randomSpeed%9)+1;
                    
                    int accSelected = speedSelected/3;
                    
                    int typeSelected = randomType%11;
                    int numStripRequired = vt.numStrip(typeSelected);       //gets required strip count for the selected vehicle
                    Path path = demandList.get(index).getPath(pathSelected);//gets path for this demand
                    int firstLinkIndex = path.getLinkIndex(0);              //gets the first link of the chosen path
                    Link firstLink = linkList.get(firstLinkIndex);
                    if(index2 == 0)lastEmptyStrip = firstLink.getSegment(0).getFpStripCount();
                    
                    
                    /*enters the loop if it finds the required number of strips(not necessarily empty) for this vehicle
                    *in the first segment of this link
                    */
                    for(index3=lastEmptyStrip;index3+numStripRequired-1<firstLink.getSegment(0).getStripCount();index3++) //last change /2
                    {
                        int index4,flag=1;
                        //loops to check whether required strips in segment are empty
                        for(index4=index3;index4<index3+numStripRequired;index4++)
                        {
                            //check whether there is space for adding this new vehicle 
                            if(firstLink.getSegment(0).getStrip(index4).isGapforAddingVehicle(vt.Length(typeSelected))==false)
                                flag=0;
                        }
                        if(flag==1)   //if enough space found empty
                        {
                            //my change for lane change-> firstLink
                            Vehicle v = new Vehicle(speedSelected,accSelected,typeSelected,firstLink,firstLink.getSegment(0),
                                firstLink.getSegment(0).getStrip(index3),index,pathSelected,0);     //acceleration is zero.
                            vehicleList.add(v);
                            
                            //debugging
                            if(isTagged==false && index==0){
                                v.tagged=true;
                                isTagged=true;
                            }
                            
                            lastEmptyStrip=index3+numStripRequired;
                            break;
                        }
                    }
                }
                int demand = demandList.get(index).getDemand();
                double demandRatio = 3600/demand;
                //if demandratio>1, then we generate vehicles once in some drawing turn .Else we draw in each turn. 
                if(demandRatio>1)
                {
                    int nextTime = nextGenerationTime.get(index)+(int)Math.round(demandRatio);
                    nextGenerationTime.set(index, nextTime);
                }
                else
                {
                    int nextTime = nextGenerationTime.get(index)+1;
                    nextGenerationTime.set(index, nextTime);

                }
            }
        }
        if(pedestrainmode)
        {
            //drawing mobile objects and moving forward
            for(int i=0; i<objectList.size(); i++)
            {
                MobileObject obj = objectList.get(i);
                drawMobileObject(ourGraphics,obj);
                if(obj.hasCrossedRoad() || obj.inAccident)
                {
                    objectRemovalList.add(obj);
                }
                else
                {
                    obj.moveForward();
                }
            }
        }
        //Drawing vehicles and moving them forward
        for(index=0;index<vehicleList.size();index++)
        {
            //Drawing vehicles
            drawVehicle(ourGraphics,vehicleList.get(index));
            //A vehicle moving forward within the segment
            if(vehicleList.get(index).segmentEnd() == false)
            {
                boolean previous = vehicleList.get(index).didPassSensor();
                
                
                    vehicleList.get(index).moveforward();
                boolean now = vehicleList.get(index).didPassSensor();
                if(previous == false && now == true)
                {
                    vehicleList.get(index).getSegment().updateSensorInfo(vehicleList.get(index).getSpeed());
                }
               
            }
            //A vehicle moving from a segment to a new segment
            else
            {
                vehicleList.get(index).isMovedForward=false;
                //A vehicle changing its link
                if(vehicleList.get(index).getSegment().lastSegment()==true)
                {
                    Vehicle v = vehicleList.get(index);
                    int demandIndex = v.getDemandIndex();
                    int pathIndex = v.getPathIndex();
                    int pathLinkIndex = v.getPathLinkIndex();
                    int lastLinkInPathIndex = demandList.get(demandIndex).getPath(pathIndex).pathLength()-1;

                    //Vehicle reaching destination
                    if(pathLinkIndex == lastLinkInPathIndex)
                    {
                        vehicleRemovalList.add(vehicleList.get(index));
                    }
                    //link stop
                    /*
                    else if(linkList.get(demandList.get(demandIndex).getPath(pathIndex).getLinkIndex(pathLinkIndex)).isLinkStopped())
                    {
                        v.reachedJunction = true;
                    }
                    */
                    //Vehicle to a new link
                    else
                    {
                        //if(v.reachedJunction)v.reachedJunction = false;
                        int newlinkIndex = demandList.get(demandIndex).getPath(pathIndex).getLinkIndex(pathLinkIndex+1);
                        int prevsegstripcount = linkList.get(pathLinkIndex).getSegment(0).getStripCount();
                        int newsegstripcount = linkList.get(newlinkIndex).getSegment(0).getStripCount();
                        int newsegfpstripcount = linkList.get(newlinkIndex).getSegment(0).getFpStripCount();
                        int newSegmentIndex = 0;
                        //int newStripIndex = vehicleList.get(index).getStrip().getStripIndex();  //chooses the same strip in new link 
                        //change for funnelling
                       
                        int newStripIndex = (int)Math.floor((v.getStrip().getStripIndex()*1.0)/prevsegstripcount*newsegstripcount);
                        if(newsegstripcount-newStripIndex < v.getNumStrip())newStripIndex = newsegstripcount-v.getNumStrip();
                        if(newStripIndex < newsegfpstripcount)newStripIndex = newsegfpstripcount;
                        //upto here
                        int index2;
                        int flag =1;
                        //looks for each strip of the new link's segment if there is enough space for adding vehicle 
                        for(index2=0;index2<v.getNumStrip();index2++)
                        {
                            if(linkList.get(newlinkIndex).getSegment(newSegmentIndex).getStrip(newStripIndex+index2).isGapforAddingVehicle(v.getLength())==false)
                                flag=0;
                        }
                       // System.out.println(flag);
                        if(flag == 1)
                        {
                            for(index2=v.getStrip().getStripIndex();index2<v.getStrip().getStripIndex()+v.getNumStrip();index2++)
                            {
                                v.getSegment().getStrip(index2).delVehicle(v);
                            }
                            //my change for lane change -> linkList.get(newlinkIndex)
                            v.linkChange(pathLinkIndex+1,linkList.get(newlinkIndex),linkList.get(newlinkIndex).getSegment(newSegmentIndex), linkList.get(newlinkIndex).getSegment(newSegmentIndex).getStrip(newStripIndex));
                        }
                    }
                }
                //vehicle changing its segment within same link
                else
                {
                    Vehicle v = vehicleList.get(index);
                    int linkIndex = v.getSegment().getLinkIndex();
                    int segIndex = v.getSegment().getSegmentIndex();
                    int stripIndex = v.getStrip().getStripIndex();
                    int index2;
                    int flag =1;
                    for(index2=0;index2<v.getNumStrip();index2++)
                    {
                        if(linkList.get(linkIndex).getSegment(segIndex+1).getStrip(stripIndex+index2).isGapforAddingVehicle(v.getLength())==false)
                            flag=0;
                    }
                   // System.out.println(flag);
                    if(flag == 1)
                    {
                        for(index2=v.getStrip().getStripIndex();index2<v.getStrip().getStripIndex()+v.getNumStrip();index2++)
                        {
                            v.getSegment().getStrip(index2).delVehicle(v);
                        }
                        v.segmentChange(linkList.get(linkIndex).getSegment(segIndex+1), linkList.get(linkIndex).getSegment(segIndex+1).getStrip(stripIndex));
                    }
                }
            }
        }
    }
    
    //Lane Changing Model
    public void changeLane()
    {
        int index;
        for(index=0;index<vehicleList.size();index++)
        {
            Vehicle cv=vehicleList.get(index);
            int currentlinkindex=demandList.get(cv.getDemandIndex()).getPath(cv.getPathIndex()).getLinkIndex(cv.getPathLinkIndex());
            Link currentLink=linkList.get(currentlinkindex);
            int lastLinkInPathIndex = demandList.get(cv.getDemandIndex()).getPath(cv.getPathIndex()).pathLength()-1;
            int nextLinkIndex;
            if(cv.hasGoneDistance() && cv.getPathLinkIndex()!=lastLinkInPathIndex)
            {
                nextLinkIndex=demandList.get(cv.getDemandIndex()).getPath(cv.getPathIndex()).getLinkIndex(cv.getPathLinkIndex()+1);
                //Link nextLinkOfPath=linkList.get(nextLinkIndex);

                String direction;
                int dir=currentLink.directionOfNextLink(nextLinkIndex);
                if(dir==0)direction="straight";
                else if(dir==1){
                    direction="left";
                    cv.moveleft();
                }
                else{
                    direction="right";
                    cv.moveright();
                }
                //debugging
//                if(cv.tagged==true){
//                    System.out.println("type= "+cv.getType()+"  nextlink= "+nextLinkIndex+" is on "+direction+" of Clink= "+currentlinkindex);//
//                }
            }
        }        
    }
    
    /*
     * Given each segment's upper starting and ending point's co-ordinate,
     * this method finds the lower starting and ending point's co-ordinate and draws road network joining them
     */
    public void drawRoadNetwork(Graphics g)
    {
    	for(int i=0; i<linkList.size(); i++)
    	{
//            if(linkList.get(i).getType() != 1)
//            {
    		for(int j=0; j<linkList.get(i).getSegmentCount(); j++)
    		{
	    		double  x1 = linkList.get(i).getSegment(j).getStartingPntX()*mpRatio;
	    		double  y1 = linkList.get(i).getSegment(j).getStartingPntY()*mpRatio;
	    		double  x2 = linkList.get(i).getSegment(j).getEndPntX()*mpRatio;
	    		double  y2 = linkList.get(i).getSegment(j).getEndPntY()*mpRatio;
	    		
	    		int a1 = (int) Math.round(x1);
	    		int b1 = (int) Math.round(y1);
	    		int a2 = (int) Math.round(x2);
	    		int b2 = (int) Math.round(y2);
	    		int fpStripCount = linkList.get(i).getSegment(j).getFpStripCount();
                        int usualStripCount = linkList.get(i).getSegment(j).getStripCount() - fpStripCount;
	    		int width = fpStripCount*fpStripPixelCount + usualStripCount*stripPixelCount;
                                //linkList.get(i).getSegment(j).getStripCount()*stripPixelCount;
	    		
	    		int a3 = (int) Math.round(returnX3(x1,y1,x2,y2,width));
	    		int b3 = (int) Math.round(returnY3(x1,y1,x2,y2,width));
	    		int a4 = (int) Math.round(returnX4(x1,y1,x2,y2,width));
	    		int b4 = (int) Math.round(returnY4(x1,y1,x2,y2,width));
                        

	    		g.setColor(Color.GREEN);
	    		g.drawLine(a1, b1, a2, b2);
	    		g.drawLine(a3, b3, a4, b4);
                        //draw divider
	    		//g.drawLine(am1, bm1, am2, bm2);
    		}
//            }
    	}
        
    }
    
    //Draws vehicle inside the network and adds color according to type
    public void drawVehicle(Graphics g,Vehicle v)
    {
        try {
            if(!complete)
            {
                br.write(v.toString());
                br.newLine();
            }
        } catch (IOException ex) {
            System.out.println("File Exception while writing"+ex);
        }
    	Segment seg = v.getSegment();
    	double segmentLength = seg.getSegmentLength();
        //Math.sqrt(Math.pow(seg.getStartingPntX()-seg.getEndPntX(), 2)+Math.pow(seg.getStartingPntY()-seg.getEndPntY(), 2));
    	
    	int length = (int) Math.ceil(v.getLength());
    	
        //Using internally section or ratio formula,it finds the coordinates along which vehicles are
    	double xp = (v.getDistance()*seg.getEndPntX()+(segmentLength-v.getDistance())*seg.getStartingPntX())/segmentLength*mpRatio;
    	double yp = (v.getDistance()*seg.getEndPntY()+(segmentLength-v.getDistance())*seg.getStartingPntY())/segmentLength*mpRatio;
    	double xq = ((v.getDistance()+length)*seg.getEndPntX()+(segmentLength-(v.getDistance()+length))*seg.getStartingPntX())/segmentLength*mpRatio;
    	double yq = ((v.getDistance()+length)*seg.getEndPntY()+(segmentLength-(v.getDistance()+length))*seg.getStartingPntY())/segmentLength*mpRatio;
    	
        
        int w = seg.getFpStripCount()*fpStripPixelCount + (v.getStrip().getStripIndex()-seg.getFpStripCount())*stripPixelCount;
    	//finds the coordinates of vehicles starting and ending upper points depending on which strip their upper(left) portion are.
    	int x1 = (int) Math.round(returnX3(xp,yp,xq,yq,w));
	int y1 = (int) Math.round(returnY3(xp,yp,xq,yq,w));
	int x2 = (int) Math.round(returnX4(xp,yp,xq,yq,w));
	int y2 = (int) Math.round(returnY4(xp,yp,xq,yq,w)); //(v.getStrip().getStripIndex())*stripPixelCount
		
	//int width = (int) Math.ceil(v.getWidth())*stripPixelCount;
        int width = v.getNumFpStrip()*fpStripPixelCount;
	
        //finds the coordinates of perpendicularly oppsite lower(right) points
	int x3 = (int) Math.round(returnX3(xp,yp,xq,yq,w+width));
	int y3 = (int) Math.round(returnY3(xp,yp,xq,yq,w+width));
	int x4 = (int) Math.round(returnX4(xp,yp,xq,yq,w+width));
	int y4 = (int) Math.round(returnY4(xp,yp,xq,yq,w+width));
		
		int type = v.getType();
		
                if(v.tagged==true)
                    g.setColor(Color.red);
                else if(type == 1)
			g.setColor(Color.blue);
		else if(type == 2)
			g.setColor(Color.cyan);
		else if(type == 3)
			g.setColor(Color.darkGray);
		else if(type == 4)
			g.setColor(Color.green);
		else if(type == 5)
			g.setColor(Color.magenta);
		else if(type == 6)
			g.setColor(Color.orange);
		else if(type == 7)
			g.setColor(Color.pink);
		else if(type == 8)
			g.setColor(Color.LIGHT_GRAY);//red
		else if(type == 9)
			g.setColor(Color.white);
		else if(type == 10)
			g.setColor(Color.yellow);

		g.drawLine(x1,y1,x2,y2);
		g.drawLine(x3,y3,x4,y4);
		g.drawLine(x1,y1,x3,y3);
		g.drawLine(x2,y2,x4,y4);
    	
    }
    
    public void drawMobileObject(Graphics g,MobileObject obj)
    {
        Segment seg = obj.getSegment();
    	double segmentLength = seg.getSegmentLength();
        double length = 1;
        //Using internally section or ratio formula,it finds the coordinates along which vehicles are
    	double xp = (obj.getInitPos()*seg.getEndPntX()+(segmentLength-obj.getInitPos())*seg.getStartingPntX())/segmentLength*mpRatio;
    	double yp = (obj.getInitPos()*seg.getEndPntY()+(segmentLength-obj.getInitPos())*seg.getStartingPntY())/segmentLength*mpRatio;
        double xq = ((obj.getInitPos()+length)*seg.getEndPntX()+(segmentLength-(obj.getInitPos()+length))*seg.getStartingPntX())/segmentLength*mpRatio;
    	double yq = ((obj.getInitPos()+length)*seg.getEndPntY()+(segmentLength-(obj.getInitPos()+length))*seg.getStartingPntY())/segmentLength*mpRatio;
        int x1 = (int) Math.round(returnX3(xp,yp,xq,yq,(obj.getDistance()/Strip.fpStripWidth)*fpStripPixelCount)); //obj.getDistance()*mpRatio
	int y1 = (int) Math.round(returnY3(xp,yp,xq,yq,(obj.getDistance()/Strip.fpStripWidth)*fpStripPixelCount));
        if(obj.inAccident)
        {
            //acci++;
            //System.out.println(acci);
            g.drawOval(x1, y1, 10, 10);
        }
        g.drawOval(x1, y1, 5, 5);
    }
    
    /*
     * The following four methods calculate X or Y value of the coordinate of the desired point 
     * which is perpendicularly given 'distance' apart from the given point.
     */
    public static double returnX3(double x1, double y1, double x2, double y2, double distance)
    {
    	double x3 = ((x1-x2)*(x1*(x1-x2)+y1*(y1-y2))-(y1-y2)*(-distance*Math.sqrt((y1-y2)*(y1-y2)+
    			(x1-x2)*(x1-x2))-x2*y1+x1*y2))/((y1-y2)*(y1-y2)+(x1-x2)*(x1-x2));
    	return x3;   	
    }
    public static double returnY3(double x1, double y1, double x2, double y2, double distance)
    {
    	double y3 = ((x1-x2)*(-distance*Math.sqrt((y1-y2)*(y1-y2)+(x1-x2)*(x1-x2))-x2*y1+x1*y2)+
    			(y1-y2)*(x1*(x1-x2)+y1*(y1-y2)))/((y1-y2)*(y1-y2)+(x1-x2)*(x1-x2));
    	return y3;    	
    }
    public static double returnX4(double x1, double y1, double x2, double y2, double distance)
    {
    	double x4 = ((x1-x2)*(x2*(x1-x2)+y2*(y1-y2))-(y1-y2)*(-distance*Math.sqrt((y1-y2)*(y1-y2)+
    			(x1-x2)*(x1-x2))-x2*y1+x1*y2))/((y1-y2)*(y1-y2)+(x1-x2)*(x1-x2));
    	return x4;   	
    }
    public static double returnY4(double x1, double y1, double x2, double y2, double distance)
    {
    	double y4 = ((x1-x2)*(-distance*Math.sqrt((y1-y2)*(y1-y2)+(x1-x2)*(x1-x2))-x2*y1+x1*y2)+
    			(y1-y2)*(x2*(x1-x2)+y2*(y1-y2)))/((y1-y2)*(y1-y2)+(x1-x2)*(x1-x2));
    	return y4;    	
    }
    
    //gets preferred size of the window
    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(500, 500);
    }
    
    //called on each timer event
    public void actionPerformed(ActionEvent e)
    {
        //simulation isn't finished yet
        if(simulationStep<simulationEndTime)
        {
            repaint();
        }
        
        //simulation has just finished
        else if(simulationStep == simulationEndTime)
        {
            complete = true;
            try {
                br.close();
            } catch (IOException ex) {
                System.out.println("Exception while closing file");
            }
            double sensorVehicleCount[] = new double[numberOfSegments];
            double sensorVehicleAvgSpeed[] = new double[numberOfSegments];
            double accidentCount[] = new double[numberOfSegments];
            int index, index2, ct = 0;
            
            //following loops calculate vehicle count and vehicle average speed for each segment
            for(index=0;index<linkList.size();index++)
            {
                for(index2=0;index2<linkList.get(index).getSegmentCount();index2++)
                {
                    sensorVehicleCount[ct]=linkList.get(index).getSegment(index2).getSensorVehicleCount();
                    sensorVehicleAvgSpeed[ct]=linkList.get(index).getSegment(index2).getSensorVehicleAvgSpeed();
                    accidentCount[ct] = linkList.get(index).getSegment(index2).getAccidentCount();
                    ct++;
                }
            }
            double y[] = {100,119,120};
            //plots graph
            SensorVehicleCountPlot p1 = new SensorVehicleCountPlot(sensorVehicleCount);
            SensorVehicleAvgSpeedPlot p2 = new SensorVehicleAvgSpeedPlot(sensorVehicleAvgSpeed);
            AccidentCountPlot p3 = new AccidentCountPlot(accidentCount);
            simulationStep++;
        }
    }
    
    //gets the coordinate of the place where mouse is pressed
    public void mousePressed(MouseEvent e)
    {
	    //Capture the starting point
	    referenceX = e.getX();
	    referenceY = e.getY();
    }

    //this method calculates the mouse-dragged distance in X and Y coordinate and sets them to make transform 
    public void mouseDragged(MouseEvent e)
    {

	    // The size of the pan translations
	    // are defined by the current mouse location subtracted
	    // from the reference location
	    int deltaX = e.getX() - referenceX;
	    int deltaY = e.getY() - referenceY;

	    // Make the reference point be the new mouse point.
	    referenceX = e.getX();
	    referenceY = e.getY();

	    this.translateX += deltaX;
	    this.translateY += deltaY;

	}

	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
}
