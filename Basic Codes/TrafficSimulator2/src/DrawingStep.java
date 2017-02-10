import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import java.awt.event.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import java.awt.geom.AffineTransform;


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

    //Holds all vehicles
    private List<Pedestrian> pedestrianList = new ArrayList<Pedestrian>();
    //Holds vehicles to be removed
    private List<Pedestrian> pedestrianRemovalList = new ArrayList<Pedestrian>();
    //Simulation time
    private int simulationStep = 0;
    //Simulation end time
    private int simulationEndTime = 0;
    //Screen Ratio
    private int stripPixelCount = 1;
    private int mpRatio = 2;
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

    public DrawingStep(int strpPixelCount, int simSpeed, int simEndTime)
    {
        this.addMouseListener(this);
	this.addMouseMotionListener(this);

        //Loading network and demand
        NetworkParse np = new NetworkParse();
    	linkList = np.getLinkList();
        numberOfSegments = np.getNumberOfSegments();
        VehicleLoad vl = new VehicleLoad();
        demandList = vl.getDemand();

        int index;

        //Vehicle generation event creation
        for(index=0;index<demandList.size();index++)
        {
            nextGenerationTime.add(1);
            double demand = (double)demandList.get(index).getDemand();
            double demandRatio = 3600/demand;

            if(demandRatio>1)
                numberOfVehiclesToGenerate.add(1);
            else
            {
                numberOfVehiclesToGenerate.add((int)Math.round(1/demandRatio));
            }

        }

        //Vehicle generation
     


        //Simulation speed, end time and screen ratio setting
        stripPixelCount = strpPixelCount;
        mpRatio = stripPixelCount*2;
        simulationSpeed = simSpeed;
        simulationEndTime = simEndTime;
    	timer = new Timer(simulationSpeed, this);
    	timer.setInitialDelay(800);
    	timer.start();
        
        
    }
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
        //Removing pedestrians
       /* for(index=0;index<pedestrianRemovalList.size();index++)
        {
            Pedestrian p = pedestrianRemovalList.get(index);
            int index2;
            for(index2=p.getStrip().getStripIndex();index2<p.getStrip().getStripIndex()+p.getNumStrip();index2++)
            {
                p.getSegment().getStrip(index2).delPedestrian(p);
            }
            pedestrianList.remove(p);
        }*/

        //Lane Changing Model
        Random randomGenerator = new Random();
        for(index=0;index<vehicleList.size();index++)
        {
            int randomInt = randomGenerator.nextInt();
            if(randomInt%3==0)
                vehicleList.get(index).moveright();
            else if(randomInt%3==1)
                vehicleList.get(index).moveleft();

        }
        for(index=0;index<pedestrianList.size();index++)
        {
            int randomInt = randomGenerator.nextInt();
            if(randomInt%3==0)
                pedestrianList.get(index).moveforward();
            else if(randomInt%3==1)
                pedestrianList.get(index).movebackward();

        }

        //Adding vehicles
        Random randomPathGenerator = new Random();
        Random randomSpeedGenerator = new Random();
        Random randomTypeGenerator = new Random();
        VehicleType vt = new VehicleType();
        int randomPath, randomSpeed, randomType;

        for(index=0; index<demandList.size(); index++)
        {
            if(nextGenerationTime.get(index)==simulationStep)
            {
                int numPath = demandList.get(index).getNumPath();

                int index2=0;
                int lastEmptyStrip = 0;
                int index3=0;
                for(index2=0;index2<numberOfVehiclesToGenerate.get(index);index2++)
                {
                    randomPath = Math.abs(randomPathGenerator.nextInt());
                    randomSpeed = Math.abs(randomSpeedGenerator.nextInt());
                    randomType = Math.abs(randomSpeedGenerator.nextInt());
                    int pathSelected = randomPath%numPath;
                    int speedSelected = (randomSpeed%9)+1;
                    int typeSelected = randomType%11;
                    int numStripRequired = vt.numStrip(typeSelected);
                    Path path = demandList.get(index).getPath(pathSelected);
                    int firstLinkIndex = path.getLinkIndex(0);
                    Link firstLink = linkList.get(firstLinkIndex);

                    for(index3=lastEmptyStrip;index3+numStripRequired-1<firstLink.getSegment(0).getStripCount();index3++)
                    {
                        int index4,flag=1;
                        for(index4=index3;index4<index3+numStripRequired;index4++)
                        {
                            if(firstLink.getSegment(0).getStrip(index4).isGapforAddingVehicle(vt.Length(typeSelected))==false)
                                flag=0;
                        }
                        if(flag==1)
                        {
                            Vehicle v = new Vehicle(speedSelected,0,typeSelected,firstLink.getSegment(0),
                                firstLink.getSegment(0).getStrip(index3),index,pathSelected,0);
                            vehicleList.add(v);
                            lastEmptyStrip=index3+numStripRequired;
                            break;
                        }
                    }
                }
                int demand = demandList.get(index).getDemand();
                double demandRatio = 3600/demand;
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
         //Adding pedestrians
        for(index=0;index<linkList.size();index++)
        {
            int index2,index3;
            double randomDistance;
            Pedestrian p;
            Link tempLink=linkList.get(index);
            for(index2=0;index2<tempLink.getSegmentCount();index2++)
            {
                Segment tempSegment=tempLink.getSegment(index2);
                double segmentLength = Math.sqrt(Math.pow(tempSegment.getStartingPntX()-tempSegment.getEndPntX(), 2)
				+Math.pow(tempSegment.getStartingPntY()-tempSegment.getEndPntY(), 2));
                for(index3=0;index3<tempSegment.getPedestriancount();index3++)
                {
                    randomDistance =  Math.abs(randomSpeedGenerator.nextInt())% segmentLength;
                    
                    if(tempSegment.getStrip(0).isGapforAddingPedestrian(randomDistance,0.5)==true)
                    {
                        p=new Pedestrian(tempSegment,tempSegment.getStrip(0),randomDistance);
                        pedestrianList.add(p);
                    }
                }
                
            }
        }
     
        for(index=0;index<pedestrianList.size();index++)
        {
            Pedestrian p=pedestrianList.get(index);
            //drawPedestrian(ourGraphics,p);
        }
        for(index=0;index<vehicleList.size();index++)
            //Drawing vehicles
            drawVehicle(ourGraphics,vehicleList.get(index));
           for(index=0;index<pedestrianList.size();index++)
        {
            
            Pedestrian p=pedestrianList.get(index);
            if(p.segmentEnd())pedestrianRemovalList.add(p);
            else p.moveright();
            
        }
        //Drawing vehicles and moving them forward
        for(index=0;index<vehicleList.size();index++)
        {
            
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
                    //Vehicle to a new link
                    else
                    {
                        int newlinkIndex = demandList.get(demandIndex).getPath(pathIndex).getLinkIndex(pathLinkIndex+1);
                        
                        int newSegmentIndex = 0;
                        int newStripIndex = vehicleList.get(index).getStrip().getStripIndex();
                        int index2;
                        int flag =1;
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
                            v.linkChange(pathLinkIndex+1,linkList.get(newlinkIndex).getSegment(newSegmentIndex), linkList.get(newlinkIndex).getSegment(newSegmentIndex).getStrip(newStripIndex));
                        }
                    }
                }
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
    public void drawRoadNetwork(Graphics g)
    {
    	for(int i=0; i<linkList.size(); i++)
    	{
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
	    		
	    		int width = linkList.get(i).getSegment(j).getStripCount()*stripPixelCount;
	    		
	    		int a3 = (int) Math.round(returnX3(x1,y1,x2,y2,width));
	    		int b3 = (int) Math.round(returnY3(x1,y1,x2,y2,width));
	    		int a4 = (int) Math.round(returnX4(x1,y1,x2,y2,width));
	    		int b4 = (int) Math.round(returnY4(x1,y1,x2,y2,width));

	    		g.setColor(Color.GREEN);
	    		g.drawLine(a1, b1, a2, b2);
	    		g.drawLine(a3, b3, a4, b4);
    		}
    	}
    }
    private void drawPedestrian(Graphics g, Pedestrian p) {
        Segment seg = p.getSegment();
    	double segmentLength = Math.sqrt(Math.pow(seg.getStartingPntX()-seg.getEndPntX(), 2)+
    			Math.pow(seg.getStartingPntY()-seg.getEndPntY(), 2));
    	
    	int length = (int) Math.ceil(p.getLength());
    	
    	double xp = (p.getDistance()*seg.getEndPntX()+(segmentLength-p.getDistance())*seg.getStartingPntX())/segmentLength*mpRatio;
    	double yp = (p.getDistance()*seg.getEndPntY()+(segmentLength-p.getDistance())*seg.getStartingPntY())/segmentLength*mpRatio;
    	double xq = ((p.getDistance()+length)*seg.getEndPntX()+(segmentLength-(p.getDistance()+length))*seg.getStartingPntX())/segmentLength*mpRatio;
    	double yq = ((p.getDistance()+length)*seg.getEndPntY()+(segmentLength-(p.getDistance()+length))*seg.getStartingPntY())/segmentLength*mpRatio;
    	
    	
    	int x1 = (int) Math.round(returnX3(xp,yp,xq,yq,(p.getStrip().getStripIndex())*stripPixelCount));
	int y1 = (int) Math.round(returnY3(xp,yp,xq,yq,(p.getStrip().getStripIndex())*stripPixelCount));
	int x2 = (int) Math.round(returnX4(xp,yp,xq,yq,(p.getStrip().getStripIndex())*stripPixelCount));
	int y2 = (int) Math.round(returnY4(xp,yp,xq,yq,(p.getStrip().getStripIndex())*stripPixelCount));
		
	int width = (int) Math.ceil(p.getWidth())*stripPixelCount;
		
	int x3 = (int) Math.round(returnX3(xp,yp,xq,yq,(p.getStrip().getStripIndex())*stripPixelCount+width));
	int y3 = (int) Math.round(returnY3(xp,yp,xq,yq,(p.getStrip().getStripIndex())*stripPixelCount+width));
	int x4 = (int) Math.round(returnX4(xp,yp,xq,yq,(p.getStrip().getStripIndex())*stripPixelCount+width));
	int y4 = (int) Math.round(returnY4(xp,yp,xq,yq,(p.getStrip().getStripIndex())*stripPixelCount+width));
		
        g.setColor(Color.MAGENTA);

		g.drawLine(x1,y1,x2,y2);
		g.drawLine(x3,y3,x4,y4);
		g.drawLine(x1,y1,x3,y3);
		g.drawLine(x2,y2,x4,y4);
    }
    public void drawVehicle(Graphics g,Vehicle v)
    {
    	Segment seg = v.getSegment();
    	double segmentLength = Math.sqrt(Math.pow(seg.getStartingPntX()-seg.getEndPntX(), 2)+
    			Math.pow(seg.getStartingPntY()-seg.getEndPntY(), 2));
    	
    	int length = (int) Math.ceil(v.getLength());
    	
    	double xp = (v.getDistance()*seg.getEndPntX()+(segmentLength-v.getDistance())*seg.getStartingPntX())/segmentLength*mpRatio;
    	double yp = (v.getDistance()*seg.getEndPntY()+(segmentLength-v.getDistance())*seg.getStartingPntY())/segmentLength*mpRatio;
    	double xq = ((v.getDistance()+length)*seg.getEndPntX()+(segmentLength-(v.getDistance()+length))*seg.getStartingPntX())/segmentLength*mpRatio;
    	double yq = ((v.getDistance()+length)*seg.getEndPntY()+(segmentLength-(v.getDistance()+length))*seg.getStartingPntY())/segmentLength*mpRatio;
    	
    	
    	int x1 = (int) Math.round(returnX3(xp,yp,xq,yq,(v.getStrip().getStripIndex())*stripPixelCount));
	int y1 = (int) Math.round(returnY3(xp,yp,xq,yq,(v.getStrip().getStripIndex())*stripPixelCount));
	int x2 = (int) Math.round(returnX4(xp,yp,xq,yq,(v.getStrip().getStripIndex())*stripPixelCount));
	int y2 = (int) Math.round(returnY4(xp,yp,xq,yq,(v.getStrip().getStripIndex())*stripPixelCount));
		
	int width = (int) Math.ceil(v.getWidth())*stripPixelCount;
		
	int x3 = (int) Math.round(returnX3(xp,yp,xq,yq,(v.getStrip().getStripIndex())*stripPixelCount+width));
	int y3 = (int) Math.round(returnY3(xp,yp,xq,yq,(v.getStrip().getStripIndex())*stripPixelCount+width));
	int x4 = (int) Math.round(returnX4(xp,yp,xq,yq,(v.getStrip().getStripIndex())*stripPixelCount+width));
	int y4 = (int) Math.round(returnY4(xp,yp,xq,yq,(v.getStrip().getStripIndex())*stripPixelCount+width));
		
		int type = v.getType();
		
		if(type == 1)
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
			g.setColor(Color.red);
		else if(type == 9)
			g.setColor(Color.white);
		else if(type == 10)
			g.setColor(Color.yellow);

		g.drawLine(x1,y1,x2,y2);
		g.drawLine(x3,y3,x4,y4);
		g.drawLine(x1,y1,x3,y3);
		g.drawLine(x2,y2,x4,y4);
    	
    }
    public double returnX3(double x1, double y1, double x2, double y2, double distance)
    {
    	double x3 = ((x1-x2)*(x1*(x1-x2)+y1*(y1-y2))-(y1-y2)*(-distance*Math.sqrt((y1-y2)*(y1-y2)+
    			(x1-x2)*(x1-x2))-x2*y1+x1*y2))/((y1-y2)*(y1-y2)+(x1-x2)*(x1-x2));
    	return x3;   	
    }
    public double returnY3(double x1, double y1, double x2, double y2, double distance)
    {
    	double y3 = ((x1-x2)*(-distance*Math.sqrt((y1-y2)*(y1-y2)+(x1-x2)*(x1-x2))-x2*y1+x1*y2)+
    			(y1-y2)*(x1*(x1-x2)+y1*(y1-y2)))/((y1-y2)*(y1-y2)+(x1-x2)*(x1-x2));
    	return y3;    	
    }
    public double returnX4(double x1, double y1, double x2, double y2, double distance)
    {
    	double x4 = ((x1-x2)*(x2*(x1-x2)+y2*(y1-y2))-(y1-y2)*(-distance*Math.sqrt((y1-y2)*(y1-y2)+
    			(x1-x2)*(x1-x2))-x2*y1+x1*y2))/((y1-y2)*(y1-y2)+(x1-x2)*(x1-x2));
    	return x4;   	
    }
    public double returnY4(double x1, double y1, double x2, double y2, double distance)
    {
    	double y4 = ((x1-x2)*(-distance*Math.sqrt((y1-y2)*(y1-y2)+(x1-x2)*(x1-x2))-x2*y1+x1*y2)+
    			(y1-y2)*(x2*(x1-x2)+y2*(y1-y2)))/((y1-y2)*(y1-y2)+(x1-x2)*(x1-x2));
    	return y4;    	
    }
    
    public Dimension getPreferredSize()
    {
        return new Dimension(500, 500);
    }
    
    public void actionPerformed(ActionEvent e)
    {
        if(simulationStep<simulationEndTime)
        {
            repaint();
        }
        else if(simulationStep == simulationEndTime)
        {
            double sensorVehicleCount[] = new double[numberOfSegments];
            double sensorVehicleAvgSpeed[] = new double[numberOfSegments];
            int index, index2, ct = 0;
            for(index=0;index<linkList.size();index++)
            {
                for(index2=0;index2<linkList.get(index).getSegmentCount();index2++)
                {
                    sensorVehicleCount[ct]=linkList.get(index).getSegment(index2).getSensorVehicleCount();
                    sensorVehicleAvgSpeed[ct]=linkList.get(index).getSegment(index2).getSensorVehicleAvgSpeed();
                    ct++;
                }
            }
            double y[] = {100,119,120};
            SensorVehicleCountPlot p1 = new SensorVehicleCountPlot(sensorVehicleCount);
            SensorVehicleAvgSpeedPlot p2 = new SensorVehicleAvgSpeedPlot(sensorVehicleAvgSpeed);
            simulationStep++;
        }
    }
    
    public void mousePressed(MouseEvent e)
    {
	    //Capture the starting point
	    referenceX = e.getX();
	    referenceY = e.getY();
    }

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
