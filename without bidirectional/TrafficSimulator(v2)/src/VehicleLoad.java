import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Loads vehicle demands and finds the corresponding paths from input text file
 * @author USER
 */

public class VehicleLoad {
    
    String fileName1 = "Path.txt";
    String fileName2 = "Demand.txt";

    private List<Path> pathList= new ArrayList<Path>();
    private List<Path> pathRemovalList= new ArrayList<Path>();
    private List<Demand> demandList= new ArrayList<Demand>();
    
    //Constructor gets data of path and demand, and sets paths for specific demand
    public VehicleLoad()
    {
        readPath();
        readDemand();
        addPath();
    }
    
   /*scans 'path.txt' file to determine source node and destination node and 
    *place the index sequence of links into a list 
    */
    public void readPath()
    {
        String DataLine = "";
        try
        {
            File inFile = new File(fileName1);
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inFile)));

            int index;

            DataLine = br.readLine();
            int numPaths = Integer.parseInt(DataLine);

            for(index =0; index<numPaths; index++)
            {
            	DataLine = br.readLine();
            	StringTokenizer stToken = new StringTokenizer(DataLine," ");
            	int nodeId1 = Integer.parseInt(stToken.nextToken());
            	int nodeId2 = Integer.parseInt(stToken.nextToken());
                pathList.add(new Path(nodeId1,nodeId2));              //adds path to the path list
                while(stToken.hasMoreTokens())
                {
                    int linkIndex = Integer.parseInt(stToken.nextToken());
                    pathList.get(index).addLinkIndex(linkIndex);      //adds link index to the corresponding path's linkindexlist
                }
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
    
    //scans 'demand.txt' and creates demand objects
    public void readDemand()
    {
        String DataLine = "";
        try
        {
            File inFile = new File(fileName2);
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inFile)));

            int index;

            DataLine = br.readLine();
            int numDemands = Integer.parseInt(DataLine);

            for(index =0; index<numDemands; index++)
            {
            	DataLine = br.readLine();
            	StringTokenizer stToken = new StringTokenizer(DataLine," ");
            	int nodeId1 = Integer.parseInt(stToken.nextToken());
            	int nodeId2 = Integer.parseInt(stToken.nextToken());
                int demand = Integer.parseInt(stToken.nextToken());
                demandList.add(new Demand(nodeId1,nodeId2,demand));
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
    
    //adds correct paths to the corresponding demand
    public void addPath()
    {
        int i,j,k;
        for(i=0;i<demandList.size();i++)
        {
            for(j=0;j<pathList.size();j++)
            {
                if(pathList.get(j).correctPath(demandList.get(i).getSource(),demandList.get(i).getDestination()))
                {
                    demandList.get(i).addPath(pathList.get(j));
                    pathRemovalList.add(pathList.get(j));          //lists paths that are already chosen for certain demand
                }
            }
            for(k=0;k<pathRemovalList.size();k++)
            {
                pathList.remove(pathRemovalList.get(k));          //deletes paths that are already chosen for certain demand
            }

        }
    }
    
    //returns demand list
    public List<Demand> getDemand()
    {
        return demandList;
    }
}
