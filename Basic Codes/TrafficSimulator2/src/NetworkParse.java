import java.util.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.*;

public class NetworkParse
{
    DataOutputStream dos;
    String fileName = "Network2.txt";
    String DataLine = "";
    String header = "";
    
    private List<Node> nodeList= new ArrayList<Node>();
    private List<Link> linkList= new ArrayList<Link>();
    private int numberOfSegments = 0;
	
	public NetworkParse()
	{
		readNetwork();
	}
          
    public void readNetwork()
    {
        try
        {
            File inFile = new File(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inFile)));
            
            int index,index2;
            
            DataLine = br.readLine();
            int numNodes = Integer.parseInt(DataLine);
            
            System.out.println(numNodes);
            
            for(index =0; index<numNodes; index++)
            {
            	DataLine = br.readLine();
            	StringTokenizer stToken1 = new StringTokenizer(DataLine," ");
            	int nodeId = Integer.parseInt(stToken1.nextToken());
            	int nodeType = Integer.parseInt(stToken1.nextToken());
            	String nodeName = stToken1.nextToken();
            	nodeList.add(new Node(nodeId,nodeType,nodeName));   	
            }
            
            DataLine = br.readLine();
            int numLinks = Integer.parseInt(DataLine);
            
            for(index =0; index<numLinks; index++)
            {
            	DataLine = br.readLine();
            	StringTokenizer stToken1 = new StringTokenizer(DataLine," ");
            	int linkId = Integer.parseInt(stToken1.nextToken());
            	int linkType = Integer.parseInt(stToken1.nextToken());
            	int UpNodeId = Integer.parseInt(stToken1.nextToken());
            	int DnNodeId = Integer.parseInt(stToken1.nextToken());
            	int segmentCount = Integer.parseInt(stToken1.nextToken());
                
                numberOfSegments += segmentCount;
            	linkList.add(new Link(index, linkId, linkType, UpNodeId, DnNodeId, segmentCount));
            	
            	List<Segment> segmentList= new ArrayList<Segment>();
            	
    		for(index2 =0; index2<segmentCount; index2++)
    		{
    			DataLine = br.readLine();
    			StringTokenizer stToken2 = new StringTokenizer(DataLine," ");
    			int segmentId = Integer.parseInt(stToken2.nextToken()); 
    			double startPntX = Double.parseDouble(stToken2.nextToken());
    			double startPntY = Double.parseDouble(stToken2.nextToken());
    			double endPntX = Double.parseDouble(stToken2.nextToken());
    			double endPntY = Double.parseDouble(stToken2.nextToken());
                        double sensor = Double.parseDouble(stToken2.nextToken());
    			int stripCount = Integer.parseInt(stToken2.nextToken());
                        int pedestriancount=Integer.parseInt(stToken2.nextToken());
                        boolean lastSegment = false;
                        if(index2 == segmentCount-1)
                            lastSegment = true;
                        segmentList.add(new Segment(index, index2, segmentId,
                            startPntX, startPntY, endPntX, endPntY, sensor, stripCount, lastSegment,pedestriancount));
    				
    			}
    			linkList.get(index).SegementInitialization(segmentList);
            }
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
    
    public List<Node> getNodeList()
    {
    	return nodeList;
    }
    
    public List<Link> getLinkList()
    {
    	return linkList;
    }

    public int getNumberOfSegments()
    {
        return numberOfSegments;
    }
    public boolean isFileExists(String fileName)
    {
        File file = new File(fileName);
        return file.exists();
    }

    public boolean deleteFile(String fileName)
    {
        File file = new File(fileName);
        return file.delete();
    }

}
