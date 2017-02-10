import java.util.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/*
 * NetworkParse class builds the topology of the network from a input text file
 */

public class NetworkParse
{
    DataOutputStream dos;
    String fileName = "Network.txt";
    String DataLine = "";
    String header = "";
    
    //holds the nodes in a network
    private List<Node> nodeList= new ArrayList<Node>();
    private List<Junction> junctionList = new ArrayList<Junction>();
    //holds the links in a network
    private List<Link> linkList= new ArrayList<Link>();
    private int numberOfSegments = 0;
    private double sensorPosition=0.5;  //half way through a segment
	
        //Parse the network
	public NetworkParse()
	{
		readNetwork();
	}
    
    //Scans the input text file and get's the network topology    
    public void readNetwork()
    {
        try
        {
            File inFile = new File(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inFile)));
            
            int index,index2;
            
            DataLine = br.readLine();                 //reads number of nodes
            int numNodes = Integer.parseInt(DataLine);
            
            //System.out.println(numNodes);
            
            //Each line represents a node's information.Node id, node type and node type respectively
            for(index =0; index<numNodes; index++)
            {
            	DataLine = br.readLine();
            	StringTokenizer stToken1 = new StringTokenizer(DataLine," ");
            	int nodeId = Integer.parseInt(stToken1.nextToken());
            	int nodeType = Integer.parseInt(stToken1.nextToken());
            	String nodeName = stToken1.nextToken();
                nodeList.add(new Node(nodeId,nodeType,nodeName));
//                if(nodeType != 0)
//                {
//                    int in = Integer.parseInt(stToken1.nextToken());
//                    
//                    junctionList.get(nodeType).incomingLinkInit(in1, in2);
//                }
                
            	 
                
            }
            
            DataLine = br.readLine();
            int numJunctions = Integer.parseInt(DataLine);
            for(int i=0; i<numJunctions; i++)
            {
                DataLine = br.readLine();
                StringTokenizer stToken = new StringTokenizer(DataLine, " ");
                int jncId = Integer.parseInt(stToken.nextToken());
                int jncType = Integer.parseInt(stToken.nextToken());
                int numNds = Integer.parseInt(stToken.nextToken());
                List<Integer> ndList = new ArrayList<Integer>();
                for(int j=0; j<numNds; j++)
                {
                    ndList.add(Integer.parseInt(stToken.nextToken()));
                }
                List<Integer> inLinkIndexList =new ArrayList<Integer>();
                for(int k=0; k<2; k++)
                {
                    inLinkIndexList.add(Integer.parseInt(stToken.nextToken()));
                }
                
                junctionList.add(new Junction(jncId, jncType, ndList, inLinkIndexList));
            }
            
            DataLine = br.readLine();              //reads number of links
            int numLinks = Integer.parseInt(DataLine);
            
            //loop corresponds to the information of a link
            for(index =0; index<numLinks; index++)
            {
            	DataLine = br.readLine();           //reads information of a link
            	StringTokenizer stToken1 = new StringTokenizer(DataLine," ");
            	int linkId = Integer.parseInt(stToken1.nextToken());
            	int linkType = Integer.parseInt(stToken1.nextToken());
            	int UpNodeId = Integer.parseInt(stToken1.nextToken());
            	int DnNodeId = Integer.parseInt(stToken1.nextToken());
            	int segmentCount = Integer.parseInt(stToken1.nextToken());
                numberOfSegments += segmentCount;
            	linkList.add(new Link(index, linkId, linkType, UpNodeId, DnNodeId, segmentCount));
            	
            	//my change on 22/3/2015
                Map<Integer,Integer> nextLinkMap=new HashMap<Integer,Integer>();
                int numberOfConnectedLink=Integer.parseInt(stToken1.nextToken());
                for(int linkno=1;linkno<=numberOfConnectedLink;linkno++){
                    int key=Integer.parseInt(stToken1.nextToken());
                    nextLinkMap.put(key, 0);
                }
                linkList.get(index).nextLinkInitialization(nextLinkMap); 
                double linkLength=0;
                
                List<Segment> segmentList= new ArrayList<Segment>();
            	
                //loop corresponds to each segment of a link
    		for(index2 =0; index2<segmentCount; index2++)
    		{
    			DataLine = br.readLine();        //reads information of a segment
    			StringTokenizer stToken2 = new StringTokenizer(DataLine," ");
    			int segmentId = Integer.parseInt(stToken2.nextToken()); 
    			double startPntX = Double.parseDouble(stToken2.nextToken());
    			double startPntY = Double.parseDouble(stToken2.nextToken());
    			double endPntX = Double.parseDouble(stToken2.nextToken());
    			double endPntY = Double.parseDouble(stToken2.nextToken());
                        //double sensor = Double.parseDouble(stToken2.nextToken());
                        double segmentLength=Math.sqrt(Math.pow(startPntX-endPntX, 2)+Math.pow(startPntY-endPntY, 2));
                        linkLength += segmentLength;
                        double sensor=segmentLength*sensorPosition;
    			int stripCount = Integer.parseInt(stToken2.nextToken());
                        //new
                        //System.out.println(DrawingStep.isStripBased);
                        if(DrawingStep.isStripBased)stripCount = stripCount*5;
                        //upto
                        boolean lastSegment = false;
                        
                        //checks whether this is the last segment of the link
                        if(index2 == segmentCount-1)
                            lastSegment = true;
                        
                        
                        segmentList.add(new Segment(index, index2, segmentId,
                            startPntX, startPntY, endPntX, endPntY, sensor, stripCount, lastSegment));
    				
    		}
                //System.out.println("linklength: "+linkLength);
                linkList.get(index).setLinkLength(linkLength);
    		linkList.get(index).SegementInitialization(segmentList);       //sets segments to the correspoding link
            }
            determineDirection();
            br.close();
        }

        catch (FileNotFoundException ex)
        {
            System.out.println("FileNotFoundException HD");
        }
        catch (IOException ex)
        {
        	System.out.println("exception");
        }
    }
    
    //returns the list of node in this network
    public List<Node> getNodeList()
    {
    	return nodeList;
    }
    
    public List<Junction> getJunctionList()
    {
        return junctionList;
    }
    
    //returns the list of links of this network
    public List<Link> getLinkList()
    {
    	return linkList;
    }
    
    //returns the total number of segments in this network
    public int getNumberOfSegments()
    {
        return numberOfSegments;
    }
    
    //checks whether the file with name 'filename' exists
    public boolean isFileExists(String fileName)
    {
        File file = new File(fileName);
        return file.exists();
    }
    
    //deletes the file named 'filename'
    public boolean deleteFile(String fileName)
    {
        File file = new File(fileName);
        return file.delete();
    }
    
    //for determining next link direction dynamivcally
   public void determineDirection(){
        for(Link link:linkList){
            Segment fs=link.getSegment(link.getSegmentCount()-1);
            double x1 = fs.getStartingPntX();
            double y1 = fs.getStartingPntY();
            double x2 = fs.getEndPntX();
            double y2 = fs.getEndPntY();
            //int width=fs.getStripCount();
            //double x=DrawingStep.returnX4(x1,y1,x2,y2,width);
            //double y=DrawingStep.returnY4(x1,y1,x2,y2,width);
            //double cnst1=(x-x1)*(y1-y2)-(y-y1)*(x1-x2);
            for(int i:link.nextLinkMap.keySet()){
                Segment nfs=linkList.get(i).getSegment(0);
                //x = nfs.getStartingPntX();
                //y = nfs.getStartingPntY();
                double x3 = nfs.getEndPntX();
                double y3 = nfs.getEndPntY();
                //double cnst2=(x-x1)*(y1-y2)-(y-y1)*(x1-x2);
                double crossprod = (x2-x1)*(y3-y1) - (y2-y1)*(x3-x1);
                //if(cnst1*cnst2 < 0)link.nextLinkMap.put(i, 1);
                //else if(cnst1*cnst2 > 0)link.nextLinkMap.put(i, 2);
                if(crossprod > 0)link.nextLinkMap.put(i, 2); //left
                else if(crossprod < 0)link.nextLinkMap.put(i, 1); //right
                else link.nextLinkMap.put(i, 0); //staight
            }
        }
    }
   

}
