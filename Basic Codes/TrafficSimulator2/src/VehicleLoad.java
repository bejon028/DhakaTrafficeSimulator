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

public class VehicleLoad {
    
    String fileName1 = "Path1.txt";
    String fileName2 = "Demand1.txt";

    private List<Path> pathList= new ArrayList<Path>();
    private List<Path> pathRemovalList= new ArrayList<Path>();
    private List<Demand> demandList= new ArrayList<Demand>();

    public VehicleLoad()
    {
        readPath();
        readDemand();
        addPath();
    }
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
                pathList.add(new Path(nodeId1,nodeId2));
                while(stToken.hasMoreTokens())
                {
                    int linkIndex = Integer.parseInt(stToken.nextToken());
                    pathList.get(index).addLinkIndex(linkIndex);
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
                //demand=1;
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
                    pathRemovalList.add(pathList.get(j));
                }
            }
            for(k=0;k<pathRemovalList.size();k++)
            {
                pathList.remove(pathRemovalList.get(k));
            }

        }
    }
    public List<Demand> getDemand()
    {
        return demandList;
    }
}
